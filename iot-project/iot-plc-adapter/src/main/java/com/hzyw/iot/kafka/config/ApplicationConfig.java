package com.hzyw.iot.kafka.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {
	
	//消费PLC指令
	@Value(value = "${iot.plc.adapter.topic.producer.plcOrder}")
	private String  plcOrder;
	
	@Value(value = "${iot.plc.adapter.topic.producer.plcOrder.consumergroup}")
	private static String  kafkaPlcConsumerGroup;
	 
	public static String getKafkaPlcConsumerGroup() {
		return kafkaPlcConsumerGroup;
	}

	public static void setKafkaPlcConsumerGroup(String kafkaPlcConsumerGroup) {
		ApplicationConfig.kafkaPlcConsumerGroup = kafkaPlcConsumerGroup;
	}

	public String plcOrder() {
		return plcOrder;
	}

	public void setDevInfoResponseTopic(String plcOrder) {
		this.plcOrder = plcOrder;
	}

}
