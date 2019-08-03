package com.hzyw.iot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hzyw.iot.bean.OrderBean;
import com.hzyw.iot.mqttTest.PublishSample;
import com.hzyw.iot.service.OrderService;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

@RestController
public class OrderController {
	/*@Autowired(required = false)
    OrderService orderService;*/
	
	/**
	 * 一键报警
	 */
	@RequestMapping(value = "/sos", method = RequestMethod.POST)
    public String sos(@RequestBody JSONObject json) {
		JSONObject jsonObject = JSONUtil.parseObj(json);
		PublishSample publish = new PublishSample();
		//publish.Publish(jsonObject.get("topic").toString(), jsonObject.toString());
		
		
		
		/*OrderBean order = new OrderBean();
		order.setDeviceId(jsonObject.get("deviceId").toString());
		order.setNamespace(jsonObject.get("namespace").toString());
		order.setOrder(jsonObject.get("order").toString());
		order.setObjectName(jsonObject.get("objectName").toString());
		order.setTopic(jsonObject.get("topic").toString());*/
        return "hello";
    }
	
	
}
