package com.hzyw.iot.utils;

import com.hzyw.iot.vo.dataaccess.MessageVO;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hzyw.iot.netty.channelhandler.ChannelManagerHandler;
import com.hzyw.iot.util.ByteUtils;
import com.hzyw.iot.util.constant.ConverUtil;
import com.hzyw.iot.util.constant.IotInfoConstant;
import com.hzyw.iot.vo.dc.GlobalInfo;
import com.hzyw.iot.vo.dc.ModbusInfo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;

/**
 * 初始化PLC配置： 1 集中器配置：向集中器写入经纬度信息 2 设置集中器时间：手动同步集中器时间 3 清除节点 4 删除flash节点 5
 * 开始组网（等待节点数*20秒） 6 查询组网(当全部节点都组网成功) 7 停止组网 8 存储节点 9 下发节点 10 配置节点（等待节点数*30秒） 11
 * 查询节点
 * 
 * 
 * @author Administrator
 *
 */
public class PlcProtocolsUtils {
	private static final Logger logger = LoggerFactory.getLogger(PlcProtocolsUtils.class);
	public static Map<String, String> gloable = new HashMap<String, String>();

	/**
	 * 设备是否已登陆成功
	 * @param modbusInfo
	 * @return
	 */
	public static boolean isLogin(ModbusInfo modbusInfo) {
		String flag = gloable.get(modbusInfo.getAddress_str() + "_login"); // 1表示已登陆
																			// 0
																			// 表示未登陆
																			// （通过心跳判断是否掉线来设置）
		if (flag != null && flag.equals("1")) {
			return true;
		}
		return false;
	}

	/**
	 * 设备是否已配置好
	 * @param modbusInfo
	 * @return
	 */
	public static boolean isConfig(ModbusInfo modbusInfo) {
		String flag = gloable.get(modbusInfo.getAddress_str() + "_config");// 1表示已下发配置到存储
																			// 0表示未下发配置过（只需要配置一次即可，配置以后才能控灯）
		if (flag != null && flag.equals("1")) {
			return true;
		}
		return false;
	}

	/**
	 * 直接发送字节流报文
	 * 
	 * @param ctx
	 * @param msg
	 * @throws Exception
	 */
	public static void ctxWriteByte(ChannelHandlerContext ctx, byte[] msg, String type) throws Exception {
		try {
			//logger.info(">>>响应设备,cmdCode="+type+",byteMsg=" + ConverUtil.convertByteToHexString(msg) );
			ctx.write(msg);
		} catch (Exception e1) {
			logger.error(">>>PlcProtocolsUtils::ctxWriteStr(),byteMsg=" + ConverUtil.convertByteToHexString(msg) + ",异常 ", e1);
		}
	}
	public static void ctxWriteByte_(Channel channel, byte[] msg, String type,final int step) throws Exception {
		try {
			logger.info(">>>响应设备,cmdCode="+type+",byteMsg=" + ConverUtil.convertByteToHexString(msg) );
			//ctx.write(msg);
			channel.writeAndFlush(msg).addListener((ChannelFutureListener) future -> { //监听下发的请求执行是否成功！
                if (future.isSuccess()) {
                	logger.info("ok");
                	//只要请求成功
                	switch (step) {
	                	case 2:
	                		//login(null,null);
	                	case 3:
	                		//
	                	default:
	                		//
                	}
                } else {
                	logger.error("send data to client exception occur: {}", future.cause());
                }
            });
		} catch (Exception e1) {
			logger.error(">>>PlcProtocolsUtils::ctxWriteStr(),byteMsg=" + ConverUtil.convertByteToHexString(msg) + ",异常 ", e1);
		}
	}

	/**
	 * 直接发送16进制的报文
	 * 
	 * @param ctx
	 * @param hexMsg
	 * @throws Exception
	 */
	public static void ctxWriteStr(ChannelHandlerContext ctx, String hexMsg, String type) throws Exception {
		try {
			logger.info(">>>响应设备,cmdCode="+type+",byteMsg=" + hexMsg );
			ctx.write(ConverUtil.hexStrToByteArr(hexMsg));
		} catch (Exception e1) {
			logger.error(">>>PlcProtocolsUtils::ctxWriteStr(),strMsg=" + hexMsg + ",异常 ", e1);
		}
	}
	
	private static final String logger_type_request = "request";
	private static final String logger_type_response = "response";
	/**
	 * @param plc_sn
	 * @param cmdCode
	 * @param hexMsg 报文
	 */
	public static String loggerBaseInfo(ModbusInfo modbusInfo){
		return ("plc_sn/cCode/cmdCode/hexMsg = /" + modbusInfo.getAddress_str() + "/" + modbusInfo.getcCode_str() + "/"
				+ modbusInfo.getCmdCode_str() + "/" + modbusInfo.toStringBW());
	}

	public static String getPort(Channel  channel){
		InetSocketAddress insocket = (InetSocketAddress)channel.localAddress();
		return String.valueOf(insocket.getPort());
	}
	/**
	 * @param ctx
	 * @param modbusInfo
	 * @return  true 成功   false 失败
	 */
	public static boolean resonseLogin(ChannelHandlerContext ctx, ModbusInfo modbusInfo) {
		ModbusInfo modbusInfo_FOh = new ModbusInfo();
		String plc_sn = modbusInfo.getAddress_str(); //"000000000100";
		Map<String,Boolean> resultFlagM = new HashMap<String,Boolean>();
		resultFlagM.put("resultFlag", true);
		try {
			// 登陆成功处理
			// 响应登陆成功请求    
			setProtocalsAuto(modbusInfo_FOh
					,ConverUtil.hexStrToByteArr(plc_sn)  //PLC SN
					,ConverUtil.hexStrToByteArr("80")    // 控制码
					,ConverUtil.hexStrToByteArr("F0")    //操作码
					,ConverUtil.hexStrToByteArr("01"));  //Pdt部分     01H：登陆成功      02H：登陆失败。03H：主机忙
			  
			logger.info("     >>>应该输入=680000000000016803094200000000000001002016");
			logger.info("     >>>实际输入=" + ConverUtil.convertByteToHexString(modbusInfo_FOh.getNewFullData()));
			System.out.println("====>>>init ModbusInfo :====");
			System.out.println(modbusInfo_FOh.toString());
			System.out.println("====<<<===");
			
			ctx.channel().writeAndFlush(modbusInfo_FOh.getNewFullDataWithByteBuf()).addListener((ChannelFutureListener) future -> { //监听是否成功
                if (future.isSuccess()) {
                	logger.info(">>>PlcProtocolsUtils::login()," + loggerBaseInfo(modbusInfo_FOh) + ",登陆成功，并已成功响应到设备! ");
                	// 上线成功需要上报消息到KAFKA 
                	//todo 
                	gloable.put(modbusInfo.getAddress_str() + "_login", "1"); // 设置设备上线状态        1-上线   0-下线
                	ChannelManagerHandler.setRTUChannelInfo(ctx, plc_sn);//登陆成功，建立设备和通道的全局映射关系
                } else {
                	resultFlagM.put("resultFlag", false);
                	logger.info(">>>PlcProtocolsUtils::login(),type=FO,登陆成功,响应并通知设备....调用失败! ");
                	logger.error(">>>send data to client exception occur: {}", future.cause());
                	gloable.put(modbusInfo.getAddress_str() + "_login", "0");  
                }
            });
		} catch (Exception e) {
			resultFlagM.put("resultFlag", false);
			gloable.put(modbusInfo.getAddress_str() + "_login", "0");
			logger.error(">>>PlcProtocolsUtils::login(),"+loggerBaseInfo(modbusInfo_FOh) + ",登陆异常 ", e);
			try {
				setProtocalsAuto(modbusInfo_FOh
						,ConverUtil.hexStrToByteArr(plc_sn)  //PLC SN
						,ConverUtil.hexStrToByteArr("80")    // 控制码
						,ConverUtil.hexStrToByteArr("F0")    //操作码
						,ConverUtil.hexStrToByteArr("02"));  //Pdt部分     01H：登陆成功      02H：登陆失败。03H：主机忙
				//ctxWriteByte(ctx, modbusInfo_FOh.getNewFullData(), "F0");
				ctx.channel().writeAndFlush(modbusInfo_FOh.getNewFullDataWithByteBuf()).addListener((ChannelFutureListener) future -> {  
	                if (future.isSuccess()) {
	                	logger.info(">>>PlcProtocolsUtils::login()," +loggerBaseInfo(modbusInfo_FOh) + ",登陆异常...响应并成功通知到设备!");
	                	gloable.put(modbusInfo.getAddress_str() + "_login", "0"); 
	                } else {
	                	logger.error(">>>PlcProtocolsUtils::login(),type=FO,登陆异常...响应并通知设备失败....调用失败!: {}", future.cause());
	                }
	            });
			} catch (Exception e1) {
				logger.error(">>>PlcProtocolsUtils::login::ctxWriteStr(),"
						+ loggerBaseInfo(modbusInfo_FOh) + ",登陆异常...响应过程中再次发生异常! ", e1);
			}  
		}
		return resultFlagM.get("resultFlag");
	}

	/**
	 * @param modbusInfo
	 * @param address
	 * @param cCode
	 * @param cmdCode
	 * @param _PDT
	 * @throws Exception
	 */
	public static void setProtocalsAuto(ModbusInfo modbusInfo,byte[] address,byte[] cCode,byte[] cmdCode,byte[] _PDT) throws Exception{
		// 1,devId,cCode,cmdCode
		modbusInfo.setAddress(address);//设备ID
		modbusInfo.setcCode(cCode);// 广播
		modbusInfo.setCmdCode(cmdCode);
		// 2,Pdt部分
		modbusInfo.setPdt(_PDT);
	}
	public static void main(String[] a) {
		//testOpenLight();
		//testSendDown(new MessageVO()); //下发例子
	}

	 

	/**
	 *  以下TEST  已通过验证
	 *  下发VO ==>报文
	 *  
	 * @return
	 */
	public static <T> boolean testSendDown(MessageVO<T> messageVO) {
		boolean excuSeccess = true;
		String response = "";
		ByteBuf byteBuf = null;
		try {
			System.out.println("=====>>> 构造 关灯 报文...===============");
			 
			//680000000000016803094200000000000001002016
			ModbusInfo modbusInfo_42h = new ModbusInfo();
			// 1,devId,cCode,cmdCode
			modbusInfo_42h.setAddress(ConverUtil.hexStrToByteArr("000000000001"));//设备ID
			modbusInfo_42h.setcCode(ConverUtil.hexStrToByteArr("03"));// 广播
			modbusInfo_42h.setCmdCode(ConverUtil.hexStrToByteArr("42"));
			
			// 2,Pdt部分
			String nodeID = "000000000001"; //无节点？
			String operator_AB = "01"; 		// 01H-a灯 02H -B灯 03H -A,B灯
			int light_value = 0;       		// 范围00H~C8H(十进制：0~200)，对应亮度为0~100%
			byteBuf = Unpooled.buffer(8); 	// 必须等于要拼接的内容的长度，否则byteBuf.array()得到的长度就不准确
			byteBuf.writeBytes(ConverUtil.hexStrToByteArr(nodeID));
			byteBuf.writeBytes(ConverUtil.hexStrToByteArr(operator_AB));
			byteBuf.writeByte(light_value); 
			if (byteBuf.hasArray()) {
				byte[] aa = byteBuf.array();
				System.out.println("pdt.lenght = " + aa.length);
				System.out.println("pdt = " + ConverUtil.convertByteToHexString(aa));
				modbusInfo_42h.setPdt(aa); 		// 内容：计算nodeId + operator_AB(A/B/所有) + 调光值
			}
			if(byteBuf != null)ReferenceCountUtil.release(byteBuf);
			
			// 3,长度
			modbusInfo_42h.resetLength();  
			// 4,CRC计算
			modbusInfo_42h.resetCrc();
			 
			logger.info("     >>>应该输入=680000000000016803094200000000000001002016");
			logger.info("     >>>实际输入=" + ConverUtil.convertByteToHexString(modbusInfo_42h.getNewFullData()));
			System.out.println("====>>>init ModbusInfo :====");
			System.out.println(modbusInfo_42h.toString());
			System.out.println("====<<<===");
			  
		} catch (Exception e) {
			e.printStackTrace();
			if(byteBuf != null)ReferenceCountUtil.release(byteBuf);
		}
		return excuSeccess;

	
	}

	/**
	 * 是否已在配置中
	 */
	public static final Map<String,Boolean> plc_config_threadruning_flag = new HashMap<String,Boolean>();
	public static void init1(ChannelHandlerContext ctx, ModbusInfo modbusInfo) {
		if(PlcProtocolsUtils.plc_config_threadruning_flag!=null  
				&& PlcProtocolsUtils.plc_config_threadruning_flag.get(modbusInfo.getAddress_str()) != null
				&& PlcProtocolsUtils.plc_config_threadruning_flag.get(modbusInfo.getAddress_str()) == true
				){
			logger.warn("设备【"+modbusInfo.getAddress_str()+"】已存在运行的配置线程,退出!" + loggerBaseInfo(modbusInfo));
			return;
		}
		plc_config_threadruning_flag.put(modbusInfo.getAddress_str(), true);//处理完毕后设置为false
		
		// 已知入参：plc_sn 其他入参应该都是从设备信息里面关联得到
		// 1 集中器配置：向集中器写入经纬度信息
		init1_config_jzq(ctx,modbusInfo.getAddress_str()); 
		// 2 设置集中器时间：手动同步集中器时间
		//init2_setTime();
		//init3_cleanNode();
		//init4_delFlash();
		// init5_startGroupAtuo(ctx,modbusInfo);
		 

		//gloable.put(modbusInfo.getAddress_str() + "_config", "1");// 设置配置状态为已配置       1-已配置 0-未配置 ,空值也是未配置
		plc_config_threadruning_flag.put(modbusInfo.getAddress_str(), false);//处理完毕后设置为false
	}

	public static boolean init1_config_jzq(ChannelHandlerContext ctx, String plc_SN ) {
		boolean excuSeccess = true;
		//String cmdCode = "8E";
		try {
			//	获取设备信息   通过chennelID  ==> devinfo
			Map<String, String> def_attributers = IotInfoConstant.allDevInfo.get(getPort(ctx.channel())).get(plc_SN + "_defAttribute");//从自定义的字段里面获取值

			logger.info("=====>>>集中器配置...===============");
			ModbusInfo modbusInfo_8E = new ModbusInfo();
			modbusInfo_8E.setAddress(ConverUtil.hexStrToByteArr(plc_SN));
			modbusInfo_8E.setcCode(ConverUtil.hexStrToByteArr("00"));  
			modbusInfo_8E.setCmdCode(ConverUtil.hexStrToByteArr("8E")); 
			// 获取设备信息  构造PDT  
			/*
				 名称	说明                                                                                       	长度
				经度	取小数后2位(-180.00 - +180.00) 再*100 整型	2 Bs     如：+9068
				纬度	取小数后2位 (-90.00 - +90.00) 再*100整型	    2 Bs     如：-XXXX
				时区	-11 - +12整型	                            1 B
				短信中心号	如: +8613010888500 字符串型	14 Bs    --不考虑
				管理员1	如:13812345678 字符串型	11 Bs     --不考虑
				管理员2	如:13812345678 字符串型	11 Bs     --不考虑
				操作员1	如:13812345678 字符串型	11 Bs     --不考虑
				操作员2	如:13812345678 字符串型	11 Bs     --不考虑
				操作员3	如:13812345678 字符串型	11 Bs     --不考虑
				短信操作回复	0 否  1是	1 B     			  --不考虑
				光控时段	前2Byte：开始 时分           2Bs                     
				                     后2Byte：结束 时分 	2 Bs
			*/
	    	String plc_cfg_step1_longitude = def_attributers.get(IotInfoConstant.dev_plc_cfg_longitude);  
	    	String plc_cfg_step1_latitude = def_attributers.get(IotInfoConstant.dev_plc_cfg_latitude);  
	    	String plc_cfg_step1_sq= def_attributers.get(IotInfoConstant.dev_plc_cfg_sq); //"-8"; //时区        转化成整形表示
	    	String plc_cfg_step1_gksd_start= def_attributers.get(IotInfoConstant.dev_plc_cfg_gksd_start); //"8:10"; //光控时段-开始时分
	    	int gksd_start_h = Integer.parseInt(plc_cfg_step1_gksd_start.split(":")[0]);
	    	int gksd_start_s = Integer.parseInt(plc_cfg_step1_gksd_start.split(":")[1]);
	    	String plc_cfg_step1_gksd_end=  def_attributers.get(IotInfoConstant.dev_plc_cfg_gksd_end); //"8:10"; //光控时段  结束时分
	    	int gksd_end_h = Integer.parseInt(plc_cfg_step1_gksd_end.split(":")[0]);
	    	int gksd_end_s = Integer.parseInt(plc_cfg_step1_gksd_end.split(":")[1]);
 	    	
			byte[] temp1 = new byte[2]; 
			temp1 = ByteUtils.varIntToByteArray(9068); //经度
			byte[] temp2 = new byte[2]; 
			temp2 = ByteUtils.varIntToByteArray(-2536); //纬度
			byte[] temp3 = new byte[1];
			temp3 = ByteUtils.varIntToByteArray(-10);  //时区
			byte[] temp4_10 = new byte[14+11*5+1];
			for(int p=0;p<temp4_10.length;p++){
				//temp4_10[p]= 0;
			}
			byte[] temp11_0 = new byte[1];
			byte[] temp11_1 = new byte[1];
			temp11_0 = ByteUtils.varIntToByteArray(gksd_start_h); //时
			temp11_1 = ByteUtils.varIntToByteArray(gksd_start_s);   //分
			byte[] temp12_0 = new byte[1];   //时
			byte[] temp12_1 = new byte[1];   //时
			temp12_0 = ByteUtils.varIntToByteArray(gksd_end_h); //时
			temp12_1 = ByteUtils.varIntToByteArray(gksd_end_s);   //分
			 
			int len = temp1.length + temp2.length + temp3.length + temp4_10.length + 2 + 2;   	
	        ByteBuf byteBuf = Unpooled.buffer(len);
	        byteBuf.writeBytes(temp1).writeBytes(temp2).writeBytes(temp3).writeBytes(temp4_10).writeBytes(temp11_0).writeBytes(temp11_1)
	         			.writeBytes(temp12_0).writeBytes(temp12_1); //CRC计算，根据CRC所在报文位置--》headStart所有的数据
	        byte[] temp = byteBuf.array();
	        if(byteBuf != null)ReferenceCountUtil.release(byteBuf);
			modbusInfo_8E.setPdt(temp);
 			//	响应
			logger.info(">>>响应设备,cmdCode=8E  ,byteMsg=" + ConverUtil.convertByteToHexString(modbusInfo_8E.getNewFullData()) );
			logger.info(">>>响应设备,cmdCode=8E  ,byteMsg.len=" + modbusInfo_8E.getNewFullData().length );
			//ctxWriteByte(ctx, modbusInfo_8E.getNewFullData(),"8E");
		} catch (Exception e) {
			logger.error(">>>PlcProtocolsUtils::init()::init5_startGroupAtuo(),address=" + plc_SN + ",异常 ", e);
			excuSeccess = false;
			try {
				// ctx.write(ConverUtil.hexStrToByteArr(rp));
			//	ctxWriteStr(ctx, response);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				logger.error(">>>PlcProtocolsUtils::init()::login::ctxWriteStr(),address=" + plc_SN + ",异常 ", e1);
			} // 响应成功
		}
		return excuSeccess;
	
	}

	public static void init2_setTime() {

	}

	public static void init3_cleanNode() {

	}

	public static void init4_delFlash() {

	}

	/**
	 * 
	 * 68 00 00 00 00 00 01 68 00 02 62 02 37 16
	 * 
	 * @param ctx
	 * @param modbusInfo
	 * @return
	 */
	public static boolean init5_startGroupAtuo(ChannelHandlerContext ctx, ModbusInfo modbusInfo) {
		boolean excuSeccess = true;
		String response = "";
		try {
			//	获取设备信息
			InetSocketAddress insocket = (InetSocketAddress) ctx.channel().localAddress();
			int port = insocket.getPort();
			Map<String, String> attributers = IotInfoConstant.allDevInfo.get(port).get(modbusInfo.getAddress_str() + "_defAttribute");

			logger.info("=====>>>开始组网...===============");
			ModbusInfo modbusInfo_62h = new ModbusInfo();
			
			// PDT 组网个数
			byte[] groupNum = new byte[1];
			int x = Integer.parseInt(attributers.get(IotInfoConstant.dev_plc_cfg_step5_groupAtuo));
			logger.info("     >>>组网个数 = " + x);
			groupNum[0] = (byte) x;  
			modbusInfo_62h.setPdt(groupNum);
			// 控制码
			modbusInfo_62h.setcCode(ConverUtil.hexStrToByteArr("00")); // 00H
			// 操作码
			modbusInfo_62h.setCmdCode(ConverUtil.hexStrToByteArr("62"));// 62H
			// CRC校验码
			modbusInfo_62h.resetCrc();
			// 重新计算长度
			modbusInfo_62h.resetLength();
 			//	响应
			ctxWriteByte(ctx, modbusInfo_62h.getNewFullData(),"62");
		} catch (Exception e) {
			logger.error(">>>PlcProtocolsUtils::init()::init5_startGroupAtuo(),msg=" + response + ",异常 ", e);
			excuSeccess = false;
			response = "68" + modbusInfo.getAddress_str() + "688002f0024416";
			try {
				// ctx.write(ConverUtil.hexStrToByteArr(rp));
				ctxWriteStr(ctx, response ,"62");
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				logger.error(">>>PlcProtocolsUtils::init()::login::ctxWriteStr(),msg=" + response + ",异常 ", e1);
			} // 响应成功
		}
		return excuSeccess;
	}

}
