package com.iot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class IotBaseServiceApp {

	public static void main(String[] args) {
		SpringApplication.run(IotBaseServiceApp.class, args);
	}
}
