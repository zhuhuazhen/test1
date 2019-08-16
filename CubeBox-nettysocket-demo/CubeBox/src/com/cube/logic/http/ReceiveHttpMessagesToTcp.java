package com.cube.logic.http;

 
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.util.ReferenceCountUtil;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.cube.event.CubeMsg;
import com.cube.event.EventEnum;
import com.cube.event.ReplyEvent;
import com.cube.logic.DefaultHttpProcess;
import com.cube.server.CubeBootstrap;
 

/**
 * http接口--接收app server 发送过来的消息，转发给手表
 * @description GetLedStatus
 * @author tengyz
 * @version 0.1
 * @date 2014年8月16日
 */
public class ReceiveHttpMessagesToTcp extends DefaultHttpProcess {

	private static final Logger LOG = Logger.getLogger(ReceiveHttpMessagesToTcp.class);

    @Override
    protected void doExcute(ChannelHandlerContext ctx, FullHttpRequest req, FullHttpResponse resp, Map<String, List<String>> params, HttpPostRequestDecoder decoder) throws IOException {
    	  ByteBuf buf = null;
          ReplyEvent replyEvent = null;
          try {
            String SN = null;
            String CODE = null;
            if (method(req) == HttpMethod.GET) {
                SN = getParam("SN", params);
                CODE = getParam("CODE", params);
                LOG.info("http SN=:"+SN+" ,CODE=:"+CODE);
            }
            if (StringUtils.isBlank(SN)) {
                LOG.error("获取的设备号SN为空");
                resp.content().writeBytes("设备号SN不能为空".getBytes());
                resp.headers().set(CONTENT_TYPE, "text/html; charset=UTF-8");
                return;
            }
            CubeMsg cubeMsg = CubeMsg.buildMsg(SN);
            if (cubeMsg == null) {
                LOG.error("手表未连接服务器,SN=:"+SN);
                sendUnableConn(resp);
                return;
            }
            replyEvent=new ReplyEvent(SN);
            CubeBootstrap.processRunnable.putReply(replyEvent);
            cubeMsg.setType(EventEnum.TWO);
            cubeMsg.setData(SN.getBytes());//SN
            cubeMsg.setDataString(CODE);//指令
            CubeBootstrap.processRunnable.pushUpMsg(cubeMsg);
		        synchronized (replyEvent) {
		                try {
		                    if (replyEvent.getObj() == null) {
		                        replyEvent.wait(10000);
		                    }
		                    replyEvent = CubeBootstrap.processRunnable.getReply(replyEvent.getId());
		                    if (replyEvent == null || replyEvent.getObj() == null) {
		                        LOG.info("timeout SN:"+SN);
		                        sendTimeout(resp);
		                    } else {
		                        //状态返回处理
		                        String retData = (String) replyEvent.getObj();
		                        //返回http消息,根据手表返回的结果进行返回给http调用者
		                        resp.content().writeBytes(new String(retData.toString().getBytes(),"UTF-8").getBytes());
		                        resp.headers().set(CONTENT_TYPE, "text/html; charset=UTF-8");
		                    }
		                } catch (InterruptedException e) {
		                    LOG.error("等待结束SN=:"+ SN);
		                    sendTimeout(resp);
		                }
		            }
		          
		        } finally {
		            if (buf != null) {
		                ReferenceCountUtil.release(buf);
		            }
		            if (replyEvent != null) {
		                CubeBootstrap.processRunnable.removeReply(replyEvent.getId());
		            }
		        }
        }  
    


}
