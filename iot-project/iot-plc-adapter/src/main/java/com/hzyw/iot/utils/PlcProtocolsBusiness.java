package com.hzyw.iot.utils;

import com.hzyw.iot.vo.ResponseCache;
import com.hzyw.iot.vo.dataaccess.DevInfoDataVO;
import com.hzyw.iot.vo.dataaccess.DevOnOffline;
import com.hzyw.iot.vo.dataaccess.DevSignlResponseDataVO;
import com.hzyw.iot.vo.dataaccess.MessageVO;
import com.hzyw.iot.vo.dataaccess.MetricInfoResponseDataVO;
import com.hzyw.iot.vo.dataaccess.RequestDataVO;
import com.hzyw.iot.vo.dataaccess.ResponseDataVO;
import com.hzyw.iot.vo.dataaccess.ResultMessageVO;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

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
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
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
		Map<String, String> plc_relation_deviceToSn = IotInfoConstant.plc_relation_deviceToSn;
		return plc_relation_deviceToSn.get(plc_node_id);
	}
	
	public static String getPlcIdByPlcSn(String plc_sn,String port) throws Exception {
		Map<String, Object> def_attributers = IotInfoConstant.allDevInfo.get(port)
				.get(plc_sn.toUpperCase() + "_defAttribute");
		String plc_id = def_attributers.get(IotInfoConstant.dev_plc_plc_id)!=null?(String)def_attributers.get(IotInfoConstant.dev_plc_plc_id):"";
		return plc_id;
	}
	
	public static String getPlcNodeIdByPlcNodeSn(String plc_node_sn,String port) throws Exception {
		Map<String, Object> def_attributers = IotInfoConstant.allDevInfo.get(port)
				.get(plc_node_sn.toUpperCase() + "_defAttribute");
		String plc_node_id =  def_attributers.get(IotInfoConstant.dev_plc_node_id)!=null?(String)def_attributers.get(IotInfoConstant.dev_plc_node_id):"";
		return plc_node_id;
	}
	
	
	public static JSONObject protocals_process(String requestMessageVOJSON){
		final ModbusInfo modbusInfo = new ModbusInfo();
		JSONObject result = new JSONObject();
		String plc_id="",plc_sn="",cmdCode="",msgId="";
		try {
			logger.info("\n mssageVO: ({}) ", requestMessageVOJSON );
			JSONObject jsonObject = JSON.parseObject(requestMessageVOJSON);//消息包
			String jsonStr=((JSONObject) jsonObject.get("data")).toJSONString();
			RequestDataVO requestVO = JSONObject.parseObject(jsonStr,RequestDataVO.class);
            Map<String,Object> methodMap =(Map<String, Object>)requestVO.getMethods().get(0);   
            //List<Map<String,Object>> inList=(List<Map<String,Object>>) methodMap.get("in"); //上报参数属性集合
            String method = methodMap.get("method").toString(); //方法(物联网平台侧)            
            plc_id = jsonObject.getString("gwId");       //集中器id(物联网平台侧)
            plc_sn = getPlcSnByPlcID(plc_id);            //集中器地址(边缘设备侧)
            msgId = jsonObject.getString("msgId");
            
            if(GlobalInfo.SN_CHANNEL_INFO_MAP.get(getPortByPlcID(plc_id)+plc_sn) == null
            		|| GlobalInfo.SN_CHANNEL_INFO_MAP.get(getPortByPlcID(plc_id) + plc_sn).getChannel() == null){
            	logger.warn("request请求,无法下发，没有找到 sn:{} 下的channel/请检查此PLC是否已经连接并登陆 !!!", plc_sn);
            	result.put("msg", "fail!");
            	result.put("data", "request请求,无法下发，没有找到 sn:{} 下的channel/请检查此PLC是否已经连接并登陆 !!! sn/" + plc_sn);
            	return result;
            }
            Channel channel = GlobalInfo.SN_CHANNEL_INFO_MAP.get(getPortByPlcID(plc_id)+plc_sn).getChannel();//获取要下发的sn设备对应的channel
            cmdCode = (String)IotInfoConstant.allDevInfo.get(PlcProtocolsUtils.getPort(channel)).get(plc_sn+"_method").get(method);
            //根据方法找到相应的指令码，调用相应指令码对应的协议报文API
            if("42".equals(cmdCode)){
            	protocols_API_42_Request(channel, jsonObject, requestVO, plc_sn);
            }else if("9a".equals(cmdCode)){//查询组网个数
            	protocols_API_9a_Request(channel, jsonObject, requestVO, plc_sn,result);
            }else if("97".equals(cmdCode)){//查询节点列表
            	protocols_API_97_Request(channel, jsonObject, requestVO, plc_sn,result);
            }else{
            	logger.info("----------此指令协议未实现 !------------" );
            }
            //其他报文下发...
             
		} catch (Exception e) {
			logger.error(">>>request(" + plc_sn + "/"+msgId+")request下发,,cmdCode="+cmdCode +",exception1！", e);
		}
		return result;
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
	public static boolean  protocols_API_42_Request(Channel channel, JSONObject jsonObject,RequestDataVO requestVO, String plc_SN) {
		boolean excuSeccess = true;
		final ModbusInfo modbusInfo = new ModbusInfo();
		try {
			logger.info("=====>>>protocols_42    开灯/关灯...===============");
            Map<String,Object> methodMap =(Map<String, Object>)requestVO.getMethods().get(0);   
            List<Map<String,Object>> inList=(List<Map<String,Object>>) methodMap.get("in"); //上报参数属性集合
            String method = methodMap.get("method").toString();
            int plc_node_a_brightness = (Integer)inList.get(0).get(IotInfoConstant.dev_plc_node_a_brightness); //获取调光值  0~200
            String dev_plc_node_type = (String)inList.get(0).get(IotInfoConstant.dev_plc_node_type);           //A,B,AB控制
            String dev_plc_node_cCode = (String)inList.get(0).get(IotInfoConstant.dev_plc_node_cCode);         //控制码    下发下来就是16进制字符
            int plc_node_a_onoff = (Integer)inList.get(0).get(IotInfoConstant.dev_plc_node_a_onoff);           //开关  0-关 1-开
            String plc_node_id = requestVO.getId();
            
            if(plc_node_a_onoff == 0){
            	plc_node_a_brightness = 0;
            }
            if(plc_node_a_onoff == 1 ){
            	if(plc_node_a_brightness == 0){//只要是开，没有指定灯光或指定为0则
            		plc_node_a_brightness = 100;
            	}
            }
             
            String plc_node_SN = getPlcNodeSnByPlcNodeID(plc_node_id);//默认是根据节点ID开灯
            modbusInfo.setAddress(ConverUtil.hexStrToByteArr(plc_SN));
			modbusInfo.setcCode(ConverUtil.hexStrToByteArr(dev_plc_node_cCode));  //01H：点控制。02H：组控制。03H：广播控制。
			if("02".equals(dev_plc_node_cCode)){
				plc_node_SN = "000000000001"; //组号，目前入参不支持，如果需要支持按组调灯，需要增加此入参的输入哦
			}
			if("03".equals(dev_plc_node_cCode)){//如果是广播自动设置节点ID为0
				plc_node_SN = "000000000000";
			}
			modbusInfo.setCmdCode(ConverUtil.hexStrToByteArr("42")); 
            
			// 构造PDT 8个字节 
			byte[] temp1 = new byte[6]; //ID
			temp1 = ConverUtil.hexStrToByteArr(plc_node_SN); 
			byte[] temp2 = new byte[1]; //A/B   01H：控制A灯。(控制双灯控制器的A灯) 02H：控制B灯。(控制双灯控制器的B灯) 03H：同时控制A灯和B灯
			temp2 = ConverUtil.hexStrToByteArr(dev_plc_node_type); 
			byte[] temp3 = new byte[1]; 
			//temp3 = ByteUtils.intToByteArray(plc_node_a_brightness*2); //DIM 调光值(0~200) ，页面上调光值尺度是0~100   ，用此方法也可以，但要默认取数组里的最后一个字节
			temp3[1] = (byte)(((plc_node_a_brightness*2)) & 0xFF); //取第一个低位  
			modbusInfo.setPdt(PlcProtocolsUtils.getPdt(temp1,temp2,temp3));
			ctxWriteAndFlush_byResponse(channel,modbusInfo,"调灯光" , jsonObject.getString("msgId"),jsonObject.getString("gwId"),plc_node_id,method); 
		} catch (Exception e) {
			logger.error(">>>request(" + modbusInfo.getAddress_str() + ")请求设备,,cmdCode="+modbusInfo.getCmdCode_str()
        				+",ccode="+modbusInfo.getcCode_str()+",exception1！", e);
			excuSeccess = false;
		}
		return excuSeccess;
	}
	
	public static boolean  protocols_API_9a_Request(Channel channel, JSONObject jsonObject,RequestDataVO requestVO, String plc_SN,JSONObject result) {
		boolean excuSeccess = true;
		final ModbusInfo modbusInfo = new ModbusInfo();
		try {
			logger.info("=====>>>protocols_9a    查询组网...===============");
			//不需要入参PDT，只需要提供SN地址即可
            Map<String,Object> methodMap =(Map<String, Object>)requestVO.getMethods().get(0);   
            modbusInfo.setAddress(ConverUtil.hexStrToByteArr(plc_SN));
			modbusInfo.setcCode(ConverUtil.hexStrToByteArr("00"));  
			modbusInfo.setCmdCode(ConverUtil.hexStrToByteArr("9a")); 
			// 构造PDT 
			byte[] temp1 = new byte[0];
			modbusInfo.setPdt(temp1);
			ctxWriteAndFlush(channel,modbusInfo,"查询组网" ,0);
			result.put("msg", "seccess");
			result.put("data", "执行指令,请求发送完毕 ");
		} catch (Exception e) {
			logger.error(">>>request(" + modbusInfo.getAddress_str() + ")请求设备,,cmdCode=" + modbusInfo.getCmdCode_str()
        				+",ccode=" + modbusInfo.getcCode_str() + ",exception1！", e);
			excuSeccess = false;
			result.put("msg", "fail");
			result.put("data", e.getMessage());
		}
		return excuSeccess;
	}
	public static boolean  protocols_API_97_Request(Channel channel, JSONObject jsonObject,RequestDataVO requestVO, String plc_SN,JSONObject result) {
		boolean excuSeccess = true;
		final ModbusInfo modbusInfo = new ModbusInfo();
		try {
			logger.info("=====>>>protocols_97    查询节点列表...===============");
			//不需要入参PDT，只需要提供SN地址即可
            Map<String,Object> methodMap =(Map<String, Object>)requestVO.getMethods().get(0);   
            modbusInfo.setAddress(ConverUtil.hexStrToByteArr(plc_SN));
			modbusInfo.setcCode(ConverUtil.hexStrToByteArr("03"));  
			modbusInfo.setCmdCode(ConverUtil.hexStrToByteArr("97")); 
			// 构造PDT 
			byte[] temp1 = new byte[0];
			modbusInfo.setPdt(temp1);
			ctxWriteAndFlush(channel,modbusInfo,"查询节点列表" ,0);
			result.put("msg", "seccess");
			result.put("data", "执行指令,请求发送完毕 ");
		} catch (Exception e) {
			logger.error(">>>request(" + modbusInfo.getAddress_str() + ")请求设备,,cmdCode=" + modbusInfo.getCmdCode_str()
        				+",ccode=" + modbusInfo.getcCode_str() + ",exception1！", e);
			excuSeccess = false;
			result.put("msg", "fail");
			result.put("data", e.getMessage());
		}
		return excuSeccess;
	}
	
	 
	public static void ctxWriteAndFlush(Channel channel,  ModbusInfo modbusInfo,String logmsg,int step) throws Exception{
		channel.writeAndFlush(modbusInfo.getNewFullDataWithByteBuf()).addListener((ChannelFutureListener) future -> {  
            if (future.isSuccess()){
        		logger.info(">>>request ("+modbusInfo.getAddress_str()+")请求执行成功/"+logmsg+"! ,cmdCode="+modbusInfo.getCmdCode_str()
        				+",ccode="+modbusInfo.getcCode_str()+",byteMsg=" + ConverUtil.convertByteToHexString(modbusInfo.getNewFullData()));
            } else {
            	logger.info(">>>request ("+modbusInfo.getAddress_str()+")请求执行失败/"+logmsg+"! ,cmdCode="+modbusInfo.getCmdCode_str()
        				+",ccode="+modbusInfo.getcCode_str()+",byteMsg=" + ConverUtil.convertByteToHexString(modbusInfo.getNewFullData()));
            }
        });
	}
	
	
	/**
	 * 
	 * 带返回的控制指令下发(主要是用于request指令,存在response返回结果的消息类型)
	 * @param channel
	 * @param modbusInfo
	 * @param logmsg
	 * @param msgid  需要临时缓存的消息ID
	 * @param plcid  需要临时缓存的网关ID
	 * @param nodeid   需要临时缓存的灯具节点ID
	 * @throws Exception
	 */
	public static void ctxWriteAndFlush_byResponse(Channel channel,  ModbusInfo modbusInfo,String logmsg
			,String msgid,String plcid,String nodeid,String method) throws Exception{
		//定义一个消息缓存
		//如果MSGID已经存在，处理未超时：返回设备忙
		//如果MSGID已经存在，处理已超时：返回(上个msgid)设备忙,继续发送报文
		//如果MSGID不存在，添加一个消息到缓存，继续发送报文
		//--缓存hash结构：<sn+cmcCOde+cCode , msgId-gwid-nodeid-timestamp>    
		if(ResponseCacheUtils.plc_ResponseCache.get(modbusInfo.getCacheMsgId())!= null
				&& !ResponseCacheUtils.timeout(modbusInfo.getCacheMsgId())){
			//上一个请求处理未超时，当前请求直接返回
			logger.info(">>>request ("+modbusInfo.getAddress_str()+")请求指令已忽略(等待上一个消息处理返回中)/"+logmsg+"! ,cmdCode="+modbusInfo.getCmdCode_str()
					+",ccode="+modbusInfo.getcCode_str()+",byteMsg=" + ConverUtil.convertByteToHexString(modbusInfo.getNewFullData()));
			ResponseCacheUtils.sendkafka_by_ResponseCache(method, msgid, plcid, nodeid, ResponseCode.HZYW_PLC_RESPONSE_BUSINESS);
		}else{
			channel.writeAndFlush(modbusInfo.getNewFullDataWithByteBuf()).addListener((ChannelFutureListener) future -> {  
	            if (future.isSuccess()){
	        		logger.info(">>>request ("+modbusInfo.getAddress_str()+")请求执行成功/"+logmsg+"! ,cmdCode="+modbusInfo.getCmdCode_str()
	        				+",ccode="+modbusInfo.getcCode_str()+",byteMsg=" + ConverUtil.convertByteToHexString(modbusInfo.getNewFullData()));
	        		//发送成功，记录发送消息
	        		ResponseCacheUtils.addResponseCache(modbusInfo, method, msgid, plcid, nodeid);
	            } else {
	            	//如果调用失败 直接上报失败，
	            	logger.info(">>>request ("+modbusInfo.getAddress_str()+")请求执行失败/"+logmsg+"! ,cmdCode="+modbusInfo.getCmdCode_str()
	        				+",ccode="+modbusInfo.getcCode_str()+",byteMsg=" + ConverUtil.convertByteToHexString(modbusInfo.getNewFullData()));
	            	//执行发送指令失败直接上报失败
	            	ResponseCacheUtils.sendkafka_by_ResponseCache(method, msgid, plcid, nodeid, ResponseCode.HZYW_PLC_EXCU_ERROR);
	            }
	        });
		}
		
	}
	
	/**
	 * 
	 * true - 进入ModbusInfo模板处理报文
	 * false- 进入ProtocalAdapter.messageRespose模板处理报文
	 * @param ctx
	 * @param modbusInfo
	 * @return
	 */
	public static boolean transformTemplateSelect(ChannelHandlerContext ctx, ModbusInfo modbusInfo){
		boolean flag = false;
		String cCmdCode = modbusInfo.getCmdCode_str()!=null?modbusInfo.getCmdCode_str():"";
		if("f0".equals(modbusInfo.getCmdCode_str().toLowerCase()) || "f1".equals(modbusInfo.getCmdCode_str().trim().toLowerCase())){
			return true; //忽略f0登陆指令，f1心跳指令
		}
		if ("9a".equals(cCmdCode)  		//查询组网个数
			|| "42".equals(cCmdCode)
			|| "97".equals(cCmdCode)) {	//查询节点列表	
			 flag = true;
		}
		return flag;
	}
	
	/**
	 * 集中器 ==》主机
	 * 设备登陆，并配置成功后，所有涉及的上行方向的报文都可以在这里解析
	 * 主要包括： 
	 *     1,请求下发request  --> 请求下发返回结果response
	 *     2,状态数据上报
	 *     3,信号数据上报
	 * 
	 * @param ctx
	 * @param modbusInfo
	 */
	public static void protocals_process_Response(ChannelHandlerContext ctx, ModbusInfo modbusInfo){
		String cCode = modbusInfo.getcCode_str()!=null?modbusInfo.getcCode_str().trim():"";
		String cCmdCode = modbusInfo.getCmdCode_str()!=null?modbusInfo.getCmdCode_str():"";
		 
		logger.info(">>>response(" + modbusInfo.getAddress_str() + "-上报)设备返回,cmdCode=" + cCmdCode + ",cCode="
				+ cCode + ",byteMsg=" + ConverUtil.convertByteToHexString(modbusInfo.getNewFullData()));
		if ("f7".equals(cCmdCode) && "04".equals(cCode)) { 	//节点数据上报协议处理
			protocols_API_F7_Response(ctx, modbusInfo);
		}
		if ("9a".equals(cCmdCode) && "80".equals(cCode)) { 	//查询组网个数
			protocols_API_9a_Response(ctx, modbusInfo);
		}
		if ("97".equals(cCmdCode) && "80".equals(cCode)) { 	//查询节点列表
			protocols_API_97_Response(ctx, modbusInfo);
		}
		if ("42".equals(cCmdCode) && "80".equals(cCode)) { 	//开关灯
			protocols_API_42_Response(ctx, modbusInfo);
		}
		//...
	}
	
	public static boolean protocols_API_42_Response(ChannelHandlerContext ctx, ModbusInfo modbusInfo) { 
		boolean excuSeccess = true;
		String cCode = modbusInfo.getcCode_str()!=null?modbusInfo.getcCode_str().trim():"";
		String cCmdCode = modbusInfo.getCmdCode_str()!=null?modbusInfo.getCmdCode_str():"";
		String plcsn = modbusInfo.getAddress_str();
		try {
			logger.info("=====>>>protocols_42  返回开关灯结果    ...===============");
			String hex_ptd = ConverUtil.convertByteToHexString(modbusInfo.getPdt()); // ack
			ResponseCache ls = ResponseCacheUtils.plc_ResponseCache.get(modbusInfo.getCacheMsgId());
			if ("01".equals(hex_ptd)) {
				ResponseCacheUtils.sendkafka_by_ResponseCache(modbusInfo.getCacheMsgId(), ResponseCode.HZYW_PLC_RESPONSE_SUCCESSFUL);
				logger.info(">>>protocols_API_42_Response(" + modbusInfo.getAddress_str() + ")响应设备,cmdCode=" + cCmdCode + ",cCode="
						+ cCode + " ,集中器反馈：开/关/调灯成功!!!");
			}else if ("02".equals(hex_ptd)) {
				ResponseCacheUtils.sendkafka_by_ResponseCache(modbusInfo.getCacheMsgId(), ResponseCode.HZYW_PLC_RESPONSE_FORMAT_ERROR);
				logger.info(">>>protocols_API_42_Response(" + modbusInfo.getAddress_str() + ")响应设备,cmdCode=" + cCmdCode + ",cCode="
						+ cCode + " ,集中器反馈：命令或数据格式无效!!!");
			} else if ("03".equals(hex_ptd)) {
				ResponseCacheUtils.sendkafka_by_ResponseCache(modbusInfo.getCacheMsgId(), ResponseCode.HZYW_PLC_RESPONSE_TIMEOU);
				logger.info(">>>protocols_API_42_Response(" + modbusInfo.getAddress_str() + ")响应设备,cmdCode=" + cCmdCode + ",cCode="
						+ cCode + " ,集中器反馈：集中器忙!!!");
			} else {
				logger.info(">>>protocols_API_42_Response(" + modbusInfo.getAddress_str() + ")响应设备,cmdCode=" + cCmdCode + ",cCode="
						+ cCode + " ,集中器反馈：返回数据异常,无此PDT!!!");
			}
		} catch (Exception e) {
			logger.error(">>>protocols_API_42_Response(" + modbusInfo.getAddress_str() + ")响应设备,cmdCode="+cCmdCode+",cCode="+cCode+",exception ！", e);
			excuSeccess = false;
		}
		return excuSeccess;

	}
	
	public static Map<String,Map<Integer,String>> all = new HashMap<String,Map<Integer,String>>();
	/**
	 *设备上报 - 查询组网个数
	 */
	public static boolean protocols_API_9a_Response(ChannelHandlerContext ctx, ModbusInfo modbusInfo) {
		boolean excuSeccess = true;
		String cCode = modbusInfo.getcCode_str()!=null?modbusInfo.getcCode_str().trim():"";
		String cCmdCode = modbusInfo.getCmdCode_str()!=null?modbusInfo.getCmdCode_str():"";
		String plcsn = modbusInfo.getAddress_str();
		try {
			
			logger.info("=====>>>protocols_9a  返回查询组网个数    ...===============");
			byte[] allpdt = modbusInfo.getPdt();
		 
			//解析PDT
			/*  帧总数	帧总数				1 B
				帧数		当前帧是第几个帧		1 B
				节点1	节点ID				6 Bs
						上级ID	            6 Bs
				 …	…	…
				节点n	同上		12 			Bs
            */
			byte[] total_frame_bytes = {allpdt[0]}; 
		    long total_frame = ByteUtils.byteArrayToLong(total_frame_bytes);      //帧总数
		    byte[] current_frame_bytes = {allpdt[2]};
		    long current_frame = ByteUtils.byteArrayToLong(current_frame_bytes); //第几个帧
		    if(current_frame == total_frame-1){
	    		all.remove(plcsn+"_"+cCmdCode+"_"+cCode);//清除临时缓存
		    }
		    
		    Map<Integer,String> nodeTreeMap = new TreeMap<Integer,String>();
		    int p=0,index=1;
	    	byte[] x = new byte[6];
	    	for(int i=2;i < allpdt.length; i++){
	    		if(p%6==0){//每6个字节重新取一次代表一个节点
	    			p=0;
	    			x = new byte[6];
	    		} 
	    		x[p] = allpdt[i];
	    		if(p==5){
	    			nodeTreeMap.put(index, ConverUtil.convertByteToHexString(x));
	    			index++;
	    		}
	    		p++;
	    	}
	    	System.out.println(">>>已组网节点：" +current_frame+"/"+total_frame +" "+JSON.toJSONString(nodeTreeMap));
	    	
	    	if(all.get(plcsn+"_"+cCmdCode+"_"+cCode)!=null){
	    		all.get(plcsn+"_"+cCmdCode+"_"+cCode).putAll(nodeTreeMap);//累积
	    	}else{
	    		all.put(plcsn+"_"+cCmdCode+"_"+cCode, nodeTreeMap);
	    	}
	    	
	    	String resultjson = "";
	    	if(total_frame == current_frame){
		    	//最后一帧
	    		resultjson = JSON.toJSONString(all);
	    		//all.remove(plcsn+"_"+cCmdCode+"_"+cCode);//清除临时缓存
	    		System.out.println(">>>已组网节点："+JSON.toJSONString(nodeTreeMap));
		    }
		} catch (Exception e) {
			logger.error(">>>initstep(" + modbusInfo.getAddress_str() + ")响应设备,cmdCode=F7,exception ！", e);
			excuSeccess = false;
		}
		return excuSeccess;

	}
	
	public static boolean protocols_API_97_Response(ChannelHandlerContext ctx, ModbusInfo modbusInfo) {
		boolean excuSeccess = true;
		String cCode = modbusInfo.getcCode_str()!=null?modbusInfo.getcCode_str().trim():"";
		String cCmdCode = modbusInfo.getCmdCode_str()!=null?modbusInfo.getCmdCode_str():"";
		String plcsn = modbusInfo.getAddress_str();
		try {
			
			logger.info("=====>>>protocols_97  返回节点列表    ...===============");
			byte[] allpdt = modbusInfo.getPdt();
		 
			//解析PDT
			/*帧总数			帧总数				1B
			     帧数			当前帧是第几个帧		1B
			     节点1			节点UID				    6 Bs
			      节点的组号： 1~255；注意：组号不能为0	1 B
			      设备类型(见附录A)						1B
			   …	…	…
			      节点n									同上	8 Bs */
			byte[] total_frame_bytes = {allpdt[0]}; 
		    long total_frame = ByteUtils.byteArrayToLong(total_frame_bytes);      //帧总数
		    byte[] current_frame_bytes = {allpdt[2]};
		    long current_frame = ByteUtils.byteArrayToLong(current_frame_bytes); //第几个帧
		    if(current_frame == total_frame-1){
	    		all.remove(plcsn+"_"+cCmdCode+"_"+cCode);//清除临时缓存
		    }
		     
		    //循环获取8个字节表示一个节点
		    Map<Integer,String> nodeTreeMap = new TreeMap<Integer,String>();
		    int p=0,index=1;
	    	byte[] a = new byte[6]; //节点ID
	    	byte[] b = new byte[1]; //组号
	    	byte[] c = new byte[1]; //设备类型
	    	for(int i=2;i < allpdt.length; i++){
	    		if(p%8==0){//每8个字节重新取一次代表一个节点
	    			p=0;
	    			a = new byte[6];
	    			b = new byte[1];
	    			c = new byte[1];
	    		} 
	    		
	    		if(p < 6){
	    			a[p] = allpdt[i];
	    		}
	    		if(p == 6){
	    			b[0] = allpdt[i];
	    		}
	    		if(p == 7){
	    			c[0] = allpdt[i];
	    		}
	    		 
	    		if(p==7){
	    			nodeTreeMap.put(index, ConverUtil.convertByteToHexString(a)
	    					+"/"+ConverUtil.convertByteToHexString(b)
	    					+"/"+ConverUtil.convertByteToHexString(c));
	    			index++;
	    		}
	    		p++;
	    	}
	    	System.out.println(">>>返回节点列表：" +current_frame+"/"+total_frame +" "+JSON.toJSONString(nodeTreeMap));
	    	
	    	if(all.get(plcsn+"_"+cCmdCode+"_"+cCode)!=null){
	    		all.get(plcsn+"_"+cCmdCode+"_"+cCode).putAll(nodeTreeMap);//累积
	    	}else{
	    		all.put(plcsn+"_"+cCmdCode+"_"+cCode, nodeTreeMap);
	    	}
	    	
	    	String resultjson = "";
	    	if(total_frame == current_frame){
		    	//最后一帧
	    		resultjson = JSON.toJSONString(all);
	    		//all.remove(plcsn+"_"+cCmdCode+"_"+cCode);//清除临时缓存
	    		System.out.println(">>>返回节点列表："+JSON.toJSONString(nodeTreeMap));
		    }
		} catch (Exception e) {
			logger.error(">>>initstep(" + modbusInfo.getAddress_str() + ")响应设备,cmdCode="+cCmdCode+",cCode="+cCode+",exception ！", e);
			excuSeccess = false;
		}
		return excuSeccess;

	}
	
	
	/**
	 *状态数据上报
	 */
	public static boolean protocols_API_F7_Response(ChannelHandlerContext ctx, ModbusInfo modbusInfo) {
		String cCode = modbusInfo.getcCode_str()!=null?modbusInfo.getcCode_str().trim():"";
		String cCmdCode = modbusInfo.getCmdCode_str()!=null?modbusInfo.getCmdCode_str():"";
		boolean excuSeccess = true;
		ByteBuf byteBuf = null;
		try {
			logger.info("=====>>>protocols_F7  状态数据上报   ...===============");
			String plc_id = getPlcIdByPlcSn(modbusInfo.getAddress_str(),PlcProtocolsUtils.getPort(ctx.channel()));
			byte[] allpdt = modbusInfo.getPdt();
			byte[] temp0_bytes = {allpdt[0]}; 
		    long node_len = ByteUtils.byteArrayToLong(temp0_bytes);     //节点总数
		    
		    logger.info("-------节点总数--------" + node_len);
			ByteBuf allPdtBuf = Unpooled.wrappedBuffer(allpdt,0,allpdt.length);//所有节点
		    allPdtBuf.readByte();//移动一个字节
			byteBuf = allPdtBuf.copy();  //拷贝readindex开始到后面的字节 ，深度拷贝，即硬拷贝
			String nodelist = ConverUtil.convertByteToHexString(byteBuf.array());
			logger.info("------- 节点list(1->n)--------" + nodelist   );

			
			//判断是新设备还是老设备 
			boolean isOld = true;
			if(((nodelist.length())/2)%18 == 0){
				//说明是新设备
				isOld = false;
				logger.info("-------isOld---新设备-----" + isOld);
			}
			if(((nodelist.length())/2)%27 == 0){
				//说明是老设备
				isOld = false;
				logger.info("-------isOld---老设备----" + isOld);
			}
			
 			//需要定义14个临时变量  27个字节
			byte[] temp1 = new byte[6]; //节点ID
			byte[] temp2 = new byte[1]; //设备码  列如:19是100W
			byte[] temp3 = new byte[1]; //在线状态   00H：不在线;    01H：在线
			byte[] temp4 = new byte[2]; //输入电压V   单位0.1V
			byte[] temp5 = new byte[2]; //输入电流A   单位mA
			byte[] temp6 = new byte[2]; //输入功率
			byte[] temp7 = new byte[1];  //功率因素
			byte[] temp8 = new byte[2]; //状态                  二进制位来表示
			byte[] temp9 = new byte[1];  //异常状态
			
			byte[] temp10 = new byte[1];  //灯具温度   新程序才有temp10~14的数据
			byte[] temp11 = new byte[2];  //输出功率
			byte[] temp12 = new byte[2];  //灯具运行时长
			byte[] temp13 = new byte[2];  //电能
			byte[] temp14 = new byte[2];  //故障时长
			List<Map> metricinfo = new ArrayList<Map>();
			String nodeid;
			for(int i=0;i < node_len; i++){ //一个节点
				metricinfo = new ArrayList<Map>();
				if(isOld){
					byteBuf.readBytes(temp1).readBytes(temp2).readBytes(temp3).readBytes(temp4).readBytes(temp5)
					.readBytes(temp6).readBytes(temp7).readBytes(temp8).readBytes(temp9).readBytes(temp10)
					.readBytes(temp11).readBytes(temp12).readBytes(temp13).readBytes(temp14);
				}else{
					byteBuf.readBytes(temp1).readBytes(temp2).readBytes(temp3).readBytes(temp4).readBytes(temp5)
					.readBytes(temp6).readBytes(temp7).readBytes(temp8).readBytes(temp9);
				}
				
				String _temp1 = ConverUtil.convertByteToHexString(temp1);//节点ID
				logger.info("-------plc_node_sn=----" + _temp1);
				nodeid = getPlcNodeIdByPlcNodeSn(_temp1,PlcProtocolsUtils.getPort(ctx.channel()));
				addMetric(IotInfoConstant.dev_plc_node_sn, _temp1 , null ,metricinfo);
				String _temp2 = ConverUtil.convertByteToHexString(temp2);//设备码  列如:19是100W
				addMetric(IotInfoConstant.dev_plc_node_devCode, _temp2 , null ,metricinfo);
				String _temp3 = ConverUtil.convertByteToHexString(temp3);//在线状态   00H：不在线;    01H：在线
				addMetric(IotInfoConstant.dev_plc_node_a_status, _temp3 , null ,metricinfo);
				double _temp4 = ByteUtils.byteArrayToLong(temp4) * 0.1;  // 输入电压  ,单位0.1V ,   
				addMetric(IotInfoConstant.dev_plc_node_voltage_in, _temp4 , IotInfoConstant.getUnit(IotInfoConstant.dev_plc_node_voltage_in) ,metricinfo);
				long _temp5 = ByteUtils.byteArrayToLong(temp5);          //整形            输入电流
				addMetric(IotInfoConstant.dev_plc_node_a_electri_in, _temp5 , "mA" ,metricinfo);
				double _temp6 = ByteUtils.byteArrayToLong(temp6) * 0.1;  //整形            输入功率  单位0.1W
				addMetric(IotInfoConstant.dev_plc_node_a_power, _temp6 , "W" ,metricinfo);
				long _temp7 = ByteUtils.byteArrayToLong(temp7);          //整形            功率因素 0~100对应0~100%
				addMetric(IotInfoConstant.dev_plc_node_a_pf, _temp7 , "%" ,metricinfo);           
				String _temp8 = ConverUtil.byteArrToBinStr(temp8); //状态   转化位二进制串 
				parseSinglAndSendKafka(_temp8,temp2, plc_id, nodeid,_temp1);
				
				long _temp9 = ByteUtils.byteArrayToLong(temp9);//异常状态   0-正常，1-异常亮灯，2-异常灭灯   没有这个字段哦，看了下报文，这里起个字段算合理点
				addMetric("xxxx", _temp9 , null ,metricinfo);
				if(isOld){
					long _temp10 = ByteUtils.byteArrayToLong(temp10);//灯具温度 ,   温度范围从-127℃~+127℃，单位为1℃
					addMetric(IotInfoConstant.dev_plc_node_temperature, _temp10 , IotInfoConstant.getUnit(IotInfoConstant.dev_plc_node_temperature) ,metricinfo);
					double _temp11 = ByteUtils.byteArrayToLong(temp11)*0.1;//输出功率      ,   单位0.1W
					addMetric(IotInfoConstant.dev_plc_node_power, _temp11 , "W" ,metricinfo);
					long _temp12 = ByteUtils.byteArrayToLong(temp12);  //灯具运行时长
					addMetric(IotInfoConstant.dev_plc_node_runtime, _temp12 , "H" ,metricinfo);
					double _temp13 = ByteUtils.byteArrayToLong(temp13)*0.1;  //电能          0.1KW
					addMetric(IotInfoConstant.dev_plc_node_electri_energy, _temp13 , "KW" ,metricinfo);
					long _temp14 = ByteUtils.byteArrayToLong(temp14); //故障时长   1小时
					addMetric(IotInfoConstant.dev_plc_node_error_runtime, _temp14 , "H" ,metricinfo);
				}
				    
				//构造一条状态数据上报
				MetricInfoResponseDataVO  metricInfoResponseDataVO = new MetricInfoResponseDataVO();
				metricInfoResponseDataVO.setId(nodeid);
				//metricInfoResponseDataVO.setAttributers(null);
				metricInfoResponseDataVO.setDefinedAttributers(metricinfo);
				Map<String,String> tags = new HashMap<String,String>();
				tags.put(IotInfoConstant.dev_plc_dataaccess_key, IotInfoConstant.dev_plc_dataaccess_value); //指定接入类型是PLC接入类型
				metricInfoResponseDataVO.setTags(tags);
				
				// 消息结构
				MessageVO messageVo = new MessageVO<>();
				messageVo.setType("metricInfoResponse");
				messageVo.setTimestamp(DateUtil.currentSeconds());
				messageVo.setMsgId(UUID.randomUUID().toString());
				messageVo.setData(metricInfoResponseDataVO);
				messageVo.setGwId(plc_id);
				//send kafka
				System.out.println("--上报METRIC--" + JSON.toJSONString(messageVo));
				//SendKafkaUtils.sendKafka("iot_topic_dataAcess", JSON.toJSONString(messageVo));
	    	}
			if (byteBuf != null)ReferenceCountUtil.release(byteBuf);//释放内存
 		} catch (Exception e) {
 			if (byteBuf != null)ReferenceCountUtil.release(byteBuf);
			logger.error(">>>initstep(" + modbusInfo.getAddress_str() + ")响应设备,cmdCode=,"+cCmdCode+",cCode="+cCode+ " ,exception ！", e);
			excuSeccess = false;
		}
		return excuSeccess;

	}
	
	public static long _6f = 110;
	public static long _70 = 112;
	public static long _7f = 127;
	/**
	 * 
	 * 1、路灯电源（设备码：00H~6FH）
		bit15~bit9：bit15~bit9：0x00,为老程序，没有以下红色数据，0x88，则为新程序，将上报以下红色数据
		bit8：温度过高报警位  bit7：温度过低报警位
		bit6：无法启动报警位  bit5：输出短路报警位
		bit4：输出开路报警位  bit3：功率过高报警位
		bit2：输入电压过高报警位
		bit1：输入电压过低报警位
		以上bit1~bit8：0表示无报警，1表示有报警。
		bit0：电源状态位；0：电源关，1：电源开。

		2、路灯控制器（设备码：70H~7FH）
		bit15~bit8表示B灯的状态：
		bit15：继电器失效报警位 bit14：过载报警位
		bit13：欠载报警位       bit12：过压警位
		bit11：欠压报警位       bit10：过流报警位
		bit9： 欠流报警位 
		以上报警位： 0表示无报警，1表示有报警      
		bit8：继电器状态位；0表示关，1表示开
		------------------------------------
		bit7~bit0表示A灯的状态：
		bit7：继电器失效报警位  bit6：过载报警位
		bit5：欠载报警位        bit4：过压警位
		bit3：欠压报警位        bit2：过流报警位
		bit1：欠流报警位 
		以上报警位： 0表示无报警，1表示有报警      
		bit0：继电器状态位；0表示关，1表示开
	 * 
	 * @param _temp8  2个字节转化为16个0,1的二进制字符串
	 * @param plc_node_devCode
	 */
	public static void parseSinglAndSendKafka(String _temp8,byte[] plc_node_devCode ,String plc_id,String plc_node_id,String plc_node_sn){ //dev_plc_node_devCode ="plc_node_devCode" 设备码
		//2个字节，通过二进制 0/1来表示
		System.out.println(">>二进制 = " + _temp8);
		List<Map> signals = new ArrayList<Map>();
		long long_plc_node_devCode = ByteUtils.byteArrayToLong(plc_node_devCode);
		if(long_plc_node_devCode >= 0 && long_plc_node_devCode <= _6f){ //表示路灯电源信息
			logger.info("-------plc_node_sn=----" + plc_node_sn + "/long_plc_node_devCode="+long_plc_node_devCode+ "/ 电源控制器信号 ");
			//bit15~bit9：0x00,为老程序，没有以下红色数据，0x88   前面已经根据长度计算出新老程序了，这里不需要
			/* 0 --》 9
			bit8：温度过高报警位  bit7：温度过低报警位
			bit6：无法启动报警位  bit5：输出短路报警位
			bit4：输出开路报警位  bit3：功率过高报警位
			bit2：输入电压过高报警位
			bit1：输入电压过低报警位
			以上bit1~bit8：0表示无报警，1表示有报警。
			bit0：电源状态位；0：电源关，1：电源开
			*/
			//1111111 100000000(bit0->8从右边数过去)
			String bit_0_15 = _temp8.replace(",", "");
			checkAlarm(bit_0_15, 8 , 109001 , signals);  //109001含义 参考报文
			checkAlarm(bit_0_15, 9 , 109002 , signals);
			checkAlarm(bit_0_15, 10 , 109003 , signals);
			checkAlarm(bit_0_15, 11 , 109004 , signals);
			checkAlarm(bit_0_15, 12 , 109005 , signals);
			checkAlarm(bit_0_15, 12 , 109006 , signals);
			checkAlarm(bit_0_15, 13 , 109007 , signals);
			checkAlarm(bit_0_15, 14 , 109008 , signals);
			checkAlarm(bit_0_15, 15 , 109009 , signals);//bit0
			 
		}else if(long_plc_node_devCode >= _70 && long_plc_node_devCode <=_7f){ //表示路灯控制器信息
			logger.info("-------plc_node_sn=----" + plc_node_sn + "/long_plc_node_devCode="+long_plc_node_devCode+ "/ 路灯控制器信号 ");
			String bit_0_15 = _temp8.replace(",", "");
			checkAlarm(bit_0_15, 0 , 109010 , signals);  
			checkAlarm(bit_0_15, 1 , 109011 , signals);
			checkAlarm(bit_0_15, 2 , 109012 , signals);
			checkAlarm(bit_0_15, 3 , 109013 , signals);
			checkAlarm(bit_0_15, 4 , 109014 , signals);
			checkAlarm(bit_0_15, 5 , 109015 , signals);
			checkAlarm(bit_0_15, 6 , 109016 , signals);
			checkAlarm(bit_0_15, 7 , 109017 , signals); //A灯控制器,继电器失效报警位
			//checkAlarm(bit_0_15, 8 , 109009 , signals); 
			checkAlarm(bit_0_15, 8 , 109018 , signals);  
			checkAlarm(bit_0_15, 9 , 109019 , signals);
			checkAlarm(bit_0_15, 10 , 109020 , signals);
			checkAlarm(bit_0_15, 11 , 109021 , signals);
			checkAlarm(bit_0_15, 12 , 109022 , signals);
			checkAlarm(bit_0_15, 13 , 109023 , signals);
			checkAlarm(bit_0_15, 14 , 109024 , signals);
			checkAlarm(bit_0_15, 15 , 109025 , signals); //B灯控制器,继电器失效报警位
		}else{
			logger.info("-------plc_node_sn=----" + plc_node_sn + "/long_plc_node_devCode="+long_plc_node_devCode+"/ 设备码超出范围.. ");
		}
		DevSignlResponseDataVO devSignlResponseDataVO = new DevSignlResponseDataVO();
		devSignlResponseDataVO.setId(plc_node_id);
		devSignlResponseDataVO.setSignals(signals);
		Map<String,String> tags = new HashMap<String,String>();
		tags.put(IotInfoConstant.dev_plc_dataaccess_key, IotInfoConstant.dev_plc_dataaccess_value); //指定接入类型是PLC接入类型
		devSignlResponseDataVO.setTags(tags);
		//消息结构
		MessageVO messageVo = getMessageVO(devSignlResponseDataVO,"devSignlResponse",System.currentTimeMillis(),UUID.randomUUID().toString(),plc_id);
		System.out.println("--上报signl--" + JSON.toJSONString(messageVo));
		//kafka处理
		//sendKafka(JSON.toJSONString(messageVo),""));
	}
	
	/**
	 * 字符1表示告警，只有存在告警才需要收集并上报
	 * 
	 * @param _tempStr  字符串
	 * @param charIndex 字符串索引 0开始
	 * @param alarmCode 告警编号
	 * @param signals   告警map
	 */
	public static void checkAlarm(String _tempStr,int charIndex ,Integer alarmCode ,List<Map> signals){
		Map<String,Integer> item = new HashMap<String,Integer>();
		if(_tempStr.charAt(charIndex) == '1'){
			item.put("signal_code", alarmCode);
			signals.add(item);
		}
	}
	/**
	 * @param type  字段或属性
	 * @param value 值
	 * @param unit  单位 -没有单位给null
	 * @param metricinfo 
	 */
	public static void addMetric(String type,Object value ,String unit ,List<Map> metricinfo){
		Map<String,Object> metric = new HashMap<String,Object>();
		metric.put("type", type);
		metric.put("value", value);
		metric.put("company", unit);
		metricinfo.add(metric);
	}
	
	public static <T> MessageVO<T>  getMessageVO(T data,String type,long timestamp,String msgId,String Plcid) {
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
	
	
	//测试
	public static void main(String[] args) {
			/*String str = "6e";
			String lampA = hexStringToByte(str);
			System.out.println(lampA);*/
		byte[] test = "6f".getBytes();
		byte b=test[0];
		for(int i=0;i<8;i++){
	     if(((b>>i)&0x01)==1)
	    	 System.out.println("第"+i+"位:"+1);
	      else
	     System.out.println("第"+i+"位:"+0);
		}
			/*try {
				String two =byteArrToBinStr(ConverUtil.hexStrToByteArr("8c"));
				System.out.println(two);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			/*char[] test = str.toCharArray();
			System.out.println(test[0]);*/
		
		}
		
	/**
	*16进制转二进制
	*/	
	public static String hexStringToByte(String hex) {
	    int i = Integer.parseInt(hex, 16);
	    String str2 = Integer.toBinaryString(i);
	    return str2;
	}
	 
	public static String byteArrToBinStr(byte[] b) {
	    StringBuffer result = new StringBuffer();
	    for (int i = 0; i < b.length; i++) {
	        result.append(Long.toString(b[i] & 0xff, 2) + ",");
	    }
	    return result.toString().substring(0, result.length() - 1);
	}
}
