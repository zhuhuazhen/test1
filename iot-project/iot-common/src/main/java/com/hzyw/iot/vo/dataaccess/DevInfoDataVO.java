package com.hzyw.iot.vo.dataaccess;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 设备数据接入-设备基本消息
 */
@SuppressWarnings("rawtypes")
public class DevInfoDataVO implements Serializable {
	private static final long serialVersionUID = 4111731933287831358L;
	private String deviceId; // 设备ID
	private String status; // 状态
	private List<Map> attributers; // 基本属性
	private List<Map> methods; // 操作方法
	private List<Map> definedAttributers; // 自定义属性
	private List<Map> definedMethods; // []
	private List<Map> signals; // [消息中断]
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<Map> getAttributers() {
		return attributers;
	}
	public void setAttributers(List<Map> attributers) {
		this.attributers = attributers;
	}
	public List<Map> getMethods() {
		return methods;
	}
	public void setMethods(List<Map> methods) {
		this.methods = methods;
	}
	public List<Map> getDefinedAttributers() {
		return definedAttributers;
	}
	public void setDefinedAttributers(List<Map> definedAttributers) {
		this.definedAttributers = definedAttributers;
	}
	public List<Map> getDefinedMethods() {
		return definedMethods;
	}
	public void setDefinedMethods(List<Map> definedMethods) {
		this.definedMethods = definedMethods;
	}
	public List<Map> getSignals() {
		return signals;
	}
	public void setSignals(List<Map> signals) {
		this.signals = signals;
	}

	 

}
