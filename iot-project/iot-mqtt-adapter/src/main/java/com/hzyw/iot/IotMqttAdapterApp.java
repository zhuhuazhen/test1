package com.hzyw.iot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@EnableCaching  //开启缓存
public class IotMqttAdapterApp {

	public static void main(String[] args) {
		SpringApplication.run(IotMqttAdapterApp.class, args);
	}
}
