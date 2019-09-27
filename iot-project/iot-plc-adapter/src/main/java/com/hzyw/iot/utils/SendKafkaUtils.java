package com.hzyw.iot.utils;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class SendKafkaUtils {
	/**
	 * setKafka发送
	 */
	public static void sendKafka(String topic,String messageVo) {
		try {
			Properties props = new Properties();
			props.put("bootstrap.servers", "47.106.189.255:9092");
			props.put("acks", "all");
			props.put("retries", 0);
			props.put("batch.size", 16384);
			props.put("linger.ms", 1);
			props.put("buffer.memory", 33554432);
			props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
			props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

			Producer<String, String> producer = new KafkaProducer<>(props);

			// Producer<String, String> producer = kafkaCommon.getKafkaProducer();
			producer.send(new ProducerRecord<>(topic, messageVo));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
