package test;

import org.apache.log4j.Logger;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;


//public class TcpClientHandler extends SimpleChannelInboundHandler<Object> {
public class TcpClientHandler extends ChannelInboundHandlerAdapter {
	
	private static final Logger logger = Logger.getLogger(TcpClientHandler.class);

	protected void channelRead0(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		//messageReceived����,��ƺܱ�Ť������һ���ڲ�����.
		System.out.println("client���յ�������ص���Ϣ:"+msg);
		
	}

   
}