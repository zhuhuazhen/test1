package com.hzyw.iot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.hzyw.iot.utils.CRCUtils;
import com.hzyw.iot.utils.IotInfoConstant;

@Configuration
@PropertySource(value = { "classpath:initPlcData.properties" }, encoding = "utf-8")
public class InitPlcDataConfig {
	 
	@Value(value = "${iot.plc.initdata.plc}")
	private String  plcJson;
	
	@Value(value = "${iot.plc.initdata.node}")
	private String  plcNodeJson;
	 
	@Bean
	public String initDevData(){
		IotInfoConstant.plc_json = plcJson;
		IotInfoConstant.plc_node_json = plcNodeJson;
		IotInfoConstant.initData();
		return new String();
	}
	

}
