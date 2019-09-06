package com.hzyw.iot.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties(prefix = "mqtt.connection") 
@PropertySource(value = { "classpath:mqttConnection.properties" }, encoding = "utf-8")
public class MqttConnectionConfig {
	
	private Map<String,String>  comm = new HashMap<String,String>();
	private Map<String,String>  serviceOffline = new HashMap<String,String>();

	
	public Map<String, String> getComm() {
		return comm;
	}

	public void setComm(Map<String, String> comm) {
		this.comm = comm;
	}


	public Map<String, String> getServiceOffline() {
		return serviceOffline;
	}

	public void setServiceOffline(Map<String, String> serviceOffline) {
		this.serviceOffline = serviceOffline;
	}

	
	
	

}
