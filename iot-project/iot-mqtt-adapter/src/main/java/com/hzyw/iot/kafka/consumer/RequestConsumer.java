package com.hzyw.iot.kafka.consumer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.stereotype.Component;

import com.hzyw.iot.kafka.KafkaCommon;

/**
 * 1，从KAFKA获取下发数据（实时获取）
 * 2, 判断服务是否在线
 * 3，判断设备是否已上线
 * 4，发送到MQTT服务器，关闭mqtt连接
 * 5，如果存在异常，检查看KAFKA是否已经提交offset
 */
@Component
public class RequestConsumer {
	 //参考 KafkaControllerTest  怎么做消费的
	
	@Autowired
	private KafkaCommon kafkaCommon;
	/**
	 * 获取kafka的数据，并处理下发Mqtt
	 */
	public void kafkaSendMqtt() {
		//制定消費隊列和消費組
   	 	String topic = "topic_wan";
        String groupId = "test";
        try {
		KafkaConsumer<String, String> consumer = kafkaCommon.getKafkaConsumer(topic, groupId);
        consumer.subscribe(Arrays.asList("topic_wan"));
        }catch(Exception e){
       	 e.printStackTrace();
        }
	}
	
	
	@Bean
	public KafkaListenerContainerFactory<?> batchFactory() {
	  ConcurrentKafkaListenerContainerFactory<String, String> factory = new 
	        ConcurrentKafkaListenerContainerFactory<>();
	  factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(consumerConfigs()));
	  factory.setBatchListener(true); // 开启批量监听
	  return factory;
	}
	
	@Bean
	public Map<String, Object> consumerConfigs() {
	  Map<String, Object> props = new HashMap<>();
	  props.put(ConsumerConfig.GROUP_ID_CONFIG, "testId");
	  props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
	  props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "47.106.189.255:9092");
	  props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 100); //设置每次接收Message的数量
	  props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
	  props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 120000);
	  props.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, 180000);
	  props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
	  props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
	  return props;
	}
	@KafkaListener(topics = "wan", id = "consumer", containerFactory = "batchFactory")
	public void listen(List<ConsumerRecord<?, ?>> list) {
	  List<String> messages = new ArrayList<>();
	  for (ConsumerRecord<?, ?> record : list) {
		System.out.printf("~~~~~~~~~offset = %d, key = %s, value = %s\n", record.offset(), record.key(), record.value());
	    Optional<?> kafkaMessage = Optional.ofNullable(record.value());
	    // 获取消息
	    kafkaMessage.ifPresent(o -> messages.add(o.toString()));
	  }
	 /* if (messages.size() > 0) {
	    // 更新索引
	    updateES(messages);
	  }*/
	}
	
	
	/*@KafkaListener(topics = {"test"}, containerFactory = "consumerConfigs")
	    public void listen(ConsumerRecord<?, ?> record){
		System.out.printf("offset = %d, key = %s, value = %s\n", record.offset(), record.key(), record.value());
		Optional<?> kafkaMessage = Optional.ofNullable(record.value());

		if (kafkaMessage.isPresent()) {

			Object message = kafkaMessage.get();
			System.out.println("---->" + record);
			System.out.println("---->" + message);

		}
		

	    }*/
	 
	 
	
	
	
}
