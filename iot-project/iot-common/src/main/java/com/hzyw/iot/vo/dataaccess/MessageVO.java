package com.hzyw.iot.vo.dataaccess;

import java.io.Serializable;

/**
 * 设备数据接入- 下发消息外层结构
 */
public class MessageVO<T> implements Serializable {
	private static final long serialVersionUID = 2097112223168452858L;
	private String type; // 消息类型
	private T data; // 消息体
	private Long seq; // 序号 相对于某个主题来说消息通信的一个序号
	private Long timestamp; // 13位时间戳
	private String msgId; // 消息唯一ID,全流程跟踪，要求设备端把此值返回

	public MessageVO() {
	}

	public  MessageVO(T t) {
		this.data = t;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public Long getSeq() {
		return seq;
	}

	public void setSeq(Long seq) {
		this.seq = seq;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

}
