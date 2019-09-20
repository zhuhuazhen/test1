package com.hzyw.iot.kafka;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.hzyw.iot.kafka.KafkaCommon;
import com.hzyw.iot.kafka.config.ApplicationConfig;
import com.hzyw.iot.netty.channelhandler.CommandHandler;
import com.hzyw.iot.service.GateWayService;
import com.hzyw.iot.util.constant.ProtocalAdapter;
import com.hzyw.iot.vo.dataaccess.MessageVO;
import com.hzyw.iot.vo.dataaccess.RequestDataVO;
import com.hzyw.iot.vo.dataaccess.ResultMessageVO;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

/**
 * 1，从KAFKA获取下发数据（实时获取） 2, 判断服务是否在线 3，判断设备是否已上线 4，发送到MQTT服务器，关闭mqtt连接
 * 5，如果存在异常，检查看KAFKA是否已经提交offset
 */
public class DataSendDownConsumer implements Runnable {

	private static Logger logger = LoggerFactory.getLogger(DataSendDownConsumer.class);
	
	private KafkaCommon kafkaCommon;
	
	private ApplicationConfig applicationConfig;
	
	
	private GateWayService gateWayService;

	public DataSendDownConsumer() {
	}

	public DataSendDownConsumer(KafkaCommon kafkaCommon, ApplicationConfig applicationConfig,
			 GateWayService gateWayService) {
		this.kafkaCommon = kafkaCommon;
		this.applicationConfig = applicationConfig;
		this.gateWayService = gateWayService;
	}

	/**
	 * 获取kafka的数据
	 */
	@SuppressWarnings("static-access")
	public void consumerProcess() {
		String topic = "testbyzhu" ; // applicationConfig.plcOrder(); 
		try {
			//获取kafka主题
			KafkaConsumer<String, String> consumer =  kafkaCommon.getKafka();
			consumer.subscribe(Arrays.asList(topic));//订阅主题
			
			for(;;){
				logger.info(">>>DataSendDownConsumer::consumerProcess waiting... " );
				ConsumerRecords<String, String> records = consumer.poll(1024);
				process(records);
				try {
					Thread.currentThread().sleep(1000 * 5);
				} catch (InterruptedException e) {
					logger.error(">>>DataSendDownConsumer::consumerProcess::process; currentThread().sleep exception!",e);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(">>>DataSendDownConsumer::consumerProcess; kafkaCommon.getKafkaConsumer() exception!",e);
		}
	}
	//通过netty发送到plc设备
	public void process(ConsumerRecords<String, String> records){
		String value;
		for (ConsumerRecord<String, String> record : records) {
			value = record.value();
			if(ObjectUtil.isNotNull(value)) {
				//CommandHandler.writeCommand("12345000000000100", value, 2);
				ProtocalAdapter protocalAdapter = new ProtocalAdapter();
				try {
					String plcTest = protocalAdapter.messageRequest(JSON.parseObject(JSON.toJSONString(value)));
					CommandHandler.writeCommandByRequestMessageVO(plcTest);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("=========================进入下发流程==========================================");
				//已验证成功开关灯   可以通过测试类KafkaProducerExampleByzhu发起调试即可
			}
		}
		
		
	}

	@Override
	public void run() {
		this.consumerProcess();
	}
	


}
