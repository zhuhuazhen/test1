package com.hzyw.iot;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.alibaba.fastjson.JSON;
import com.hzyw.iot.config.MapListPropertiesConfig;

@SpringBootApplication
public class IotMqttAdapterApp implements CommandLineRunner{
    @Autowired
	private MapListPropertiesConfig propertiesConfig;
  
   
	public static void main(String[] args) {
		SpringApplication.run(IotMqttAdapterApp.class, args);
	}
 
	@Override
	public void run(String... arg0) throws Exception {
		Map<String, String> map = propertiesConfig.getMap();
		System.out.println(JSON.toJSONString(map));
		Map<String, String> p = propertiesConfig.getPrison();
 		System.out.println(JSON.toJSONString(p));
		List<String> list = propertiesConfig.getList();
		System.out.println(JSON.toJSONString(list));
	} 
}