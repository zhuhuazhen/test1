package com.hzyw.iot.service.Impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hzyw.iot.kafka.DataSendDownConsumer;
import com.hzyw.iot.kafka.KafkaCommon;
import com.hzyw.iot.kafka.config.ApplicationConfig;
import com.hzyw.iot.service.GateWayService;


/**
 * 盒子网关服务
 *
 */
@Service
public class GateWayServiceImpl implements GateWayService {
	
	@Autowired
	private KafkaCommon kafkaCommon; //KAFKA工具类
	
	@Autowired
	private ApplicationConfig applicationConfig;//全局配置

	
	/* 
	 * 数据下发-消费KAFKA获取下发数据
	 */
	@Override
	public void dataSendDown() {
		//从KAFKA获取下发消息 DataSendDownConsumer
		new Thread(new DataSendDownConsumer(kafkaCommon,applicationConfig,this),"数据下发::消费KAFKA").start();
		
	}
	
	
	

}
