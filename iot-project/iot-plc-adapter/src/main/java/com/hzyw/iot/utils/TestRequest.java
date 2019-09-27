package com.hzyw.iot.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.alibaba.fastjson.JSON;
import com.hzyw.iot.util.constant.ProtocalAdapter;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import redis.clients.jedis.Jedis;

public class TestRequest {
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
	        inMap.put("level", 100);
	        inMap.put("ab", "03H");
	        inMap.put("code", "03H");
	        inMap.put("onoff", 1);
	        inList.add(inMap);
	        
	       Map<String,Object> tags =new HashMap<String,Object>();
	       tags.put("agreement", "plc");
	        
	        List listMethods = new ArrayList();
	        Map<String,Object> methods =new HashMap<String,Object>();
	        methods.put("method", "set_brightness");
	        methods.put("in", inList);
	        listMethods.add(methods);
	        setListMap.put("id", "1010-1f31e84812046d00-3001-ffff-8ea3");// 000002000533-1010-b6fdc8a25f45b30e-3001-ffff-b3af    00000200053a-1010-1f31e84812046d00-3001-ffff-8ea3   0000020004ee-1010-b6fdc8a25f45b30e-3001-ffff-b2af
	        setListMap.put("methods", listMethods);
	        
	      /*  List list = new ArrayList();
	        setListMap.put("attributer", list);
	        setListMap.put("definedAttributer", list);
	        setListMap.put("definedMethod", list);*/
	        setListMap.put("tags", tags);
	        
	        Map<String,Object> mapVo =new HashMap<String,Object>();
	        mapVo.put("type", "request");
	        mapVo.put("timestamp",1566205651);
	        mapVo.put("msgId", "1db179ce-c81e-4499-bff2-29e8a954af97");
	        mapVo.put("gwId", "1010-d8b38d288d431464-3001-ffff-36cf");//000000000100-1010-d8b38d288d431464-3001-ffff-36cf
	        mapVo.put("data", setListMap);
	        ProtocalAdapter protocalAdapter = new ProtocalAdapter();
	        JSONObject jsonObject = JSONUtil.parseObj(mapVo);
	        System.out.println(jsonObject);
			try {
				//String plcTest = protocalAdapter.messageRequest(JSON.parseObject(jsonObject.toString()));
				producer.send(new ProducerRecord<>("iot_topic_dataAcess_request", jsonObject.toString()));
			    producer.close();
				//System.out.println("111111111111111拼装好的指令:"+plcTest);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        /*JSONObject jsonObject = JSONUtil.parseObj(map);
	        System.out.println(jsonObject.toString());*/
		}
		
		
		/*public static void main(String[] args) {
		        Jedis jedis = new Jedis("47.106.189.255", 6379);
		             jedis.set("hello","javaRedis");
		        String hello = jedis.get("hello");
		        System.out.println(hello);
		             jedis.close();
		}*/
}
