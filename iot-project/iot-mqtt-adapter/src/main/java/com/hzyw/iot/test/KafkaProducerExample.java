package com.hzyw.iot.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.alibaba.fastjson.JSON;
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
	        Map<String,Object> inMap =new HashMap<String,Object>();
	        //inMap.put("program", "pictrue_and_multy_text.vsn");
	        //inMap.put("src", "lan");
	        inMap.put("level", 100);
	        inList.add(inMap);
	        
	        
	        
	       Map<String,Object> tags =new HashMap<String,Object>();
	       tags.put("agreement", "plc");
	        
	        List listMethods = new ArrayList();
	        Map<String,Object> methods =new HashMap<String,Object>();
	        methods.put("method", "set_brightness");
	        methods.put("in", inList);
	        listMethods.add(methods);
	        setListMap.put("id", "1010-3f7b3eb6bffe6fb1-2009-ffff-be7");
	        setListMap.put("methods", listMethods);
	        
	        List list = new ArrayList();
	        setListMap.put("attributer", list);
	        setListMap.put("definedAttributer", list);
	        setListMap.put("definedMethod", list);
	        //setListMap.put("tags", tags);
	        
	        Map<String,Object> map =new HashMap<String,Object>();
	        map.put("type", "request");
	        map.put("timestamp",1566205651);
	        map.put("msgId", "1db179ce-c81e-4499-bff2-29e8a954af97");
	        map.put("gwId", "1000-f82d132f9bb018ca-2001-ffff-d28a");
	        map.put("data", setListMap);
	        
	       
	        JSONObject jsonObject = JSONUtil.parseObj(map);
	        System.out.println(jsonObject.toString());
	        producer.send(new ProducerRecord<>("iot_topic_dataAcess_request", jsonObject.toString()));

	        producer.close();
	    }
	 
	/* public static void main(String[] args) {
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
	        
	        List<String[]> pdt =new ArrayList<String[]>();
			String[] test= {"00","02","03","8c","af"};
			pdt.add(test);
	       
	        
	        Map<String,Object> setListMap =new HashMap<String,Object>();
	        List<Map> inList = new ArrayList<Map>();
	        Map<String,Object> inMap =new HashMap<String,Object>();
	        inMap.put("pdt", pdt);
	        inMap.put("code", "03H");
	        inList.add(inMap);
	        
	        
	        
	       Map<String,Object> tags =new HashMap<String,Object>();
	       tags.put("agreement", "plc");
	        
	        List listMethods = new ArrayList();
	        Map<String,Object> methods =new HashMap<String,Object>();
	        methods.put("method", "42H");
	        methods.put("in", inList);
	        listMethods.add(methods);
	        setListMap.put("id", "000000000001");
	        setListMap.put("methods", listMethods);
	        
	        setListMap.put("tags", tags);
	        
	        Map<String,Object> map =new HashMap<String,Object>();
	        map.put("type", "request");
	        map.put("timestamp",1566205651);
	        map.put("msgId", "1db179ce-c81e-4499-bff2-29e8a954af97");
	        map.put("gwId", "000000000001");
	        map.put("data", setListMap);
	        
	       
	        JSONObject jsonObject = JSONUtil.parseObj(map);
	        System.out.println(jsonObject.toString());
	        producer.send(new ProducerRecord<>("iot_topic_dataAcess_request", jsonObject.toString()));

	        producer.close();
	    }*/
	 
		/*public static void main(String[] args) {
			List<String[]>aa=new ArrayList<String[]>();
			String[] ah= {"qq","dsd","dfd","dsfd"};
			String[] ah1= {"qq","dsd","dfd","dsfd"};
			aa.add(ah);
			aa.add(ah1);
			System.out.println(JSON.toJSONString(aa).toString());
		}*/
}
