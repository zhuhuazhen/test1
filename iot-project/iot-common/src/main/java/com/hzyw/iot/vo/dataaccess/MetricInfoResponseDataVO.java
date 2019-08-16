package com.hzyw.iot.vo.dataaccess;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class MetricInfoResponseDataVO extends DataVO implements Serializable {
	private static final long serialVersionUID = 4111731933287812345L;
	private String deviceId; // 设备ID
	private List<Map> attributers; // 详细属性

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public List<Map> getAttributers() {
		return attributers;
	}

	public void setAttributers(List<Map> attributers) {
		this.attributers = attributers;
	}
}
