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
	 
    private byte[] data;
     
 

}
