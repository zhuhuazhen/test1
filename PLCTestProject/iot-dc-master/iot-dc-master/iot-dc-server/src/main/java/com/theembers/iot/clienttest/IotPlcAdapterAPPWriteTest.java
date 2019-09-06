package com.theembers.iot.clienttest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
//import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

import com.theembers.iot.netty.channelhandler.CommandHandler;


/**
 * 测试，下发消息入口
 *
 */

public class IotPlcAdapterAPPWriteTest {

	public static void main(String[] args) {
		String _16str = "68 00 00 00 00 00 01 68 00 02 70 03 46 16";
		CommandHandler.writeCommand("sn1", _16str, 2);  //2表示入参 是16进制字符串
		 
	}
	 

}
