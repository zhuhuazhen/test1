package com.hzyw.iot.netty.channelhandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * 心跳检测 headler
 *
 */
public class HeartBeatHandler extends ChannelInboundHandlerAdapter {
    private int loss_connect_time = 0;
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        /*if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                loss_connect_time++;
                if (loss_connect_time > 2) {
                    ctx.channel().close();
                }
            } else {
                super.userEventTriggered(ctx, evt);
            }
        }*/
    	super.userEventTriggered(ctx, evt);  
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                loss_connect_time++;
                System.out.println("---------心跳----读操作--------loss_connect_time--" +loss_connect_time);
                if (loss_connect_time > 2) {
                    //ctx.channel().close();
                	System.out.println("---------心跳----读操作--------loss_connect_time>2分钟--" +loss_connect_time);
                }
            } else if (event.state().equals(IdleState.WRITER_IDLE)) {  
            	System.out.println("---------心跳----写操作----------" );
            	  
            } else if (event.state().equals(IdleState.ALL_IDLE)) {  
            	System.out.println("---------心跳----ALL_IDLE操作--------发送心跳....." );
                 //未进行读写  
                // 发送心跳消息  
               // MsgHandleService.getInstance().sendMsgUtil.sendHeartMessage(ctx);  
                  
            }
        }
       
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

}
