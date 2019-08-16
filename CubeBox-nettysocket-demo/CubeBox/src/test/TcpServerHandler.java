package test;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;

import org.apache.log4j.Logger;

import com.cube.event.CubeMsg;
import com.cube.event.EventEnum;
import com.cube.event.ReplyEvent;
import com.cube.server.CubeBootstrap;



public class TcpServerHandler extends SimpleChannelInboundHandler<Object> {

    private static final Logger LOG = Logger.getLogger(TcpServerHandler.class);

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
    	System.out.print("TcpServerHandler.................................channelRead0....................");
    	LOG.info("SERVER���յ���Ϣ:"+msg);
		ctx.channel().writeAndFlush("yes, server is accepted you ,nice !"+msg);
		
		   String SN="3G*1451562501*";
		   CubeMsg cubeMsg = CubeMsg.buildMsg(SN);
		   ByteBuf buf = null;
	       ReplyEvent replyEvent = null;
           replyEvent=new ReplyEvent(SN);
           CubeBootstrap.processRunnable.putReply(replyEvent);
           cubeMsg.setType(EventEnum.TWO);
           cubeMsg.setData(SN.getBytes());//SN
           cubeMsg.setDataString(msg.toString());//指令
           CubeBootstrap.processRunnable.pushUpMsg(cubeMsg);
		        synchronized (replyEvent) {
		                try {
		                    if (replyEvent.getObj() == null) {
		                        replyEvent.wait(10000);
		                    }
		                    replyEvent = CubeBootstrap.processRunnable.getReply(replyEvent.getId());
		                    if (replyEvent == null || replyEvent.getObj() == null) {
		                        LOG.info("timeout SN:"+SN);
		                      
		                    } else {
		                        //状态返回处理
		                        String retData = (String) replyEvent.getObj();
		                        //返回http消息,根据手表返回的结果进行返回给http调用者
		                        
		                    }
		                } catch (InterruptedException e) {
		                    LOG.info("等待结束SN=:"+ SN);
		                    
		                }
		            }
		        System.out.print("TcpServerHandler.................................channelRead0.........end...........");
		        } 
		             
		      
 

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,
            Throwable cause) throws Exception {
        LOG.warn("Unexpected exception from downstream.", cause);
        ctx.close();
    }
}