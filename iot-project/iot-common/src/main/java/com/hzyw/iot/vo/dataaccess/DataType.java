package com.hzyw.iot.vo.dataaccess;

/**
 * 
 * 设备数据接入-消息类型
 *
 */
public enum DataType {
	ServiceOnline("serviceOnline"),   //平台上线
	ServiceOffline("serviceOffline"),   //平台下线
	DevLogin("devLogin"),   //DEV登陆认证
	DevOnline("devOnline"),    //DEV在线
	DevOffline("devOffline"),  //DEV离线
	Request("request"),     //下发请求
	Response("response"),   //上报下发请求结果
	DevInfoResponse("devInfoResponse"),    //属性上报
	MetricInfoResponse("metricInfoResponse"),  //设备状态数据上报
	DevSignlResponse("devSignlResponse");      //设备信号上报

	private String messageType;

	private DataType(String messageType) {
		this.messageType = messageType;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	

}
