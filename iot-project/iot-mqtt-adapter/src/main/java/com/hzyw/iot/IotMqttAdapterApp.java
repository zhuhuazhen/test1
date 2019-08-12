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
		SpringApplication application = new SpringApplication(IotMqttAdapterApp.class);
		//application.addInitializers(new ApplicationStartedListener());
		SpringApplication.run(IotMqttAdapterApp.class, args);
	}
	/*@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(appController.class);
    }*/

}
