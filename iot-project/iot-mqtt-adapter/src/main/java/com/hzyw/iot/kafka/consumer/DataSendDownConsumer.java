package com.hzyw.iot.kafka.consumer;

import java.util.Arrays;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.hzyw.iot.config.ApplicationConfig;
import com.hzyw.iot.kafka.KafkaCommon;
import com.hzyw.iot.mqtt.callback.MqttCallbackImpl;
import com.hzyw.iot.mqtt.pub.CommPubHandler;
import com.hzyw.iot.service.GateWayService;
import com.hzyw.iot.util.GatewayMqttUtil;
import com.hzyw.iot.vo.dataaccess.MessageVO;
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
	
	private CommPubHandler commPubHandler;
	
	private GateWayService gateWayService;

	public DataSendDownConsumer() {
	}

	public DataSendDownConsumer(KafkaCommon kafkaCommon, ApplicationConfig applicationConfig,
			CommPubHandler commPubHandler, GateWayService gateWayService) {
		this.kafkaCommon = kafkaCommon;
		this.applicationConfig = applicationConfig;
		this.commPubHandler = commPubHandler;
		this.gateWayService = gateWayService;
	}

	/**
	 * 获取kafka的数据，并处理下发Mqtt(抽到外层的service业务方法里去)
	 */
	@SuppressWarnings("static-access")
	public void consumerProcess() {
		String topic = applicationConfig.getDatasendTopic(); //"iot_topic_dataAcess_request"
		String groupId = applicationConfig.getDatasendTopicGroup(); //"iot_topic_dataAcess_request_group"
		try {
			KafkaConsumer<String, String> consumer = kafkaCommon.getKafkaConsumer(groupId);//指定消费组
			consumer.subscribe(Arrays.asList(topic));//订阅主题
			
			for(;;){
				logger.info(">>>DataSendDownConsumer::consumerProcess waiting... " );
				ConsumerRecords<String, String> records = consumer.poll(100);
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
	
	public void process(ConsumerRecords<String, String> records){
			String value,type,deviceId,gatewayId;boolean isOnline;
			int p = 0;
			for (ConsumerRecord<String, String> record : records) {
				JSONObject jsonObject = new JSONObject();
				try{//不阻塞
					p++;
					value = record.value();
					if(ObjectUtil.isNotNull(value)) {
						jsonObject = JSONUtil.parseObj(value);//格式=messageVO
						JSONObject data = JSONUtil.parseObj(jsonObject.get(GatewayMqttUtil.dataModel_messageVO_data));
						// 判断type类型
						type = (String)jsonObject.get(GatewayMqttUtil.dataModel_messageVO_type);
						deviceId = (String)jsonObject.get(GatewayMqttUtil.dataModel_messageVO_data_deviceId);//设备ID
						Map<String,String> tags = (Map<String,String>)jsonObject.get(GatewayMqttUtil.dataModel_messageVO_data_tags);
						gatewayId = tags.get(GatewayMqttUtil.dataModel_messageVO_data_gatewayId) ;//网关ID
						isOnline = gateWayService.deviceOnLine(deviceId);//设备是否在线？
						logger.info(">>>DataSendDownConsumer::consumerProcess;  p=" + p);
	
						logger.info(">>>DataSendDownConsumer::consumerProcess; type/gatewayId/deviceId/isOnline=" + type +"/"+ gatewayId +"/"+ deviceId +"/"+ isOnline);
						//推送到MQTT
						if(isOnline){
							logger.info(">>>DataSendDownConsumer::consumerProcess::Publish(下发);.... /topic/value =" + commPubHandler.getTopic()+ "/"+value );
							commPubHandler.Publish(value,gatewayId); 
						}else{
							//反馈失败信息
							jsonObject.put(GatewayMqttUtil.dataModel_messageVO_messageCode, GatewayMqttUtil.return_devoffline_code);
							jsonObject.put(GatewayMqttUtil.dataModel_messageVO_message, GatewayMqttUtil.return_devoffline_message);//反馈不在线
							sendKafka(JSON.toJSONString(jsonObject),applicationConfig.getDataAcessTopic());
						}
					}
				}catch(Exception e){
					//反馈异常失败信息
					jsonObject.put(GatewayMqttUtil.dataModel_messageVO_messageCode, GatewayMqttUtil.return_fail_code);
					jsonObject.put(GatewayMqttUtil.dataModel_messageVO_message, GatewayMqttUtil.return_fail_message+e.getMessage());//反馈不在线
					sendKafka(JSON.toJSONString(jsonObject),applicationConfig.getDataAcessTopic());
					logger.error(">>>DataSendDownConsumer::consumerProcess::process; 处理ConsumerRecord  exception!",e);
				}
			}
	}

	@Override
	public void run() {
		this.consumerProcess();
	}
	
	public void sendKafka(String messageVo, String topic) {
		try {
			Producer<String, String> producer = kafkaCommon.getKafkaProducer();
			producer.send(new ProducerRecord<>(topic, messageVo.toString()));
		} catch (Exception e) {
			logger.error(">>>DataSendDownConsumer::process::sendKafka ; producer.send异常 !",e);
		}
	}

}
