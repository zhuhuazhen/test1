package com.hzyw.iot.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

//@Configuration 标识这是一个配置类
//@Component(“configData”) // 添加到bean容器时指定其名为 configData
@Configuration
@ConfigurationProperties(prefix = "hz.mqtt.topic") // 读取前缀为 hz.mqtt.topic 的内容
@PropertySource(value = { "classpath:hzTopicForMqtt.properties" }, encoding = "utf-8")
public class HzTopicConfig {
	
	private Map<String,String>  service = new HashMap<String,String>();
	
	private Map<String,String>  comm = new HashMap<String,String>();

	public Map<String, String> getService() {
		return service;
	}

	public void setService(Map<String, String> service) {
		this.service = service;
	}

	public Map<String, String> getComm() {
		return comm;
	}

	public void setComm(Map<String, String> comm) {
		this.comm = comm;
	}
	

}
