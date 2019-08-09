package com.hzyw.iot.vo.dataaccess;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 设备数据接入-下发消息
 */
@SuppressWarnings("rawtypes")
public class RequestDataVO implements Serializable {
  
	private static final long serialVersionUID = -1080455511252050149L;
	private String deviceId; // 设备ID
	private List<Map> methods; // 操作方法   [{method:方法名, in:[{字段：值，字段:值，...}] out:[返回字段，返回字段...]}]
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public List<Map> getMethods() {
		return methods;
	}
	public void setMethods(List<Map> methods) {
		this.methods = methods;
	}
	 
 

}
