package com.hzyw.iot.netty.channelhandler;

 import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.hzyw.iot.utils.IotInfoConstant;
import com.hzyw.iot.utils.PlcProtocolsUtils;
import com.hzyw.iot.vo.dataaccess.DevOnOffline;
import com.hzyw.iot.vo.dataaccess.MessageVO;
import com.hzyw.iot.vo.dc.GlobalInfo;

import cn.hutool.core.convert.Convert;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * 自定义心跳检测 headler
 *
 */
public class HeartBeatHandler extends ChannelInboundHandlerAdapter {
	
	private static final Logger logger = LoggerFactory.getLogger(HeartBeatHandler.class);
	
    private int loss_connect_counts = 0;
    
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
    	super.userEventTriggered(ctx, evt);  
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent)evt;
            if (event.state() == IdleState.READER_IDLE) {
            	loss_connect_counts++;
            	logger.info("---------心跳----读操作--------loss_connect_counts--" +loss_connect_counts);
                if (loss_connect_counts > 2) {
                	handlerBusiness(ctx);
                    ctx.channel().close();
                    logger.info("---------没有读取到心跳上报- ----loss_connect_count > 2次  关闭连接 !"  );
                }
            } else if (event.state().equals(IdleState.WRITER_IDLE)) {  
            	logger.info("---------心跳----写操作----------" );
            } else if (event.state().equals(IdleState.ALL_IDLE)) {  
            	logger.info("---------心跳----ALL_IDLE操作------" );
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    	logger.error("---------心跳HeartBeatHandler,异常 ！" ,cause);
        ctx.close();
    }
    
    /**
     * 1,设置全局里面此设备的在线状态为离线  
     * 2,上报一个 DEV离线的消息类型
     * 
     * @param ctx
     */
    private void handlerBusiness(ChannelHandlerContext ctx){
    	try{
    		
    		ChannelId channelId = ctx.channel().id();
        	InetSocketAddress insocket = (InetSocketAddress)ctx.channel().localAddress();
        	String plc_sn = GlobalInfo.CHANNEL_INFO_MAP.get(channelId).getSn();
    		PlcProtocolsUtils.gloable_dev_status.put(plc_sn + "_login", "0"); // 1 --上线   0--离线
    		
    		String devcdId = IotInfoConstant.allDevInfo.get(insocket.getPort()+"").get(plc_sn+"_defAttribute").get(IotInfoConstant.dev_plc_plc_id);
    		DevOnOffline devOnline = new DevOnOffline();
			devOnline.setId(devcdId); //=deviceId
			devOnline.setStatus("offline");
			Map<String,String> tags = new HashMap<String,String>();
			tags.put(IotInfoConstant.dev_plc_dataaccess_key, IotInfoConstant.dev_plc_dataaccess_value); //指定接入类型是PLC接入类型
			devOnline.setTags(tags); 
			
			//消息结构
			MessageVO messageVo = getMessageVO(devOnline,"devOffline",System.currentTimeMillis(),UUID.randomUUID().toString(),devcdId);
			//kafka处理
			//sendKafka(JSON.toJSONString(messageVo).toString(),applicationConfig.getDataAcessTopic());
    		
    	}catch(Exception e){
    		logger.error("---------心跳HeartBeatHandler,handlerBusiness处理异常 ！" ,e);
    	}
    }
    
	public <T> MessageVO<T>  getMessageVO(T data,String type,Object timestamp,String msgId,String Plcid) {
		//消息结构
		MessageVO<T> messageVo = new MessageVO<T>();
		//消息结构
		messageVo.setType(type);
		messageVo.setTimestamp(timestamp);//取当前时间戳即可
		messageVo.setMsgId(msgId);
		messageVo.setData(data);
		messageVo.setGwId(Plcid);
		return messageVo;
	}

}
