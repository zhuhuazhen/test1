package com.hzyw.iot.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.hzyw.iot.vo.dataaccess.RequestDataVO;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

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
	        
	        
	        
	       
	        
	        Map<String,Object> setListMap =new HashMap<String,Object>();
	        List<Map> inList = new ArrayList<Map>();
	        List outList = new ArrayList();
	        Map<String,Object> inMap =new HashMap<String,Object>();
	        /*inMap.put("program", "pictrue_and_multy_text.vsn");
	        inMap.put("src", "lan");*/
	        inMap.put("level", "100");
	        inList.add(inMap);
	        /*outList.add("test");
	        outList.add("test1");*/
	        
	        
	        Map<String,Object> tags =new HashMap<String,Object>();
	        tags.put("key", "value");
	        
	        List listMethods = new ArrayList();
	        Map<String,Object> methods =new HashMap<String,Object>();
	        methods.put("method", "set_brightness");
	        methods.put("in", inList);
	        methods.put("out", outList);
	        listMethods.add(methods);
	        setListMap.put("id", "0001-f82d132f9bb018ca-2001-ffff-70a0");
	        setListMap.put("methods", listMethods);
	        setListMap.put("tags", tags);
	        
	        Map<String,Object> map =new HashMap<String,Object>();
	        map.put("type", "request");
	        map.put("timestamp", "1566205651");
	        map.put("msgId", "bdde30e7-1248-410f-8084-c13b9162beee");
	        map.put("gwId", "0001-f82d132f9bb018ca-2001-ffff-d28a");
	        map.put("data", setListMap);
	        
	       
	        JSONObject jsonObject = JSONUtil.parseObj(map);
	        System.out.println(jsonObject.toString());
	        producer.send(new ProducerRecord<>("iot_topic_dataAcess_request", jsonObject.toString()));

	        producer.close();
	    }
}
