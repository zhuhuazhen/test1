package com.hzyw.iot.test;

import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.context.annotation.Bean;

public class KafkaConsumerExample {
	public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "47.106.189.255:9092");
        props.put("group.id", "group123");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        //props.put("auto.offset.reset", "earliest");
        props.put("auto.offset.reset", "latest");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList("iot_topic_dataAcess_devInfoResponse"));
       
        //for(int i = 0; i < 10; i++) {
        for( ;  ;  ) {
            ConsumerRecords<String, String> records = consumer.poll(10);
            //if(true)throw new RuntimeException("xxxxxxxxxx");
            for (ConsumerRecord<String, String> record : records)
                System.out.printf("offset = %d, key = %s, value = %s\n", record.offset(), record.key(), record.value());
            try {
				Thread.currentThread().sleep(1000*5);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }
}
