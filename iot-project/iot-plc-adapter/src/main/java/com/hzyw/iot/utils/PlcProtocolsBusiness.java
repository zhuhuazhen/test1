package com.hzyw.iot.utils;

import com.hzyw.iot.vo.dataaccess.DevInfoDataVO;
import com.hzyw.iot.vo.dataaccess.DevOnOffline;
import com.hzyw.iot.vo.dataaccess.MessageVO;
import com.hzyw.iot.vo.dataaccess.RequestDataVO;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hzyw.iot.netty.channelhandler.ChannelManagerHandler;
import com.hzyw.iot.util.ByteUtils;
import com.hzyw.iot.util.constant.ConverUtil;
import com.hzyw.iot.vo.dc.GlobalInfo;
import com.hzyw.iot.vo.dc.ModbusInfo;

import cn.hutool.core.convert.Convert;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;

/**
 * 初始化PLC配置： 
 * 1 集中器配置：向集中器写入经纬度信息 
 * 2 设置集中器时间：手动同步集中器时间 
 * 3 清除节点 
 * 4 删除flash节点 
 * 5 开始组网（等待节点数*20秒） 
 * 6 查询组网(当全部节点都组网成功) (略) 
 * 7 停止组网
 *  8 存储节点 
 *  9 下发节点 
 *  10 配置节点（等待节点数*30秒） 
 *  11 查询节点  (略)
 *  
 * @author Administrator
 *
 */
public class PlcProtocolsBusiness {
	private static final Logger logger = LoggerFactory.getLogger(PlcProtocolsBusiness.class);
	public static final Map<String, String> gloable_dev_status = new HashMap<String, String>(); //PLC设备是否上线或离线  

	public static String getPlcSnByPlcID(String plc_id) throws Exception {
		return IotInfoConstant.plc_relation_deviceToSn.get(plc_id);
	}
	
	public static String getPortByPlcID(String plc_id) throws Exception {
		return "12345"; //后面从数据里面查找即可
	}
	 
	public static String getPlcNodeSnByPlcNodeID(String plc_node_id) throws Exception {
		return IotInfoConstant.plc_relation_deviceToSn.get(plc_node_id);
	}
	
	public static void protocals_process(String requestMessageVOJSON){
		final ModbusInfo modbusInfo = new ModbusInfo();
		try {
			 
			logger.info("\n mssageVO: ({}) ", requestMessageVOJSON );
			
			JSONObject jsonObject = JSON.parseObject(requestMessageVOJSON);//消息包
			String jsonStr=((JSONObject) jsonObject.get("data")).toJSONString();
			RequestDataVO requestVO = JSONObject.parseObject(jsonStr,RequestDataVO.class);
            Map<String,Object> methodMap =(Map<String, Object>)requestVO.getMethods().get(0);   
            List<Map<String,Object>> inList=(List<Map<String,Object>>) methodMap.get("in"); //上报参数属性集合
            String method = methodMap.get("method").toString(); //方法(物联网平台侧)            
            String plc_id = jsonObject.getString("gwId");       //集中器id(物联网平台侧)
            String plc_sn = getPlcSnByPlcID(plc_id);//集中器地址(边缘设备侧)
            
            if(GlobalInfo.SN_CHANNEL_INFO_MAP.get(getPortByPlcID(plc_id)+plc_sn) == null
            		|| GlobalInfo.SN_CHANNEL_INFO_MAP.get(getPortByPlcID(plc_id)+plc_sn).getChannel() == null){
            	logger.warn("request请求,无法下发，没有找到 sn:{} 下的channel !!!", plc_sn);
            	return;
            }
            Channel channel = GlobalInfo.SN_CHANNEL_INFO_MAP.get(getPortByPlcID(plc_id)+plc_sn).getChannel();//获取要下发的sn设备对应的channel
            String cmdCode = IotInfoConstant.allDevInfo.get(PlcProtocolsUtils.getPort(channel)).get(plc_sn+"_method").get(method);
            //根据方法找到相应的指令码，调用相应指令码对应的协议报文API
            if("42".equals(cmdCode)){
            	protocols_API_42(channel, jsonObject, requestVO, plc_sn);
            }
            //其他报文下发...
             
		} catch (Exception e) {
			logger.error(">>>request(" + modbusInfo.getAddress_str() + ")request下发,,cmdCode="+modbusInfo.getCmdCode_str()
        				+",ccode="+modbusInfo.getcCode_str()+",exception1！", e);
		}
	}
	
	 
	/**
	 * 
	 * 支持开灯，关灯，调灯管等操作
	 * 
	 * @param ctx
	 * @param jsonObject
	 * @param requestVO
	 * @return
	 */
	public static boolean  protocols_API_42(Channel channel, JSONObject jsonObject,RequestDataVO requestVO, String plc_SN) {
		boolean excuSeccess = true;
		final ModbusInfo modbusInfo = new ModbusInfo();
		try {
			logger.info("=====>>>protocols_42    开灯/关灯...===============");
            Map<String,Object> methodMap =(Map<String, Object>)requestVO.getMethods().get(0);   
            List<Map<String,Object>> inList=(List<Map<String,Object>>) methodMap.get("in"); //上报参数属性集合
            //String method = methodMap.get("method").toString(); //方法
            int plc_node_a_brightness = (Integer)inList.get(0).get(IotInfoConstant.dev_plc_node_a_brightness); //获取调光值  0~200
            String dev_plc_node_type = (String)inList.get(0).get(IotInfoConstant.dev_plc_node_type);           //A,B,AB控制
            String dev_plc_node_cCode = (String)inList.get(0).get(IotInfoConstant.dev_plc_node_cCode);         //控制码    下发下来就是16进制字符
            int plc_node_a_onoff = (Integer)inList.get(0).get(IotInfoConstant.dev_plc_node_a_onoff);          //开关  0-关 1-开
            String plc_node_id = requestVO.getId();
            
            if(plc_node_a_onoff == 0){
            	plc_node_a_brightness = 0;
            }
            if(plc_node_a_onoff == 1 && plc_node_a_brightness == 0){
            	plc_node_a_brightness =100;
            }
             
            String plc_node_SN = getPlcNodeSnByPlcNodeID(plc_node_id);//默认是根据节点ID开灯
            modbusInfo.setAddress(ConverUtil.hexStrToByteArr(plc_SN));
			modbusInfo.setcCode(ConverUtil.hexStrToByteArr(dev_plc_node_cCode));  //01H：点控制。02H：组控制。03H：广播控制。
			if("02".equals(dev_plc_node_cCode)){
				plc_node_SN = "000000000001"; //组号，目前入参不支持，如果需要支持按组调灯，需要增加此入参的输入哦
			}
			if("03".equals(dev_plc_node_cCode)){
				plc_node_SN = "000000000000";
			}
			modbusInfo.setCmdCode(ConverUtil.hexStrToByteArr("42")); 
            
			// 构造PDT 8个字节 
			byte[] temp1 = new byte[6]; //ID
			temp1 = ConverUtil.hexStrToByteArr(plc_node_SN); 
			byte[] temp2 = new byte[1]; //A/B   01H：控制A灯。(控制双灯控制器的A灯) 02H：控制B灯。(控制双灯控制器的B灯) 03H：同时控制A灯和B灯
			temp2 = ConverUtil.hexStrToByteArr(dev_plc_node_type); 
			byte[] temp3 = new byte[1]; 
			temp3 = ByteUtils.varIntToByteArray(plc_node_a_brightness*2); //DIM 调光值(0~200) ，页面上调光值尺度是0~100
			modbusInfo.setPdt(PlcProtocolsUtils.getPdt(temp1,temp2,temp3));
			ctxWriteAndFlush(channel,modbusInfo,"调灯光" ,0);
		} catch (Exception e) {
			logger.error(">>>request(" + modbusInfo.getAddress_str() + ")请求设备,,cmdCode="+modbusInfo.getCmdCode_str()
        				+",ccode="+modbusInfo.getcCode_str()+",exception1！", e);
			excuSeccess = false;
		}
		return excuSeccess;
	}
	
	public static void ctxWriteAndFlush(Channel channel,  ModbusInfo modbusInfo,String logmsg,int step) throws Exception{
		channel.writeAndFlush(modbusInfo.getNewFullDataWithByteBuf()).addListener((ChannelFutureListener) future -> {  
            if (future.isSuccess()) {
        		logger.info(">>>request ("+modbusInfo.getAddress_str()+")请求设备成功/"+logmsg+"！,cmdCode="+modbusInfo.getCmdCode_str()
        				+",ccode="+modbusInfo.getcCode_str()+",byteMsg=" + ConverUtil.convertByteToHexString(modbusInfo.getNewFullData()));
            } else {
            	logger.info(">>>request ("+modbusInfo.getAddress_str()+")请求设备失败/"+logmsg+"！,cmdCode="+modbusInfo.getCmdCode_str()
        				+",ccode="+modbusInfo.getcCode_str()+",byteMsg=" + ConverUtil.convertByteToHexString(modbusInfo.getNewFullData()));
            }
        });
	}
	 
}
