package com.cube.logic.proc;

import org.apache.log4j.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
 

import com.cube.core.CubeRun;
import com.cube.core.conn.Connection;
import com.cube.core.conn.ConnectionManager;
import com.cube.event.CubeMsg;
import com.cube.event.EventEnum;
import com.cube.exception.IllegalDataException;
import com.cube.logic.Process;
import com.cube.utils.ByteBufUtils;
import com.cube.utils.HexByteToolUtil;

/**
 * app发给服务，服务再发给手表
 * @description
 * @author tengyz
 * @version 0.1
 * @date 2014年8月6日
 */
public class SendWatchMessage implements Process {
	private static final Logger LOG = Logger.getLogger(SendWatchMessage.class);
    
    /**
     * 要发送数据，必须对CubeMsg设置ctx和data , ctx 从ConnectionManager中获取, data必须包含replyid
     * ReplyEvent可以向ProcessRunnable注册 然后ReplyEvent.wait(timeout);
     * 在finally中删除ProcessRunnable的注册
     */
    public void excute(CubeMsg msg) throws IllegalDataException {
        String doing="error";
        Connection conn = ConnectionManager.getInstance().getConn(new String(msg.getData()));
        if(null!=conn){
        	if (conn.getCtx().channel().isWritable()) {
                LOG.info((new StringBuilder()).append("向手表发送数据=:").append(msg.getDataString()).toString());
                //转发给手表
                ByteBuf  buff=(ByteBuf) Unpooled.wrappedBuffer(HexByteToolUtil.hexStringToBytes(HexByteToolUtil.encode(msg.getDataString())));
            	conn.getCtx().writeAndFlush(buff);
            	
                doing="ok";
                msg.getCtx().pipeline().writeAndFlush(doing);//回复app发送情况
            } else {
                LOG.info("手表不可写，无法发送数据,关闭连接");
                msg.getCtx().pipeline().writeAndFlush(doing);
                conn.getCtx().pipeline().close();
            }
        }else{
        	 LOG.info("手表未连接，无法发送数据,关闭连接");
        	 msg.getCtx().pipeline().writeAndFlush(doing);//回复app发送情况
        	 msg.getCtx().pipeline().close();
        }
        
    }


    
}
