package com.theembers.iot.netty.channelhandler;

import com.theembers.iot.*;
import com.theembers.iot.enums.EMqExchange;
import com.theembers.iot.netty.processor.*;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * RTU 设备 解码
 *
 * @author TheEmbers Guo
 * @version 1.0
 * createTime 2018-10-19 15:59
 */
@Component
public class ProcessorHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessorHandler.class);
    private final IDataProcessor sysDataProcessor = new SysDataProcessor();
    //private final IDataProcessor simDataProcessor = new SimDataProcessor();
    private final IDataProcessor displayDataProcessor = new DisplayDataProcessor();
    private final IDataProcessor r485DataProcessor = new R485DataProcessor();


    public ProcessorHandler() {
        //sysDataProcessor.setNextProcessor(simDataProcessor);
        //simDataProcessor.setNextProcessor(r485DataProcessor);
        //r485DataProcessor.setNextProcessor(displayDataProcessor);
    	sysDataProcessor.setNextProcessor(r485DataProcessor); //把上面别人的流程注释掉 ，这里直接跳到R485DataProcessor即可
    	//传感器 Processor
    	//wiff  Processor
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof ByteBuf) {
            ByteBuf byteBuf = (ByteBuf) msg;
            RTUInfo rtuInfo = new RTUInfo(null);
            //sysDataProcessor.translate(ctx, byteBuf, rtuInfo);
            //rtuInfo {id, sn ,data<T>}   T可以适配任何设备模型，如有新的业务场景 抽象出的设备模型不一样 ，那这里就很有意义了哦
            // -- PLC接入 这里的T可以转化成  他对应的统一的数据模型
            // -- 如：{uuid系统唯一标识, 设备sn ,data<T>}   {msgid, 设备sn ,data<MessageVO>}
            r485DataProcessor.translate(ctx, byteBuf, rtuInfo);
            //定义这个是要查设备信息或从缓存里获取系统里面录入的设备信息，补全rtuInfo
            // --设备的数据结构：< id ,< 设备属性字段：设备属性对应值 >> ,如 < 设备sn , < monitorObject:TCL-400W-01>>   
            // --暂时只配置 ：集中器UUID,节点UUID,集中器ID,节点ID,所属组,设备类型码,设备sn(=集中器ID_节点ID)
            buildRtuInfo(ctx, rtuInfo);
            ctx.fireChannelRead(rtuInfo); //调用fireChannelRead方法时，调用该方法的context会从自己开始在链表中根据自己的next指针来寻找下一个注册（invoke）的handler去处理事件
        } else {
            ctx.fireChannelRead(msg);
        }

    }

    /**
     * rtuinfo{
     *   id
     *   sn
     *   data<item>
     * 统一补全设备的相关属性信息
     * 
     * 
     * @param ctx
     * @param rtuInfo
     */
    private void buildRtuInfo(ChannelHandlerContext ctx, RTUInfo rtuInfo) { //补全rtuInfo信息
        RTUChannelInfo rtuChannelInfo = GlobalInfo.CHANNEL_INFO_MAP.get(ctx.channel().id()); //全局 netty channel 管理器 map{channelId, channelinfo}

        IotInfo iotInfo = rtuChannelInfo.getIotInfo(); //设备信息？
        // 如果不存在物联网信息则使用sn号并添加未注册队列标记，否则使用物联网id
        rtuInfo.setSn(rtuChannelInfo.getSn());
        Map<String, String> iotInfoDataMap;
        if (iotInfo == null) { //在物联网平台，没有配置设备信息，或没有注册
            rtuInfo.setId(rtuChannelInfo.getSn());
            EMqExchange[] eMqExchanges = {EMqExchange.RTU_UNREGISTERED};
            rtuInfo.setMqExchange(eMqExchanges);// 指定此消息的类型  ，这里其实是个常量
            iotInfoDataMap = new HashMap<>();
        } else {
            rtuInfo.setId(iotInfo.getId());
            iotInfoDataMap = iotInfo.getData();
        }

        // 替换 rtuInfo 的 item id
        if (rtuInfo.getData() instanceof Collection) { //如果返回的是个集合
            List<ItemInfo> itemInfoList = (List) rtuInfo.getData(); 
            if (CollectionUtils.isEmpty(itemInfoList)) {
                return;
            }
            itemInfoList.forEach(item -> {
                String itemId = rtuInfo.getSn() + "-" + item.getId();//指标ID
                item.setId(iotInfoDataMap.getOrDefault(itemId, itemId));// 取不到值 则默认给一个值
            });
        } else if (rtuInfo.getData() instanceof ItemInfo) {//如果返回的是个具体指标
            ItemInfo itemInfo = ((ItemInfo) rtuInfo.getData());
            String itemId = rtuInfo.getSn() + "-" + itemInfo.getId();
            itemInfo.setId(iotInfoDataMap.getOrDefault(itemId, itemId));
        }
    }
}
