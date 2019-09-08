package com.hzyw.iot.adapter.plc.vo;


import java.io.Serializable;

import org.apache.log4j.Logger;

import io.netty.channel.ChannelHandlerContext;

public class CubeMsg implements Serializable {
 

	/**
	 * 实例化id
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOG = Logger.getLogger(CubeMsg.class);
 
    private int req;
    private int ack;
    private int step;
    private String msg;
	public int getReq() {
		return req;
	}
	public void setReq(int req) {
		this.req = req;
	}
	public int getAck() {
		return ack;
	}
	public void setAck(int ack) {
		this.ack = ack;
	}
	public int getStep() {
		return step;
	}
	public void setStep(int step) {
		this.step = step;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
     

    
     
 

}
