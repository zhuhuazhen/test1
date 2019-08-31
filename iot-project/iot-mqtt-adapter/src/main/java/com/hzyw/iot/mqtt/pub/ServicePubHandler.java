package com.hzyw.iot.mqtt.pub;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.hzyw.iot.config.ApplicationConfig;
import com.hzyw.iot.config.MqttConnectionConfig;
import com.hzyw.iot.service.RedisService;
import com.hzyw.iot.util.GatewayMqttUtil;
import com.hzyw.iot.vo.dataaccess.DataType;
import com.hzyw.iot.vo.dataaccess.MessageVO;
import com.hzyw.iot.vo.dataaccess.ServiceDataVO;

import org.slf4j.Logger;

/**
 * 服务上线
 */
@Service 
public class ServicePubHandler  extends AbstractPubHandler  {
	private static Logger logger = LoggerFactory.getLogger(ServicePubHandler.class);
	@Autowired
	private ApplicationConfig applicationConfig;//全局配置
	
	@Autowired
	private RedisService redisService;
	
	@Autowired
	private MqttConnectionConfig mqttConnectionConfig;
	public ServicePubHandler(){}
	
	@PostConstruct
    public void init() {
		try {
			setConfig(getConfig(),mqttConnectionConfig);
		} catch (MqttException e) {
			e.printStackTrace();
		}
    }
	
	//需要自己定义自己的配置
	public Map<String,String> getConfig(){
		Map<String,String> config = new HashMap<String,String>();
		config.put("topic", mqttConnectionConfig.getServiceOnline().get("topic"));
		config.put("qos", mqttConnectionConfig.getServiceOnline().get("qos"));
		config.put("url", mqttConnectionConfig.getComm().get("url"));
		config.put("userName", mqttConnectionConfig.getComm().get("userName"));
		config.put("password", mqttConnectionConfig.getComm().get("password"));
		config.put("clientId", mqttConnectionConfig.getServiceOnline().get("clientId"));
		return config;
	}

	/* 
	 * 服务上线
	 * (non-Javadoc)
	 * @see com.hzyw.iot.mqtt.pub.AbstractPubHandler#Publish(java.lang.String[])
	 */
	@Override
	public void Publish(String ... content) {
		try {
			logger.info("==============ServicePubHandler平台上线,连接中==============================");
			////NEW一个客户端连接对象
			this.setMqttClient(new MqttClient(this.getUrl(), this.getClientId(), new MemoryPersistence()));
			// 遗愿处理
			String failoverTopic = this.getTopic();//下线主题
			this.getOptions().setWill(failoverTopic, content[1].getBytes(), this.getQos(), true);
			// 建立连接
			this.getMqttClient().connect(getOptions());
			// 创建消息
			MqttMessage message = new MqttMessage(content[0].getBytes());
			// 设置消息的服务质量
			message.setQos(this.getQos());
			// 发布上线消息
			this.getMqttClient().publish(this.getTopic(), message.toString().getBytes(),1,true);
			logger.info("==============ServicePubHandler平台上线,已上线==============================");

			//更新服务上线状态
			redisService.hmSet(GatewayMqttUtil.rediskey_iot_cache_dataAccess, applicationConfig.getServiceId(), GatewayMqttUtil.onLine);
		} catch (MqttException me) {
			logger.error("服务上线-发布异常:", me);
			redisService.hmSet(GatewayMqttUtil.rediskey_iot_cache_dataAccess, applicationConfig.getServiceId(), GatewayMqttUtil.offLine);
			try {
				this.getMqttClient().disconnect();
			} catch (MqttException e) {
				logger.error("服务上线-发布异常-关闭MqttClient().disconnect异常:", e);
			}
			try {
				this.getMqttClient().close();
			} catch (MqttException e) {
				logger.error("服务上线-发布异常-关闭MqttClient().disconnect异常:", e);
			}
		}
	}
  
}
