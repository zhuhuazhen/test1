package com.hzyw.iot.utils;

import com.hzyw.iot.vo.dataaccess.DevInfoDataVO;
import com.hzyw.iot.vo.dataaccess.DevOnOffline;
import com.hzyw.iot.vo.dataaccess.MessageVO;
import com.hzyw.iot.vo.dataaccess.MetricInfoResponseDataVO;
import com.hzyw.iot.vo.dataaccess.RequestDataVO;

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
	
	public static JSONObject protocals_process(String requestMessageVOJSON){
		final ModbusInfo modbusInfo = new ModbusInfo();
		JSONObject result = new JSONObject();
		try {
			 
			logger.info("\n mssageVO: ({}) ", requestMessageVOJSON );
			
			JSONObject jsonObject = JSON.parseObject(requestMessageVOJSON);//消息包
			String jsonStr=((JSONObject) jsonObject.get("data")).toJSONString();
			RequestDataVO requestVO = JSONObject.parseObject(jsonStr,RequestDataVO.class);
            Map<String,Object> methodMap =(Map<String, Object>)requestVO.getMethods().get(0);   
            //List<Map<String,Object>> inList=(List<Map<String,Object>>) methodMap.get("in"); //上报参数属性集合
            String method = methodMap.get("method").toString(); //方法(物联网平台侧)            
            String plc_id = jsonObject.getString("gwId");       //集中器id(物联网平台侧)
            String plc_sn = getPlcSnByPlcID(plc_id);            //集中器地址(边缘设备侧)
            
            if(GlobalInfo.SN_CHANNEL_INFO_MAP.get(getPortByPlcID(plc_id)+plc_sn) == null
            		|| GlobalInfo.SN_CHANNEL_INFO_MAP.get(getPortByPlcID(plc_id)+plc_sn).getChannel() == null){
            	logger.warn("request请求,无法下发，没有找到 sn:{} 下的channel/请检查此PLC是否已经连接并登陆 !!!", plc_sn);
            	result.put("msg", "fail!");
            	result.put("data", "request请求,无法下发，没有找到 sn:{} 下的channel/请检查此PLC是否已经连接并登陆 !!! sn/"+plc_sn);
            	return result;
            }
            Channel channel = GlobalInfo.SN_CHANNEL_INFO_MAP.get(getPortByPlcID(plc_id)+plc_sn).getChannel();//获取要下发的sn设备对应的channel
            String cmdCode = (String)IotInfoConstant.allDevInfo.get(PlcProtocolsUtils.getPort(channel)).get(plc_sn+"_method").get(method);
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
			logger.error(">>>request(" + modbusInfo.getAddress_str() + ")request下发,,cmdCode="+modbusInfo.getCmdCode_str()
        				+",ccode="+modbusInfo.getcCode_str()+",exception1！", e);
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
            if (future.isSuccess()) {
        		logger.info(">>>request ("+modbusInfo.getAddress_str()+")请求设备成功/"+logmsg+"！,cmdCode="+modbusInfo.getCmdCode_str()
        				+",ccode="+modbusInfo.getcCode_str()+",byteMsg=" + ConverUtil.convertByteToHexString(modbusInfo.getNewFullData()));
            } else {
            	logger.info(">>>request ("+modbusInfo.getAddress_str()+")请求设备失败/"+logmsg+"！,cmdCode="+modbusInfo.getCmdCode_str()
        				+",ccode="+modbusInfo.getcCode_str()+",byteMsg=" + ConverUtil.convertByteToHexString(modbusInfo.getNewFullData()));
            }
        });
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
		if(!("f0".equals(modbusInfo.getCmdCode_str().toLowerCase()) 
	            		|| "f1".equals(modbusInfo.getCmdCode_str().trim().toLowerCase()))){
			return true; //忽略f0登陆指令，f1心跳指令
		}
		if ("9a".equals(cCmdCode)  		//查询组网个数
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
		try {
			logger.info(">>>response(" + modbusInfo.getAddress_str() + "-上报)设备返回,cmdCode=" + cCmdCode + ",cCode="
					+ cCode + ",byteMsg=" + ConverUtil.convertByteToHexString(modbusInfo.getNewFullData()));
			if ("f7".equals(cCmdCode) && "04".equals(cCode)) { 	//节点数据上报协议处理
				//protocols_API_F7_Response(ctx, modbusInfo);
			}
			if ("9a".equals(cCmdCode) && "80".equals(cCode)) { 	//查询组网个数
				protocols_API_9a_Response(ctx, modbusInfo);
			}
			if ("97".equals(cCmdCode) && "80".equals(cCode)) { 	//查询节点列表
				protocols_API_97_Response(ctx, modbusInfo);
			}
			//其他报文转化可以继续添加
			//...
			
		} catch (Exception e) {
			logger.error(">>>initstep(" + modbusInfo.getAddress_str() + "-上报)设备返回,cmdCode=" + cCmdCode + ",cCode="
					+ cCode + " ,exception1！", e);
		}

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
		boolean excuSeccess = true;
		//final ModbusInfo modbusInfo = new ModbusInfo(); 
		try {
			//68,00,00,00,00,01,00,68,04,4a,f7,04,00,00,02,00,04,ee,0a,01,09,33,00,3f,00,1e,14,00,00,00,00,00,10,00,14,d1,00,00,00,00,00,00,00,00,00,00,00,00,00,00,10,02,00,b6,00,00,00,00,00,00,00,00,00,00,00,00,00,00,10,02,01,48,00,00,00,00,00,00,00,00,00,00,00,00,de,16
			//新设备
			//68,00,00,00,00,01,00,68,04,4a,f7,    04, 00,00,02,00,04,ee,0a,01,09,33,00,3f,00,1e,14,00,00,00(18),00,00,10,00,14,d1,00,00,00,00,00,00,00,00,00,00,00,00(18),00,00,10,02,00,b6,00,00,00,00,00,00,00,00,00,00,00,00(18),00,00,10,02,01,48,00,00,00,00,00,00,00,00,00,00,00,00(18),de,16
			logger.info("=====>>>protocols_F7  状态数据上报   ...===============");
			byte[] allpdt = modbusInfo.getPdt();
			byte[] temp0_bytes = {allpdt[0]}; 
		    long node_len = ByteUtils.byteArrayToLong(temp0_bytes);     //节点总数
			ByteBuf byteBuf = Unpooled.wrappedBuffer(allpdt,1,allpdt.length);//所有节点
			
			//判断是新设备还是老设备 
			boolean isOld = true;
			if(allpdt.length-1%18 == 0){
				//说明是新设备
				isOld = false;
			}
			if(allpdt.length-1%27 == 0){
				//说明是老设备
				isOld = true;
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
			for(int i=0;i < node_len; i++){ //一个节点
				if(isOld){
					byteBuf.readBytes(temp1).readBytes(temp2).readBytes(temp3).readBytes(temp4).readBytes(temp5)
					.readBytes(temp6).readBytes(temp7).readBytes(temp8).readBytes(temp9).readBytes(temp10)
					.readBytes(temp11).readBytes(temp12).readBytes(temp13).readBytes(temp14);
				}else{
					byteBuf.readBytes(temp1).readBytes(temp2).readBytes(temp3).readBytes(temp4).readBytes(temp5)
					.readBytes(temp6).readBytes(temp7).readBytes(temp8).readBytes(temp9);
				}
				
				String _temp1 = ConverUtil.convertByteToHexString(temp1);//节点ID
				String _temp2 = ConverUtil.convertByteToHexString(temp2);//设备码  列如:19是100W
				String _temp3 = ConverUtil.convertByteToHexString(temp3);//在线状态   00H：不在线;    01H：在线
				double _temp4 = ByteUtils.bytes2Double(temp4);//浮点数     输入电压
				double _temp5 = ByteUtils.bytes2Double(temp5);  //整形            输入电流
				double _temp6 = ByteUtils.bytes2Double(temp6);  //整形            输入功率
				String _temp7 = ConverUtil.convertByteToHexString(temp7);  //整形            功率因素
				String _temp8 = ConverUtil.convertByteToHexString(temp8); //状态   转化位二进制串？
				state(_temp8);//解析二进制数据     信号数据，在里面构造一条信号数据上报
				String _temp9 = ConverUtil.convertByteToHexString(temp9);//异常状态
				if(isOld){
					String _temp10 = ConverUtil.convertByteToHexString(temp10);//灯具温度   新程序才有temp10~14的数据
					double _temp11 = ByteUtils.bytes2Double(temp11);;//输出功率
					double _temp12 = ByteUtils.bytes2Double(temp12);;  //灯具运行时长
					double _temp13 = ByteUtils.bytes2Double(temp13);;  //电能
					double _temp14 = ByteUtils.bytes2Double(temp14);; //故障时长
				}
				//构造一条状态数据上报
				
	    	}
			if (byteBuf != null)ReferenceCountUtil.release(byteBuf);//释放内存
			
			
			
			
			//解析PDT
			  
			
			
			//state(_temp8);//解析二进制数据
			
			
			// 设备上下VO
			MetricInfoResponseDataVO  metricInfoResponseDataVO = new MetricInfoResponseDataVO();
			// 初始化设备属性
			IotInfoConstant iotInfoConstant = new IotInfoConstant();
			Map<String,Object> tags =new HashMap<String,Object>();//tags
			tags.put("agreement", "plc");
			
			/*metricInfoResponseDataVO.setId(data.get(GatewayMqttUtil.dataModel_messageVO_data_deviceId).toString());
			metricInfoResponseDataVO.setAttributers((List<Map>)data.get(GatewayMqttUtil.dataModel_messageVO_data_attributers));
			metricInfoResponseDataVO.setDefinedAttributers((List<Map>)data.get(GatewayMqttUtil.dataModel_messageVO_data_definedAttributers));
			metricInfoResponseDataVO.setTags((Map)data.get(GatewayMqttUtil.dataModel_messageVO_data_tags));
			*/
			// 消息结构
			MessageVO messageVo = new MessageVO<>();
			messageVo.setType("devOnline");
			messageVo.setTimestamp(DateUtil.currentSeconds());
			messageVo.setMsgId(UUID.randomUUID().toString());
			//messageVo.setData(devOnline);
			//messageVo.setGwId(plc_sn);
			
			System.out.println(JSON.toJSONString(messageVo));
			//解析上面每个temp即可得到报文内容，并NEW待上报的消息体即可
			
			// 获取设备信息 通过chennelID ==> devinfo
			String plc_nodde_SN = "";
			Map<String, Object> def_attributers = IotInfoConstant.allDevInfo.get(PlcProtocolsUtils.getPort(ctx.channel()))
					.get(plc_nodde_SN + "_defAttribute");// 从自定义的字段里面获取值
   
			//发送KAFKA上报
		} catch (Exception e) {
			logger.error(">>>initstep(" + modbusInfo.getAddress_str() + ")响应设备,cmdCode=F7,exception ！", e);
			excuSeccess = false;
		}
		return excuSeccess;

	}
	
	/**
	 *F7状态二进制解析
	 */
	public static void state(String _temp8) {
		char[] lampA = hexStringToByte(StrUtil.sub(_temp8, 0, 2)).toCharArray();//截取一个字节转成2进制,并转成char
		char[] lampB = hexStringToByte(StrUtil.sub(_temp8, 2, 4)).toCharArray();
		for(int i=0;i<lampA.length;i++){
			
			
			
		}
		
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
