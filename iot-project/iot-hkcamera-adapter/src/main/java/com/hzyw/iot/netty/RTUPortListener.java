package com.hzyw.iot.netty;

import com.hzyw.iot.netty.channelhandler.ProcessorHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * RTU 接口监听器
 */
public class RTUPortListener extends PortListenerAbstract {
	//int port;
    public RTUPortListener(int port, EventLoopGroup bossGroup, EventLoopGroup workerGroup) {
    	
        super(port, bossGroup, workerGroup);
        //this.port = port;
        super.bind();
        
    }

    @Override
    ChannelHandler settingChannelInitializerHandler() {
        return new ChildChannelHandler(this.getPort());  
    }

    private class ChildChannelHandler extends BaseHandler {
    	int _port;
    	ChildChannelHandler(){}
    	ChildChannelHandler(int port){
    		this._port = port;
    	}
    	
        @Override
        ChannelPipeline extHandler(ChannelPipeline pipeline) {
        	//客户端每次连接都会进入注册handler
        	//获取接入类型,根据具体的协议规范来定义Decoder
        	if(_port == 12345){
        		System.out.println("---PLC("+_port+")---进入自定义解码器------第一连接进入----------------" + pipeline.channel().id());
        		pipeline.addLast(new LengthFieldBasedFrameDecoder(100, 9, 1, 2, 0));//根据数据帧的格式做拆包粘包解码
        		// 数据封装
                pipeline.addLast(new ProcessorHandler(_port));
        	}
        	if(_port == 5678){
        		System.out.println("---其他类型("+_port+")---进入自定义解码器------第一连接进入----------------" + pipeline.channel().id());
        		pipeline.addLast(new LengthFieldBasedFrameDecoder(100, 9, 1, 2, 0));//根据数据帧的格式做拆包粘包解码
        		pipeline.addLast(new ProcessorHandler(_port));
        	}
            return pipeline;
        }
    }

}
