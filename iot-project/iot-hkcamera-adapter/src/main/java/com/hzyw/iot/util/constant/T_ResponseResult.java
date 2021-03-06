package com.hzyw.iot.util.constant;

//import com.hzyw.iot.utils.IotInfoConstant;
import com.hzyw.iot.utils.IotInfoConstant;
import com.hzyw.iot.vo.dataaccess.DataType;
import com.hzyw.iot.vo.dataaccess.MessageVO;
import io.netty.channel.ChannelHandlerContext;

import java.net.InetSocketAddress;
//import java.net.InetSocketAddress;

/**
 * PLC 响应 消息头
 */
public class T_ResponseResult {
    /**
     * PLC 上报数据 消息头
     * @param ctx Netty上下文
     * @param plc_sn 集中器地址 UID
     * @param type 消息类型【设备状态数据(metricInfoResponse), 设备信号数据(devSignalResponse)】
     * @param data 上报的消息体
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> MessageVO<T> getResponseVO(ChannelHandlerContext ctx, String plc_sn, String type, T data)throws Exception{
        String msgId="31a8c447-5079-4e91-a364-1769ac06fd5c";  //暂时固定值，后面考虑怎么生成获取消息ID  消息ID 请求那边生成的带的
        MessageVO<T> mesVO = new MessageVO<T>();
        DataType enumType = DataType.getByValue(type);
        System.out.println("============getResponseVO 方法， 消息type: "+type);
        String typee="";
        switch (enumType) {
            case MetricInfoResponse://设备状态数据上报
                typee=DataType.MetricInfoResponse.getMessageType();
                break;
            case DevSignalResponse://设备信号上报
                typee=DataType.DevSignalResponse.getMessageType();
                break;
        }
        if("".equals(typee)) throw new Exception("PLC响应 上报 消息类型 :"+type+", 没在配置文件中定义!");
        // 获取节点ID(设备ID)
        InetSocketAddress insocket = (InetSocketAddress) ctx.channel().localAddress();
        String devcdId = IotInfoConstant.allDevInfo.get((insocket.getPort()) + "")
                .get(plc_sn + "_defAttribute").get(IotInfoConstant.dev_plc_plc_id);

        mesVO.setType(typee);
        mesVO.setTimestamp(System.nanoTime());
        mesVO.setData(data);
        mesVO.setMsgId(msgId);
        mesVO.setGwId(devcdId);
        return mesVO;
    }
}
