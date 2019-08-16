package com.cube.event;


import java.io.Serializable;

import org.apache.log4j.Logger;

import com.cube.core.conn.Connection;
import com.cube.core.conn.ConnectionManager;
import io.netty.channel.ChannelHandlerContext;

public class CubeMsg implements Serializable {
 

	/**
	 * 实例化id
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOG = Logger.getLogger(CubeMsg.class);
	 
    private EventEnum type;
    private ChannelHandlerContext ctx;
    private byte[] data;
    private String dataString;
    private Object dataObject;
    
    public String toString() {  
        return "type:" + type + " ctx:" + ctx + " data:" + data+ " dataString:" + dataString+ " dataObject:" + dataObject;  
    }  

    public EventEnum getType() {
        return type;
    }

    public void setType(EventEnum type) {
        this.type = type;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public ChannelHandlerContext getCtx() {
        return ctx;
    }

    public void setCtx(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    /**
     * 获取CubeMsg并赋值ChannelHandlerContext
     * 返回null则为无效mac
     */
    public static CubeMsg buildMsg(String mac) {
        Connection conn = ConnectionManager.getInstance().getConn(mac);
        if (conn == null) {
            LOG.error("sn:对应的conn为空"+mac);
            return null;
        }
        CubeMsg cubeMsg = new CubeMsg();
        cubeMsg.setCtx(conn.getCtx());
        return cubeMsg;
    }

	public String getDataString() {
		return dataString;
	}

	public void setDataString(String dataString) {
		this.dataString = dataString;
	}

	public Object getDataObject() {
		return dataObject;
	}

	public void setDataObject(Object dataObject) {
		this.dataObject = dataObject;
	}

}
