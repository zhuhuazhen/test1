package com.hzyw.iot.test;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class KafkaProducerExample {
	 public static void main(String[] args) {
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
	        producer.send(new ProducerRecord<>("test", "group123"));
	       /* for(int i = 0; i < 10; i++)
	            producer.send(new ProducerRecord<>("topic1", "demo", "helloWorld"));*/

	        producer.close();
	    }
	 
	 
	 
	 public Properties getProperties() {
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
	        producer.send(new ProducerRecord<>("test", "group123"));
	       /* for(int i = 0; i < 10; i++)
	            producer.send(new ProducerRecord<>("topic1", "demo", "helloWorld"));*/

	        producer.close();
			return props;
	}
}
