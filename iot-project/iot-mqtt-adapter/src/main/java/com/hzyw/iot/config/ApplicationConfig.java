package com.hzyw.iot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {
	//iot.mqtt.adapter.serviceid=service_00001
	/*@Value(value = "${iot.mqtt.adapter.kafkatopic}")
	private String  kafkaTopic;
	
	*/
	@Value(value = "${iot.mqtt.adapter.serviceid}")
	private String  serviceId;
	
	@Value(value = "${iot.mqtt.adapter.topic.producer.dataAcess}")
	private String  dataAcessTopic;
	
	@Value(value = "${iot.mqtt.adapter.topic.producer.devInfoResponse}")
	private String  devInfoResponseTopic;
	
	@Value(value = "${iot.mqtt.adapter.topic.consumer.datasend}")
	private String  datasendTopic;
	
	@Value(value = "${iot.mqtt.adapter.topic.consumer.datasend.groupid}")
	private String  datasendTopicGroup;
	 
	public String getDataAcessTopic() {
		return dataAcessTopic;
	}

	public void setDataAcessTopic(String dataAcessTopic) {
		this.dataAcessTopic = dataAcessTopic;
	}

	public String getDevInfoResponseTopic() {
		return devInfoResponseTopic;
	}

	public void setDevInfoResponseTopic(String devInfoResponseTopic) {
		this.devInfoResponseTopic = devInfoResponseTopic;
	}

	public String getDatasendTopic() {
		return datasendTopic;
	}

	public void setDatasendTopic(String datasendTopic) {
		this.datasendTopic = datasendTopic;
	}

	public String getDatasendTopicGroup() {
		return datasendTopicGroup;
	}

	public void setDatasendTopicGroup(String datasendTopicGroup) {
		this.datasendTopicGroup = datasendTopicGroup;
	}

	/*public String getKafkaTopic() {
		return kafkaTopic;
	}

	public void setKafkaTopic(String kafkaTopic) {
		this.kafkaTopic = kafkaTopic;
	}

	*/
	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	 
	

}
