package com.hzyw.iot.utils;

import com.hzyw.iot.vo.dataaccess.MessageVO;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.Calendar;
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
	
	
	//直接定义在内存:<iot_init_Configkey,<field=initconfig+100+cmdCode,json>> 
	public static final Map<String, Map<String, String>> iot_init_Config_chache = new HashMap<String, Map<String, String>>();
	 
	
	public static void ctxWriteByte_ChannelFuture(Channel channel, byte[] msg, String type,final int step) throws Exception {
		try {
			logger.info(">>>响应设备,cmdCode="+type+",byteMsg=" + ConverUtil.convertByteToHexString(msg) );
			channel.writeAndFlush(msg).addListener((ChannelFutureListener) future -> { //监听执行结果！
                if (future.isSuccess()) {
                	logger.info("ok");
                	//如果成功，新增或更新操作步骤
                	 Map<String, String> it = new HashMap<String, String>
                	iot_init_Config_chache.put("iot_init_Configkey", value)
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
		
		
		//判断控制码，根据控制码来引导配置流程
		if("8e".equals(modbusInfo.getCmdCode_str().toLowerCase()) 
				&& "80".equals(modbusInfo.getcCode_str().toLowerCase())){
			//init1 集中器配置参数
			initX_config_jzq_response(ctx, modbusInfo);
		}else if("8c".equals(modbusInfo.getCmdCode_str().toLowerCase()) 
				&& "80".equals(modbusInfo.getcCode_str().toLowerCase())){
			//init2  设置时钟
			initX_config_jzq_response(ctx, modbusInfo);
		}else if("99".equals(modbusInfo.getCmdCode_str().toLowerCase()) 
				&& "80".equals(modbusInfo.getcCode_str().toLowerCase())){
			//init3  删除所有节点
			initX_config_jzq_response(ctx, modbusInfo);
		}else if("8c".equals(modbusInfo.getCmdCode_str().toLowerCase()) 
				&& "80".equals(modbusInfo.getcCode_str().toLowerCase())){
			//init4  删除FLASH节点
			initX_config_jzq_response(ctx, modbusInfo);
		}else if("8c".equals(modbusInfo.getCmdCode_str().toLowerCase()) 
				&& "80".equals(modbusInfo.getcCode_str().toLowerCase())){
			//init5   开始组网
			initX_config_jzq_response(ctx, modbusInfo);
		}else{
			//发起init1请求
			init1_config_jzq(ctx,modbusInfo.getAddress_str());
			//成功
			//失败，退出
		}
		
		// 已知入参：plc_sn 其他入参应该都是从设备信息里面关联得到
		// 1 集中器配置：向集中器写入经纬度信息
		//init1_config_jzq(ctx,modbusInfo.getAddress_str()); 
		
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
		final ModbusInfo modbusInfo_8E_00 = new ModbusInfo(); //8e表示命令码 00表示请求  80表示响应
		try {
			//	获取设备信息   通过chennelID  ==> devinfo
			Map<String, String> def_attributers = IotInfoConstant.allDevInfo.get(getPort(ctx.channel())).get(plc_SN + "_defAttribute");//从自定义的字段里面获取值

			logger.info("=====>>>initstep(" + plc_SN + ")集中器配置参数...===============");
			modbusInfo_8E_00.setAddress(ConverUtil.hexStrToByteArr(plc_SN));
			modbusInfo_8E_00.setcCode(ConverUtil.hexStrToByteArr("00"));  
			modbusInfo_8E_00.setCmdCode(ConverUtil.hexStrToByteArr("8e")); 
			// 获取设备信息  构造PDT  
			/*
				 名称	说明                                                                                       	长度
				经度	取小数后2位(-180.00 - +180.00) 再*100 整型	2 Bs     如：+9068
				纬度	取小数后2位 (-90.00 - +90.00) 再*100整型	    2 Bs     如：-XXXX
				时区	-11 - +12整型	                            1 B
				短信中心号	如: +8613010888500 字符串型	14 Bs    --不考虑
				管理员1	如:13812345678 字符串型	11 Bs        --不考虑
				管理员2	如:13812345678 字符串型	11 Bs    	 --不考虑
				操作员1	如:13812345678 字符串型	11 Bs     	 --不考虑
				操作员2	如:13812345678 字符串型	11 Bs        --不考虑
				操作员3	如:13812345678 字符串型	11 Bs        --不考虑
				短信操作回复	0 否  1是			1 B     	 --不考虑
				光控时段	前2Byte：开始 时分                    2Bs                     
				                     后2Byte：结束 时分 	    2 Bs
			*/
	    	String plc_cfg_step1_longitude = def_attributers.get(IotInfoConstant.dev_plc_cfg_longitude);  //经度
	    	String plc_cfg_step1_latitude = def_attributers.get(IotInfoConstant.dev_plc_cfg_latitude);  //纬度
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
			/*for(int p=0;p<temp4_10.length;p++){ //不需要给值，报文的相应位置默认给0
				temp4_10[p]= 0;
			}*/
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
	        modbusInfo_8E_00.setPdt(temp);
 			//	响应
			logger.info(">>>请求设备,cmdCode=8e_00  ,byteMsg=" + ConverUtil.convertByteToHexString(modbusInfo_8E_00.getNewFullData()) );
			logger.info(">>>请求设备,cmdCode=8e_00  ,byteMsg.len=" + modbusInfo_8E_00.getNewFullData().length );
			//ctxWriteByte(ctx, modbusInfo_8E.getNewFullData(),"8E");
			ctx.channel().writeAndFlush(modbusInfo_8E_00.getNewFullDataWithByteBuf()).addListener((ChannelFutureListener) future -> {  
                if (future.isSuccess()) {
            		logger.info(">>>initstep("+modbusInfo_8E_00.getAddress_str()+")请求设备成功！,cmdCode=8e_00,byteMsg=" 
							+ ConverUtil.convertByteToHexString(modbusInfo_8E_00.getNewFullData()));
                } else {
                	logger.info(">>>initstep("+modbusInfo_8E_00.getAddress_str()+")请求设备失败！,cmdCode=8e_00,byteMsg=" 
							+ ConverUtil.convertByteToHexString(modbusInfo_8E_00.getNewFullData()));
                }
            });
		} catch (Exception e) {
			logger.error(">>>initstep(" + modbusInfo_8E_00.getAddress_str() + ")响应设备,cmdCode=8e_00,exception ！", e);
			excuSeccess = false;
		}
		return excuSeccess;
	
	}
	
	
	public static boolean initX_config_jzq_response(ChannelHandlerContext ctx, ModbusInfo modbusInfo ) {
		boolean excuSeccess = true;
		//String cmdCode = "8E";
		String cCode = modbusInfo.getcCode_str();
		String cCmdCode = modbusInfo.getCmdCode_str();
		
		try {
			logger.info("=====initstep(" + modbusInfo.getAddress_str() + ")>>>集中器配置响应...===============");
			logger.info(">>>initstep(" + modbusInfo.getAddress_str() + ")响应设备,cmdCode="+cCmdCode+",cCode="+cCode+",byteMsg="
					+ ConverUtil.convertByteToHexString(modbusInfo.getNewFullData()));
			//PDT  ack
			/*
				01H：集中器成功受理。
				02H：命令或数据格式无效。 
				03H：集中器忙。
			*/
	    	String hex_ptd = ConverUtil.convertByteToHexString(modbusInfo.getPdt());  //ack
	    	if("01".equals(modbusInfo.getPdt())){
				logger.info(">>>initstep("+modbusInfo.getAddress_str()+")响应设备,cmdCode="+cCmdCode+",cCode="+cCode+",集中器反馈：集中器成功受理!");
				if("8e".equals(cCmdCode) && "80".equals(cCode)){
					//请求设备 设置时间
					init2_config_setTime(ctx,modbusInfo.getAddress_str());
				}
				if("8c".equals(cCmdCode) && "80".equals(cCode)){
					//请求设备，删除所有节点
					init3_config_cleanNode(ctx,modbusInfo.getAddress_str());
				}
				if("99".equals(cCmdCode) && "80".equals(cCode)){
					//请求设备，删除所有节点
					init4_config_delFlash(ctx,modbusInfo.getAddress_str());
				}
				if("69".equals(cCmdCode) && "80".equals(cCode)){
					//请求设备，开始组网
					init5_startGroupAtuo(ctx,modbusInfo.getAddress_str());
				}
				if("62".equals(cCmdCode) && "80".equals(cCode)){ //组网成功 ，直接停止组网 或者说失败的时候才需要手动的去停止？ 这个需要沟通下
					//请求设备，开始组网
					init6_stopGroupAtuo(ctx,modbusInfo.getAddress_str());
				}
				 
				
	    	}else if("02".equals(modbusInfo.getPdt())){
	    		logger.info(">>>initstep("+modbusInfo.getAddress_str()+")响应设备,cmdCode="+cCmdCode+",cCode="+cCode+",集中器反馈：命令或数据格式无效");
	    	}else if("03".equals(modbusInfo.getPdt())){
	    		logger.info(">>>initstep("+modbusInfo.getAddress_str()+")响应设备,cmdCode="+cCmdCode+",cCode="+cCode+",集中器反馈：集中器忙！");
	    	}else{
				logger.info(">>>initstep(" + modbusInfo.getAddress_str() + ")响应设备,cmdCode="+cCmdCode+",cCode="+cCode+",集中器反馈：返回数据异常！");
	    	}
		} catch (Exception e) {
			logger.error(">>>initstep(" + modbusInfo.getAddress_str() + ")响应设备,cmdCode="+cCmdCode+",cCode="+cCode+",exception1！", e);
			excuSeccess = false;
		}
		return excuSeccess;
	
	}

	public static boolean init2_config_setTime(ChannelHandlerContext ctx,String plc_SN) {
		boolean excuSeccess = true;
		final ModbusInfo modbusInfo_8c_00 = new ModbusInfo();
		try {
			//	获取设备信息   通过chennelID  ==> devinfo
			Map<String, String> def_attributers = IotInfoConstant.allDevInfo.get(getPort(ctx.channel())).get(plc_SN + "_defAttribute");//从自定义的字段里面获取值

			logger.info("=====>>>initstep(" + plc_SN+ ")集中器设置时钟...===============");
			modbusInfo_8c_00.setAddress(ConverUtil.hexStrToByteArr(plc_SN));
			modbusInfo_8c_00.setcCode(ConverUtil.hexStrToByteArr("00"));  
			modbusInfo_8c_00.setCmdCode(ConverUtil.hexStrToByteArr("8c")); 
			//   构造PDT 7个字节 
			/*
				 年，月，日，周，时，分，秒
			*/
			long timestamp = System.currentTimeMillis();//输入定为时间戳
			Calendar ca = Calendar.getInstance();
			ca.setTimeInMillis(timestamp);
			int hour = ca.get(Calendar.DATE);  //时
			int min = ca.get(Calendar.MINUTE); //分
			int sec = ca.get(Calendar.SECOND);//秒
			int week = ca.get(Calendar.DAY_OF_WEEK);//周 默认从1开始  是否要-1？
			int day = ca.get(Calendar.DAY_OF_MONTH);//日
			int month = ca.get(Calendar.MONTH);//月   默认0开始  是否要 +1？
			int year = ca.get(Calendar.YEAR);//年
 	    	
			byte[] temp1 = new byte[7]; 
			temp1[0] = (byte)year; 
			temp1[1] = (byte)month; 
			temp1[2] = (byte)day; 
			temp1[3] = (byte)week; 
			temp1[4] = (byte)hour; 
			temp1[5] = (byte)min; 
			temp1[6] = (byte)year; 
			modbusInfo_8c_00.setPdt(temp1);

			ctxWriteAndFlush(ctx,modbusInfo_8c_00);
		} catch (Exception e) {
			logger.error(">>>initstep(" + modbusInfo_8c_00.getAddress_str() + ")请求设备,,cmdCode="+modbusInfo_8c_00.getCmdCode_str()
        				+"ccode="+modbusInfo_8c_00.getcCode_str()+",exception1！", e);
			excuSeccess = false;
		}
		return excuSeccess;
	}

  
	public static boolean init3_config_cleanNode(ChannelHandlerContext ctx, String plc_SN) {
		boolean excuSeccess = true;
		final ModbusInfo modbusInfo = new ModbusInfo();
		try {
			//	删除所有节点，只需要指定集中器ID即可

			logger.info("=====>>>initstep(" + plc_SN+ ")集中器设置时钟...===============");
			modbusInfo.setAddress(ConverUtil.hexStrToByteArr(plc_SN));
			modbusInfo.setcCode(ConverUtil.hexStrToByteArr("03"));  //01删某个ID，02删除一组  03删除所有
			modbusInfo.setCmdCode(ConverUtil.hexStrToByteArr("99")); 
			//   构造PDT 6个字节 
			byte[] temp1 = new byte[6]; 
			modbusInfo.setPdt(temp1);

			ctxWriteAndFlush(ctx,modbusInfo);
		} catch (Exception e) {
			logger.error(">>>initstep(" + modbusInfo.getAddress_str() + ")请求设备,,cmdCode="+modbusInfo.getCmdCode_str()
        				+"ccode="+modbusInfo.getcCode_str()+",exception1！", e);
			excuSeccess = false;
		}
		return excuSeccess;
	
	}
	
	public static void ctxWriteAndFlush(ChannelHandlerContext ctx,  ModbusInfo modbusInfo) throws Exception{
		ctx.channel().writeAndFlush(modbusInfo.getNewFullDataWithByteBuf()).addListener((ChannelFutureListener) future -> {  
            if (future.isSuccess()) {
        		logger.info(">>>initstep("+modbusInfo.getAddress_str()+")请求设备成功！,cmdCode="+modbusInfo.getCmdCode_str()
        				+"ccode="+modbusInfo.getcCode_str()+",byteMsg=" + ConverUtil.convertByteToHexString(modbusInfo.getNewFullData()));
            } else {
            	logger.info(">>>initstep("+modbusInfo.getAddress_str()+")请求设备失败！,cmdCode="+modbusInfo.getCmdCode_str()
        				+"ccode="+modbusInfo.getcCode_str()+",byteMsg=" + ConverUtil.convertByteToHexString(modbusInfo.getNewFullData()));
            }
        });
	}

	public static boolean init4_config_delFlash(ChannelHandlerContext ctx, String plc_SN) {
		boolean excuSeccess = true;
		final ModbusInfo modbusInfo = new ModbusInfo();
		try {
			//	删除所有节点，只需要指定集中器ID即可
			logger.info("=====>>>initstep(" + plc_SN+ ")删除FLASH节点...===============");
			modbusInfo.setAddress(ConverUtil.hexStrToByteArr(plc_SN));
			modbusInfo.setcCode(ConverUtil.hexStrToByteArr("00")); 
			modbusInfo.setCmdCode(ConverUtil.hexStrToByteArr("69")); 
			byte[] temp1 = new byte[0]; 
			modbusInfo.setPdt(temp1);//注意这里，没有PDT的时候 是否需要给值0

			ctxWriteAndFlush(ctx,modbusInfo);
		} catch (Exception e) {
			logger.error(">>>initstep(" + modbusInfo.getAddress_str() + ")请求设备,cmdCode="+modbusInfo.getCmdCode_str()
        				+"ccode="+modbusInfo.getcCode_str()+",exception1！", e);
			excuSeccess = false;
		}
		return excuSeccess;
	
	}
	 

	/**
	 * 
	 * 68 00 00 00 00 00 01 68 00 02 62 02 37 16
	 * 
	 * @param ctx
	 * @param modbusInfo
	 * @return
	 */
	public static boolean init5_startGroupAtuo(ChannelHandlerContext ctx, String plc_SN) {
		boolean excuSeccess = true;
		final ModbusInfo modbusInfo = new ModbusInfo();
		try {
			//	获取设备信息
			InetSocketAddress insocket = (InetSocketAddress) ctx.channel().localAddress();
			int port = insocket.getPort();
			Map<String, String> attributers = IotInfoConstant.allDevInfo.get(port).get(modbusInfo.getAddress_str() + "_defAttribute");
					
			logger.info("=====>>>initstep(" + plc_SN+ ") 开始组网...===============");
			modbusInfo.setAddress(ConverUtil.hexStrToByteArr(plc_SN));
			modbusInfo.setcCode(ConverUtil.hexStrToByteArr("00")); 
			modbusInfo.setCmdCode(ConverUtil.hexStrToByteArr("62")); 
			// PDT 组网个数
			byte[] groupNum = new byte[1];
			int x = Integer.parseInt(attributers.get(IotInfoConstant.dev_plc_cfg_step5_groupAtuo));
			logger.info("     >>>组网个数 = " + x);
			groupNum[0] = (byte) x;  
			modbusInfo.setPdt(groupNum);
			  
			ctxWriteAndFlush(ctx,modbusInfo);
		} catch (Exception e) {
			logger.error(">>>initstep(" + modbusInfo.getAddress_str() + ")请求设备,cmdCode="+modbusInfo.getCmdCode_str()
        				+"ccode="+modbusInfo.getcCode_str()+",exception1！", e);
			excuSeccess = false;
		}
		return excuSeccess;
		
	}
	
	public static boolean init6_stopGroupAtuo(ChannelHandlerContext ctx, String plc_SN) {
		boolean excuSeccess = true;
		final ModbusInfo modbusInfo = new ModbusInfo();
		try {
			logger.info("=====>>>initstep(" + plc_SN+ ") 停止组网...==63,00=============");
			modbusInfo.setAddress(ConverUtil.hexStrToByteArr(plc_SN));
			modbusInfo.setcCode(ConverUtil.hexStrToByteArr("00")); 
			modbusInfo.setCmdCode(ConverUtil.hexStrToByteArr("63")); 
			// PDT 
			byte[] temp1 = new byte[0]; 
			modbusInfo.setPdt(temp1);
			ctxWriteAndFlush(ctx,modbusInfo);
		} catch (Exception e) {
			logger.error(">>>initstep(" + modbusInfo.getAddress_str() + ")请求设备,cmdCode="+modbusInfo.getCmdCode_str()
        				+"ccode="+modbusInfo.getcCode_str()+",exception1！", e);
			excuSeccess = false;
		}
		return excuSeccess;
		
	}
	public static boolean init7_saveNode(ChannelHandlerContext ctx, String plc_SN) {  //未完成。。。。。
		boolean excuSeccess = true;
		final ModbusInfo modbusInfo = new ModbusInfo();
		try {
			logger.info("=====>>>initstep(" + plc_SN+ ") 存储节点...==66,00=============");
			modbusInfo.setAddress(ConverUtil.hexStrToByteArr(plc_SN));
			modbusInfo.setcCode(ConverUtil.hexStrToByteArr("00")); 
			modbusInfo.setCmdCode(ConverUtil.hexStrToByteArr("66")); 
			// PDT 
			byte[] temp1 = new byte[0]; 
			modbusInfo.setPdt(temp1);
			ctxWriteAndFlush(ctx,modbusInfo);
		} catch (Exception e) {
			logger.error(">>>initstep(" + modbusInfo.getAddress_str() + ")请求设备,cmdCode="+modbusInfo.getCmdCode_str()
        				+"ccode="+modbusInfo.getcCode_str()+",exception1！", e);
			excuSeccess = false;
		}
		return excuSeccess;
		
	}
	public static boolean init8_sendDownNode(ChannelHandlerContext ctx, String plc_SN) {
		boolean excuSeccess = true;
		final ModbusInfo modbusInfo = new ModbusInfo();
		try {
			logger.info("=====>>>initstep(" + plc_SN+ ") 下发节点...==96,00=============");
			modbusInfo.setAddress(ConverUtil.hexStrToByteArr(plc_SN));
			modbusInfo.setcCode(ConverUtil.hexStrToByteArr("00")); 
			modbusInfo.setCmdCode(ConverUtil.hexStrToByteArr("96")); 
			// PDT 
			byte[] temp1 = new byte[0]; 
			modbusInfo.setPdt(temp1);
			ctxWriteAndFlush(ctx,modbusInfo);
		} catch (Exception e) {
			logger.error(">>>initstep(" + modbusInfo.getAddress_str() + ")请求设备,cmdCode="+modbusInfo.getCmdCode_str()
        				+"ccode="+modbusInfo.getcCode_str()+",exception1！", e);
			excuSeccess = false;
		}
		return excuSeccess;
		
	}
	public static boolean ini9_configNode(ChannelHandlerContext ctx, String plc_SN) {
		boolean excuSeccess = true;
		final ModbusInfo modbusInfo = new ModbusInfo();
		try {
			logger.info("=====>>>initstep(" + plc_SN+ ") 配置节点...==98,00=============");
			modbusInfo.setAddress(ConverUtil.hexStrToByteArr(plc_SN));
			modbusInfo.setcCode(ConverUtil.hexStrToByteArr("00")); 
			modbusInfo.setCmdCode(ConverUtil.hexStrToByteArr("98")); 
			// PDT 
			byte[] temp1 = new byte[0]; 
			modbusInfo.setPdt(temp1);
			ctxWriteAndFlush(ctx,modbusInfo);
		} catch (Exception e) {
			logger.error(">>>initstep(" + modbusInfo.getAddress_str() + ")请求设备,cmdCode="+modbusInfo.getCmdCode_str()
        				+"ccode="+modbusInfo.getcCode_str()+",exception1！", e);
			excuSeccess = false;
		}
		return excuSeccess;
		
	}

}
