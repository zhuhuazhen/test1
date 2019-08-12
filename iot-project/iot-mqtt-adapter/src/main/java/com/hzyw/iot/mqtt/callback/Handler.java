package com.hzyw.iot.mqtt.callback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;

import com.hzyw.iot.kafka.KafkaCommon;
import com.hzyw.iot.vo.dataaccess.DevInfoDataVO;
import com.hzyw.iot.vo.dataaccess.MessageVO;

import cn.hutool.core.convert.Convert;

//import com.hzyw.iot.redis.JedisPoolUtils;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

/**
 * 处理消息
 */
public class Handler {
	@Autowired
	private KafkaCommon kafkaCommon;
	/**
	 * 处理逻辑消息
	 */
	public void handlerMessages(String topic, String message) {
		JSONObject jsonObject = JSONUtil.parseObj(message);
		JSONObject data = JSONUtil.parseObj(jsonObject.get("data"));
		// 判断type类型
		switch (jsonObject.get("type").toString()) {
		case "MessageVO":
			
			//设备基本消息
			DevInfoDataVO dataVo = new DevInfoDataVO();
			dataVo.setDeviceId(data.get("deviceId").toString());
			dataVo.setDeviceId(data.get("status").toString());
			dataVo.setAttributers((List<String>)data.get("attributers"));
			dataVo.setMethods((List<String>)data.get("method"));
			dataVo.setDefinedAttributers((List<String>)data.get("definedAttributer"));
			dataVo.setDefinedMethods((List<String>)data.get("definedMethod"));
			dataVo.setSignals((List<Map>)data.get("signal"));
			
			//消息结构
			MessageVO messageVo = new MessageVO();
			messageVo.setType(jsonObject.get("type").toString());
			messageVo.setSeq(Convert.toLong(jsonObject.get("seq")));
			messageVo.setTimestamp(Convert.toLong(jsonObject.get("timestamp")));
			messageVo.setData(dataVo);
			
			JSONObject messageVoJson = JSONUtil.parseObj(messageVo);
			System.out.println(messageVoJson);
			
			//setKafka
			try {
				Producer<String, String> producer = kafkaCommon.getKafkaProducer(topic);
				producer.send(new ProducerRecord<>(topic, messageVoJson.toString()));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			break;
		}
		
		//System.out.println("在线离线状态:" + JedisPoolUtils.getPool("adapter01"));
	}
	/*public void handlerMessages(String topic, String message) {
		JSONObject jsonObject = JSONUtil.parseObj(message);
		JSONObject data = JSONUtil.parseObj(jsonObject.get("data"));
		// 判断type类型
		switch (jsonObject.get("type").toString()) {
		case "serviceOnOff":// 是否在线
			if (data.get("status").equals("online")) {
				//JedisPoolUtils.setPool(data.get("serviceid").toString(), "online");
			} else {
				//JedisPoolUtils.setPool(data.get("serviceid").toString(), "offline");
			}
			break;
		}
		//System.out.println("在线离线状态:" + JedisPoolUtils.getPool("adapter01"));
	}*/

}
