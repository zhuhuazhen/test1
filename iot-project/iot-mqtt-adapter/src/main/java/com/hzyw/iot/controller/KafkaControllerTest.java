package com.hzyw.iot.controller;

import java.util.Arrays;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.hzyw.iot.kafka.KafkaCommon;

@RestController
public class KafkaControllerTest {

	@Autowired
	private KafkaCommon kafkaCommon;
	 
	@RequestMapping(value = "/testconsumer", method = RequestMethod.GET)
	public void testConsumer1(String[] args) {
         try{
        	 //制定消費隊列和消費組
        	 String topic = "topic_zhu";
             String groupId = "group_zhu";
             //System.out.println("----kafkaCommon-----" +kafkaCommon);
             KafkaConsumer<String, String> consumer = kafkaCommon.getKafkaConsumer(topic, groupId);
             consumer.subscribe(Arrays.asList("topic_zhu"));
             
             int p = 0;
             for(;;) {
             	 System.out.println("===========into==========");
                 ConsumerRecords<String, String> records = consumer.poll(100);
                 for (ConsumerRecord<String, String> record : records){
                 	p++;
                     System.out.printf("==========="+p+"=============>offset = %d, key = %s, value = %s\n", record.offset(), record.key(), record.value());
                 }
                 try {
     				Thread.currentThread().sleep(1000*5);
     			} catch (InterruptedException e) {
     				// TODO Auto-generated catch block
     				e.printStackTrace();
     			}
             }
         }catch(Exception e){
        	 e.printStackTrace();
         }
       
    }
}