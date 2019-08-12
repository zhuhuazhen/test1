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
import com.hzyw.iot.mqtt.PublishSample;
import com.hzyw.iot.service.RedisService;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

@RestController
public class KafkaControllerTest {

	@Autowired
	private KafkaCommon kafkaCommon;
	@Autowired
	private RedisService redisService;
	
	@RequestMapping(value = "/testconsumer", method = RequestMethod.GET)
	public void testConsumer1(String[] args) {
         try{
        	 //制定消費隊列和消費組
        	 String topic = "topic_wan";
             String groupId = "weiyi";
             //System.out.println("----kafkaCommon-----" +kafkaCommon);
             KafkaConsumer<String, String> consumer = kafkaCommon.getKafkaConsumer(topic, groupId);
             consumer.subscribe(Arrays.asList("topic_wan"));
             
             //redisService.set("status", "online");
             int p = 0;
             for(;;) {
             	 System.out.println("===========into==========");
                 ConsumerRecords<String, String> records = consumer.poll(100);
                 for (ConsumerRecord<String, String> record : records){
                 	p++;
                 	//判断有无数据，并处理
                 	if(ObjectUtil.isNotNull(record.value())) {
                 		//判断设备是否在线(redis)
                 		if(redisService.get("status").equals("online")) {
                 			PublishSample publish = new PublishSample();
                 			publish.Publish(topic, record.value());//发送数据到设备
                 			
                 		}else {
                 			System.out.println("设备不在线");
                 		}
                 		
                 		
                 		/*JSONObject jsonObject = JSONUtil.parseObj(record.value());
                 		jsonObject.get("methods");*/
                 		
                 		//System.out.printf("==========="+p+"=============>offset = %d, key = %s, value = %s\n", record.offset(), record.key(), record.value());
                 	}
                     
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