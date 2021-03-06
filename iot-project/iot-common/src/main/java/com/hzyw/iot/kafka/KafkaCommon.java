package com.hzyw.iot.kafka;

import java.util.Properties;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.stereotype.Service;

import com.hzyw.iot.config.KafkaConfig;

import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisOperations;

//@Service  
public class KafkaCommon extends AbstractKafkaCommon {

	public KafkaConsumer<String, String> getKafkaConsumer(String groupId) throws Exception {
		Properties config = KafkaConfig.copeProperty(this.getConsumerConfig()); //this.getConfig()返回的对象是KafkaCommon单例下的一个属性，需要拷贝新的对象
		config.put(KafkaConfig.group_id, groupId);   //动态指定消费组
		KafkaConsumer<String, String> consumer = new KafkaConsumer<>(config);
		return consumer;
	}

	public Producer<String, String> getKafkaProducer() throws Exception {
		Properties config = KafkaConfig.copeProperty(this.getProducerConfig()); 
		Producer<String, String> producer = new KafkaProducer<>(config);
		return producer;
	}
 
}
