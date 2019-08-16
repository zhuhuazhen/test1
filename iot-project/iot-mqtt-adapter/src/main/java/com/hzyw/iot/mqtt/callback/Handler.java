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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.hzyw.iot.config.ApplicationConfig;
import com.hzyw.iot.kafka.KafkaCommon;
import com.hzyw.iot.service.RedisService;
import com.hzyw.iot.util.GatewayMqttUtil;
import com.hzyw.iot.vo.dataaccess.DataType;
import com.hzyw.iot.vo.dataaccess.DevInfoDataVO;
import com.hzyw.iot.vo.dataaccess.DevOnOffline;
import com.hzyw.iot.vo.dataaccess.DevSignlResponseDataVO;
import com.hzyw.iot.vo.dataaccess.MessageVO;
import com.hzyw.iot.vo.dataaccess.MetricInfoResponseDataVO;
import com.hzyw.iot.vo.dataaccess.ResponseDataVO;
import com.hzyw.iot.vo.dataaccess.ResultMessageVO;

import cn.hutool.core.convert.Convert;

//import com.hzyw.iot.redis.JedisPoolUtils;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

/**
 * 处理消息
 */
@Component
public class Handler {
	@Autowired
	private RedisService redisService; //redis工具类
	
	@Autowired
	private KafkaCommon kafkaCommon;
	
	@Autowired
	private ApplicationConfig applicationConfig;
	
	private static final Logger log = LoggerFactory.getLogger(Handler.class);
	/**
	 * 处理逻辑消息
	 */
	public void handlerMessages(String topic, String message) {
		JSONObject jsonObject =null;
		try{
			 jsonObject = JSONUtil.parseObj(message);//messageVO
		    //yes, it is
		}catch(Exception e){
			log.error("json数据解析错误",e);
		}
		
		JSONObject data = JSONUtil.parseObj(jsonObject.get(GatewayMqttUtil.dataModel_messageVO_data));
		// 判断type类型
		String type = (String)jsonObject.get(GatewayMqttUtil.dataModel_messageVO_type);
		String deviceId = (String)jsonObject.get(GatewayMqttUtil.dataModel_messageVO_data_deviceId);
		DataType enumType = DataType.getByValue(type);
		//消息结构初始化
		MessageVO messageVo =null;
		
		//在这里统一做消息认证，认证通过继续执行 
		//---代码待增量开发加入即可..
		
		//下发的时候才需要判断设备是否在线，因上报的时候默认设备就是在线的
		
		//应该根据设备激活状态（建议这个状态丢到TAG里面去）+是否上线来决定以下的执行
		
		switch (enumType) {
		case DevInfoResponse://属性上报
			//设备基本消息
			DevInfoDataVO devInfoDataVo = new DevInfoDataVO();
			devInfoDataVo.setDeviceId(data.get(GatewayMqttUtil.dataModel_messageVO_data_deviceId).toString());
			devInfoDataVo.setDeviceId(data.get(GatewayMqttUtil.dataModel_messageVO_data_status).toString());
			devInfoDataVo.setAttributers((List<String>)data.get(data.get(GatewayMqttUtil.dataModel_messageVO_data_attributers)));
			devInfoDataVo.setMethods((List<String>)data.get(data.get(GatewayMqttUtil.dataModel_messageVO_data_methods)));
			devInfoDataVo.setDefinedAttributers((List<String>)data.get(data.get(GatewayMqttUtil.dataModel_messageVO_data_definedAttributers) ));
			devInfoDataVo.setDefinedMethods((List<String>)data.get(data.get(GatewayMqttUtil.dataModel_messageVO_data_definedMethods) ));
			devInfoDataVo.setSignals((List<Map>)data.get(data.get(GatewayMqttUtil.dataModel_messageVO_data_signals) ));
			/*//消息结构
			messageVo.setType(type);
			messageVo.setSeq(Convert.toLong(jsonObject.get(GatewayMqttUtil.dataModel_messageVO_seq)));
			messageVo.setTimestamp(Convert.toLong(jsonObject.get(GatewayMqttUtil.dataModel_messageVO_timestamp)));//消息上报时间
			messageVo.setMsgId((String)jsonObject.get(GatewayMqttUtil.dataModel_messageVO_msgId));
			messageVo.setData(devInfoDataVo);*/
			
			messageVo= getMessageVO(devInfoDataVo,type,Convert.toLong(jsonObject.get(GatewayMqttUtil.dataModel_messageVO_seq)),Convert.toLong(jsonObject.get(GatewayMqttUtil.dataModel_messageVO_timestamp)),jsonObject.get(GatewayMqttUtil.dataModel_messageVO_msgId).toString());
			//消息结构
			sendKafka(JSON.toJSONString(messageVo),applicationConfig.getDevInfoResponseTopic());
			break;
			
		case Response://请求返回
			ResponseDataVO responseDataVo = new ResponseDataVO();
			responseDataVo.setDeviceId(data.get(GatewayMqttUtil.dataModel_messageVO_data_deviceId).toString());
			responseDataVo.setMethods((List<Map>)data.get(data.get(GatewayMqttUtil.dataModel_messageVO_data_methods)));
			responseDataVo.setTags((Map)data.get(data.get(GatewayMqttUtil.dataModel_messageVO_data_tags)));
			//消息结构
			messageVo= getMessageVO(responseDataVo,type,Convert.toLong(jsonObject.get(GatewayMqttUtil.dataModel_messageVO_seq)),Convert.toLong(jsonObject.get(GatewayMqttUtil.dataModel_messageVO_timestamp)),jsonObject.get(GatewayMqttUtil.dataModel_messageVO_msgId).toString());
			//kafka处理
			sendKafka(JSON.toJSONString(messageVo),applicationConfig.getDataAcessTopic());
			break;
		case MetricInfoResponse://设备状态数据上报
			MetricInfoResponseDataVO  metricInfoResponseDataVO = new MetricInfoResponseDataVO();
			System.out.println(data.get(GatewayMqttUtil.dataModel_messageVO_data_deviceId));
			metricInfoResponseDataVO.setDeviceId(data.get(GatewayMqttUtil.dataModel_messageVO_data_deviceId).toString());
			metricInfoResponseDataVO.setAttributers((List<Map>)data.get(data.get(GatewayMqttUtil.dataModel_messageVO_data_attributers)));
			metricInfoResponseDataVO.setTags((Map)data.get(data.get(GatewayMqttUtil.dataModel_messageVO_data_tags)));
			//消息结构
			messageVo= getMessageVO(metricInfoResponseDataVO,type,Convert.toLong(jsonObject.get(GatewayMqttUtil.dataModel_messageVO_seq)),Convert.toLong(jsonObject.get(GatewayMqttUtil.dataModel_messageVO_timestamp)),jsonObject.get(GatewayMqttUtil.dataModel_messageVO_msgId).toString());
			//kafka处理
			sendKafka(JSON.toJSONString(messageVo),applicationConfig.getDataAcessTopic());
			break;
		case DevSignlResponse://设备信号上报
			DevSignlResponseDataVO devSignlResponseDataVO = new DevSignlResponseDataVO();
			devSignlResponseDataVO.setDeviceId(data.get(GatewayMqttUtil.dataModel_messageVO_data_deviceId).toString());
			devSignlResponseDataVO.setSignals((List<Map>)data.get(data.get(GatewayMqttUtil.dataModel_messageVO_data_signals)));
			devSignlResponseDataVO.setTags((Map)data.get(data.get(GatewayMqttUtil.dataModel_messageVO_data_tags)));
			//消息结构
			messageVo= getMessageVO(devSignlResponseDataVO,type,Convert.toLong(jsonObject.get(GatewayMqttUtil.dataModel_messageVO_seq)),Convert.toLong(jsonObject.get(GatewayMqttUtil.dataModel_messageVO_timestamp)),jsonObject.get(GatewayMqttUtil.dataModel_messageVO_msgId).toString());
			//kafka处理
			sendKafka(JSON.toJSONString(messageVo),applicationConfig.getDataAcessTopic());
			break;
		case DevOnline://DEV在线
			DevOnOffline devOnline = new DevOnOffline();
			devOnline.setDeviceId(data.get(GatewayMqttUtil.dataModel_messageVO_data_deviceId).toString());
			devOnline.setStatus(data.get(GatewayMqttUtil.dataModel_messageVO_data_deviceId).toString());
			devOnline.setTags((Map)data.get(data.get(GatewayMqttUtil.dataModel_messageVO_data_tags)));
			//消息结构
			messageVo= getMessageVO(devOnline,type,Convert.toLong(jsonObject.get(GatewayMqttUtil.dataModel_messageVO_seq)),Convert.toLong(jsonObject.get(GatewayMqttUtil.dataModel_messageVO_timestamp)),jsonObject.get(GatewayMqttUtil.dataModel_messageVO_msgId).toString());
			//kafka处理
			sendKafka(JSON.toJSONString(messageVo).toString(),applicationConfig.getDataAcessTopic());
			//更新缓存中的设备上线状态
			redisService.hmSet(GatewayMqttUtil.rediskey_iot_cache_dataAccess, (String)data.get(GatewayMqttUtil.dataModel_messageVO_data_deviceId), GatewayMqttUtil.onLine);
            break;
		case DevOffline://DEV离线
			DevOnOffline devOffline = new DevOnOffline();
			devOffline.setDeviceId(data.get(GatewayMqttUtil.dataModel_messageVO_data_deviceId).toString());
			devOffline.setStatus(data.get(GatewayMqttUtil.dataModel_messageVO_data_deviceId).toString());
			devOffline.setTags((Map)data.get(data.get(GatewayMqttUtil.dataModel_messageVO_data_tags)));
			//消息结构
			messageVo= getMessageVO(devOffline,type,Convert.toLong(jsonObject.get(GatewayMqttUtil.dataModel_messageVO_seq)),Convert.toLong(jsonObject.get(GatewayMqttUtil.dataModel_messageVO_timestamp)),jsonObject.get(GatewayMqttUtil.dataModel_messageVO_msgId).toString());
			//kafka处理
			sendKafka(JSON.toJSONString(messageVo),applicationConfig.getDataAcessTopic());
			//更新缓存中的设备下线状态
			redisService.hmSet(GatewayMqttUtil.rediskey_iot_cache_dataAccess, (String)data.get(GatewayMqttUtil.dataModel_messageVO_data_deviceId), GatewayMqttUtil.offLine);
            break;
        default:
            System.out.println("未知消息类型");
            break;
		}

	}
	
	/**
	 * 消息结构处理
	 */
	@SuppressWarnings("unchecked")
	public <T> MessageVO<T>  getMessageVO(T data,String type,Long seq,Long timestamp,String msgId) {
		//消息结构
		ResultMessageVO<T> messageVo = new ResultMessageVO<T>();
		//消息结构
		messageVo.setType(type);
		messageVo.setSeq(seq);
		messageVo.setTimestamp(timestamp);//消息上报时间
		messageVo.setMsgId(msgId);
		messageVo.setData(data);
		return messageVo;
	}

	/**
	 * setKafka处理
	 */
	public void sendKafka(String messageVo, String topic) {
		try {
			Producer<String, String> producer = kafkaCommon.getKafkaProducer();
			producer.send(new ProducerRecord<>(topic, messageVo.toString()));
			redisService.hmSet(GatewayMqttUtil.rediskey_iot_cache_dataAccess, applicationConfig.getServiceId(), GatewayMqttUtil.onLine);
		} catch (Exception e) {
			log.error(">>>Handler::handlerMessages::sendKafka ; producer.send异常 !",e);
		}
	}
	 
}
