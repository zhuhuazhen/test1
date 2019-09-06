package com.hzyw.iot.netty.channelhandler;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.net.InetSocketAddress;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hzyw.iot.util.constant.IotInfoConstant;
import com.hzyw.iot.vo.dc.GlobalInfo;
import com.hzyw.iot.vo.dc.RTUChannelInfo;

/**
 * 链路管理 handler
 *
 * @author TheEmbers Guo
 * @version 1.0
 * createTime 2018-10-25 15:18
 */
@Sharable
public class ChannelManagerHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChannelManagerHandler.class);
    
    public ChannelManagerHandler(){}

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info("new channel coming! ----> {}", ctx.channel());
        ChannelId channelId = ctx.channel().id();
        RTUChannelInfo channelInfo = GlobalInfo.CHANNEL_INFO_MAP.getOrDefault(channelId, RTUChannelInfo.build("unknownSN", channelId));
        GlobalInfo.CHANNEL_INFO_MAP.put(channelId, channelInfo);//还不知道此channel对应到哪个SN上
        ctx.fireChannelRegistered();
    }

    /*@Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info("channel out! ----> {}", ctx.channel());
        ChannelId channelId = ctx.channel().id();
        RTUChannelInfo channelInfo = GlobalInfo.CHANNEL_INFO_MAP.remove(channelId);
        GlobalInfo.SN_CHANNEL_INFO_MAP.remove(channelInfo.getSn());
        LOGGER.info("remove channel: {}", channelInfo);
        ctx.fireChannelUnregistered();
    }*/

    /**
     * 补全 链路信息：根据 channelId 获取 channelInfo 并写入 sn 和 物联网信息
     *
     * @param ctx
     * @param sn
     * @return
     */
    public static void setRTUChannelInfo(ChannelHandlerContext ctx, String sn) {
        ChannelId channelId = ctx.channel().id();
        //IotInfo iot = GlobalInfo.iotMapper.get(sn);
        InetSocketAddress insocket = (InetSocketAddress)ctx.channel().localAddress();
        System.out.println("--------- channelId ="+channelId+"---/Port= "+insocket.getPort() + " /sn="+sn);
        Map<String, Map<String, String>> devinfo = IotInfoConstant.allDevInfo.get(insocket.getPort()+"");
        GlobalInfo.CHANNEL_INFO_MAP.get(channelId).setSn(sn).setChannel(ctx.channel());
        if(devinfo.get(sn) == null){
        	LOGGER.error("--------- channelId ="+channelId+"---/Port= "+insocket.getPort() + " /sn="+sn + "---设备登陆失败 ----请检查是否已经初始化此设备数据---");
        	return;
        }
        GlobalInfo.CHANNEL_INFO_MAP.get(channelId).setDevInfo(devinfo.get(sn));

        RTUChannelInfo channelInfo = GlobalInfo.SN_CHANNEL_INFO_MAP.getOrDefault(sn, RTUChannelInfo.build(sn, channelId));
        channelInfo.setChannel(ctx.channel());
        channelInfo.setDevInfo(devinfo.get(sn));
        GlobalInfo.SN_CHANNEL_INFO_MAP.put(sn, channelInfo);
        LOGGER.info("sn: {} in the house.", sn);
    }

    /**
     * 刷新 链路信息
     */
    public static void refreshRTUChannelInfo() {
        LOGGER.info("refresh GlobalInfo...");
        GlobalInfo.CHANNEL_INFO_MAP.forEach((channelId, channelInfo) -> {
            String sn = channelInfo.getSn();
            //channelInfo.setIotInfo(GlobalInfo.iotMapper.get(sn));
            InetSocketAddress insocket = (InetSocketAddress)channelInfo.getChannel().localAddress();
            channelInfo.setDevInfo(IotInfoConstant.allDevInfo.get(insocket.getPort()+"").get(sn));//设置设备信息
        });
        
        GlobalInfo.SN_CHANNEL_INFO_MAP.forEach((sn, channelInfo) ->{ 
        	InetSocketAddress insocket = (InetSocketAddress)channelInfo.getChannel().localAddress();
        	channelInfo.setDevInfo(IotInfoConstant.allDevInfo.get(insocket.getPort()+"").get(sn));//设置设备信息
        });
    }
}
