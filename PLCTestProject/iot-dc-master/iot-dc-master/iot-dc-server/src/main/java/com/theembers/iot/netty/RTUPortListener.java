package com.theembers.iot.netty;

import com.theembers.iot.netty.channelhandler.CommandHandler;
import com.theembers.iot.netty.channelhandler.ProcessorHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * RTU 接口监听器
 *
 * @author TheEmbers Guo
 * @version 1.0
 * createTime 2018-09-30 2:29 PM
 */
public class RTUPortListener extends PortListenerAbstract {
    public RTUPortListener(int port, EventLoopGroup bossGroup, EventLoopGroup workerGroup) {
        super(port, bossGroup, workerGroup);
        super.bind();
    }

    @Override
    ChannelHandler settingChannelInitializerHandler() {
        return new ChildChannelHandler();
    }

    private class ChildChannelHandler extends BaseHandler {
        @Override
        ChannelPipeline extHandler(ChannelPipeline pipeline) {
            pipeline
                    //  数据拆帧 & 头信息过滤
            //lengthFieldOffset  2  表长度区域是从第几个字节开始（从1开始算）
            //lengthFieldLength  2  表长度区域所占字节个数
              //lengthAdjustment -->（lengthAdjustment + 数据所占字节个数 = 数据长度字段之后剩下包的字节数）   说简单点就是除了数据以外的其他报文码所占的总字节数（当然是从长度域后面的下一个字节开始计算咯）
              //initialBytesToStrip --> 从整个包第一个字节开始设置为0，这里从第4个字节开始，忽略掉了前面四个字节，这种场景应该是做数据混淆吧估计
                    .addLast(new LengthFieldBasedFrameDecoder(10240, 2, 2, -4, 4, true))//根据数据帧的格式做拆包粘包解码 ，跳过了包头部区域了哦
                    // 数据封装
                    .addLast(new ProcessorHandler()) //写了好多个PROCESS处理类  把解码的数据（有JSON串，有自定义协议的报文）做解码，然后转化成RTUInfo对象，然后继续到下一个handler..
                    // 指令下发
                    .addFirst(new CommandHandler())
            ;
            return pipeline;
        }
    }

}
