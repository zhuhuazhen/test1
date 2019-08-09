package com.hzyw.iot.mqtt.callback;

import org.springframework.beans.factory.annotation.Autowired;

//import com.hzyw.iot.redis.JedisPoolUtils;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

/**
 * 处理消息
 */
public class Handler {

	/**
	 * 处理逻辑消息
	 */
	public void handlerMessages(String topic, String message) {
		JSONObject jsonObject = JSONUtil.parseObj(message);
		JSONObject data = JSONUtil.parseObj(jsonObject.get("data"));
		// 判断type类型
		switch (jsonObject.get("type").toString()) {
		case "serviceOnOff":// 是否在线
			if (data.get("status").equals("online")) {
				//JedisPoolUtils.setPool(data.get("serviceid").toString(), "online");
			} else {
				//JedisPoolUtils.setPool(data.get("serviceid").toString(), "offline");
			}
			break;
		}
		//System.out.println("在线离线状态:" + JedisPoolUtils.getPool("adapter01"));

	}

}
