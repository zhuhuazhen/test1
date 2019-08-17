package com.hzyw.iot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hzyw.iot.adapter.event.EventUtil;
import com.hzyw.iot.adapter.event.IStartupEvent;
import com.hzyw.iot.adapter.event.ITestEvent;
import com.hzyw.iot.bean.OrderBean;
import com.hzyw.iot.mqttTest.PublishSample;
import com.hzyw.iot.service.OrderService;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

@RestController
public class TestEvenPublishController {
	/*@Autowired(required = false)
    OrderService orderService;*/
	
	/**
	 * 一键报警
	 */
	@RequestMapping(value = "/testEvenPublishController", method = RequestMethod.GET)
    public String sos() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("aaa", "xxxxxxxxxxxxxxxxxxxx");
		 
		EventUtil.dispatcher(ITestEvent.class,jsonObject);//另一个工程里面去做实现呗 ----经过验证 此地方的事件分发机制只适用在同一个工程里面，跨context就不行
		
        return "hello";
    }
	
	
}
