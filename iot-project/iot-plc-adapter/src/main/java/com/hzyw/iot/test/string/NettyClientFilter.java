package com.hzyw.iot.test.string;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class NettyClientFilter extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline ph = ch.pipeline();
        /*
         * 解码和编码，应和服务端一致
         * */
        ph.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
        //ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(1024,5,2,10,0));  LengthFieldBasedFrameDecoder 自定义报文解码
        //maxFrameLength：单个包最大的长度
        //lengthFieldOffset：表示数据长度字段开始的偏移量  
        //lengthAdjustment：这里取值为10=7(系统时间) + 1（校验码）+ 2 (包尾)
        ph.addLast("decoder", new StringDecoder());
        ph.addLast("encoder", new StringEncoder());
     //   ph.addLast("handler", new NettyClientHandler()); //客户端的逻辑
    }
}
