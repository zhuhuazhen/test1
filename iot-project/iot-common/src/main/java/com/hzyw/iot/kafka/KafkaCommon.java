package com.hzyw.iot.kafka;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisOperations;

//implements RedisOperations<K, V>, BeanClassLoaderAware
//@Service
public class KafkaCommon extends AbstractKafkaCommon {

	public KafkaConsumer<String, String> getKafkaConsumer(String topic,String groupId) throws Exception {
		KafkaConsumer<String, String> consumer = new KafkaConsumer<>(this.getConfig());
		return consumer;
	}

	public Producer<String, String> getKafkaProducer(String topic) throws Exception {
		Producer<String, String> producer = new KafkaProducer<>(this.getConfig());
		return producer;
	}
	
	public Producer<String, String> getKafkaProducer(String topic,String key) throws Exception {
		Producer<String, String> producer = new KafkaProducer<>(this.getConfig());
		return producer;
	}
	
	public Producer<String, String> getKafkaProducer(String topic,String key,String partition) throws Exception {
		Producer<String, String> producer = new KafkaProducer<>(this.getConfig());
		return producer;
	}
 
}
