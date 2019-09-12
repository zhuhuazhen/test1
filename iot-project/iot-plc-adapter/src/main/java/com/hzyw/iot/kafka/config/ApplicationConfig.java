package com.hzyw.iot.kafka.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {
	
	//消费PLC指令
	@Value(value = "${iot.plc.adapter.topic.producer.plcOrder}")
	private String  plcOrder;
	
	 
	
	public String plcOrder() {
		return plcOrder;
	}

	public void setDevInfoResponseTopic(String plcOrder) {
		this.plcOrder = plcOrder;
	}
	

}
