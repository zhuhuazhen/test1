package com.hzyw.iot.utils;

import com.hzyw.iot.vo.dataaccess.MessageVO;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hzyw.iot.netty.channelhandler.ChannelManagerHandler;
import com.hzyw.iot.util.ByteUtils;
import com.hzyw.iot.util.constant.ConverUtil;
import com.hzyw.iot.vo.dc.GlobalInfo;
import com.hzyw.iot.vo.dc.ModbusInfo;

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
public class PlcProtocolsUtilsTest {
	private static final Logger logger = LoggerFactory.getLogger(PlcProtocolsUtilsTest.class);
	public static Map<String, String> gloable = new HashMap<String, String>();

	 

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
		testGetBW(); //手工构造一条没有PDT的报文
		init1_config_jzq(null,"000000000100");
		init8_sendNode();//下发节点
		ini9_configNode(null,"000000000100"); //配置节点
		getSplSTR();
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
			 
			logger.info("     >>>您要的报文=" + ConverUtil.convertByteToHexString(modbusInfo_42h.getNewFullData()));
			System.out.println("====>>>init ModbusInfo :====");
			System.out.println(modbusInfo_42h.toString());
			System.out.println("====<<<===");
			  
		} catch (Exception e) {
			e.printStackTrace();
			if(byteBuf != null)ReferenceCountUtil.release(byteBuf);
		}
		return excuSeccess;

	
	}
	
	public static <T> boolean testGetBW() {  //无PDT的报文
		boolean excuSeccess = true;
		String response = "";
		ByteBuf byteBuf = null;
		try {
			System.out.println("=====>>> 构造 没有PDT的报文 报文...===============");
  			ModbusInfo modbusInfo_42h = new ModbusInfo();
			// 1,devId,cCode,cmdCode
			modbusInfo_42h.setAddress(ConverUtil.hexStrToByteArr("000000000100"));//设备ID
			modbusInfo_42h.setcCode(ConverUtil.hexStrToByteArr("80"));// 广播
			modbusInfo_42h.setCmdCode(ConverUtil.hexStrToByteArr("f1"));
			
			// 2,Pdt部分
			 
			byte[] aa = new byte[0];
			modbusInfo_42h.setPdt(aa); 	
			modbusInfo_42h.resetLength();  
			modbusInfo_42h.resetCrc();
			
			//logger.info("     >>>---------=68 00 00 00 00 00 01 68 00 01 63 35 16");
			logger.info("     >>>您要的报文=" + ConverUtil.convertByteToHexString(modbusInfo_42h.getNewFullData()));
			System.out.println("====>>>init ModbusInfo :====");
			System.out.println(modbusInfo_42h.toString());
			System.out.println("====<<<===");
			  
		} catch (Exception e) {
			e.printStackTrace();
		}
		return excuSeccess;

	
	}
	public static void getSplSTR(){
		String tst = "680000000001006800508e236cf618f600000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000080a08328e16";
		StringBuffer sp = new StringBuffer();
		for(int i=0 ;i<tst.length();i++){
			if(i%2 == 0){
				sp.append(","+tst.charAt(i));
			}else{
				sp.append( tst.charAt(i));
			}
		}
		System.out.println("========" +sp.toString());
	}

	public static <T> boolean init8_sendNode() { //下发节点
		boolean excuSeccess = true;
		String response = "000000000100";
		ByteBuf byteBuf = null;
		try {
			ModbusInfo modbusInfo = new ModbusInfo();
			String plc_SN = "000000000100";
			logger.info("=====>>>initstep(" + plc_SN+ ") 下发节点...==96,00=============");
			modbusInfo.setAddress(ConverUtil.hexStrToByteArr(plc_SN));
			modbusInfo.setcCode(ConverUtil.hexStrToByteArr("03")); 
			modbusInfo.setCmdCode(ConverUtil.hexStrToByteArr("96")); 
			
			// 获取设备信息   通过chennelID  ==> devinfo
		 
			
			// PDT  节点列表
			int pdtLen = 8 ;//一个节点占8个字节
			//if(nodelist != null && nodelist.size() > 0){
				byte[] temp1 = new byte[pdtLen];
				if(true){
					 byteBuf = Unpooled.buffer(8);
					//for(int i = 0; i < nodelist.size(); i++){
 						int len = 8;
						String nodeid = "0000020004ee";  //6
						int groupid = 1;                 //1
						String devCode = "12"; //1
				        byteBuf.writeBytes(ConverUtil.hexStrToByteArr(nodeid))  //节点ID
				        		.writeBytes(ByteUtils.varIntToByteArray(groupid)) //组号
				        		.writeBytes(ConverUtil.hexStrToByteArr(devCode));  //设备码
					//}
					  temp1 = byteBuf.array();
			        if(byteBuf != null)ReferenceCountUtil.release(byteBuf);
				}
				modbusInfo.setPdt(temp1);
				 
			//}else{
				//
				 
			//}
			  
			logger.info("     >>>您要的报文=" + ConverUtil.convertByteToHexString(modbusInfo.getNewFullData())); 
			  
		} catch (Exception e) {
			e.printStackTrace();
		}
		return excuSeccess;

	
	}

	public static boolean init1_config_jzq(ChannelHandlerContext ctx, String plc_SN ) {
		boolean excuSeccess = true;
		final ModbusInfo modbusInfo = new ModbusInfo(); //8e表示命令码 00表示请求  80表示响应
		try {
			//	获取设备信息   通过chennelID  ==> devinfo
			//Map<String, String> def_attributers = IotInfoConstant.allDevInfo.get(getPort(ctx.channel())).get(plc_SN + "_defAttribute");//从自定义的字段里面获取值
			logger.info("=====>>>initstep1(" + plc_SN + ")集中器配置参数...===============");
			modbusInfo.setAddress(ConverUtil.hexStrToByteArr(plc_SN));
			modbusInfo.setcCode(ConverUtil.hexStrToByteArr("00"));  
			modbusInfo.setCmdCode(ConverUtil.hexStrToByteArr("8e")); 
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
	    	String plc_cfg_step1_longitude = "9096";  //经度
	    	String plc_cfg_step1_latitude = "4521";  //纬度
	    	String plc_cfg_step1_sq= "+8"; //"-8"; //时区        转化成整形表示
	    	String plc_cfg_step1_gksd_start= "8:10"; //"8:10"; //光控时段-开始时分
	    	int gksd_start_h = Integer.parseInt(plc_cfg_step1_gksd_start.split(":")[0]);
	    	int gksd_start_s = Integer.parseInt(plc_cfg_step1_gksd_start.split(":")[1]);
	    	String plc_cfg_step1_gksd_end=  "8:50"; //"8:10"; //光控时段  结束时分
	    	int gksd_end_h = Integer.parseInt(plc_cfg_step1_gksd_end.split(":")[0]);
	    	int gksd_end_s = Integer.parseInt(plc_cfg_step1_gksd_end.split(":")[1]);
 	    	
			byte[] temp1 = new byte[2]; 
			temp1 = ByteUtils.varIntToByteArray(9068); //经度
			byte[] temp2 = new byte[2]; 
			temp2 = ByteUtils.varIntToByteArray(-2536); //纬度
			byte[] temp3 = new byte[1];
			temp3 = ByteUtils.varIntToByteArray(-10);  //时区
			byte[] temp4_10 = new byte[14+11*5+1];
			for(int p=0;p<temp4_10.length;p++){ //不需要给值，报文的相应位置默认给0
				temp4_10[p]= 0;
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
			logger.info(">>>请求设备,cmdCode=8e_00  , 应该是len=" + len);  
	        ByteBuf byteBuf = Unpooled.buffer(len);
	        byteBuf.writeBytes(temp1).writeBytes(temp2).writeBytes(temp3).writeBytes(temp4_10).writeBytes(temp11_0).writeBytes(temp11_1)
	         			.writeBytes(temp12_0).writeBytes(temp12_1); //CRC计算，根据CRC所在报文位置--》headStart所有的数据
	        byte[] temp = byteBuf.array();
	        if(byteBuf != null)ReferenceCountUtil.release(byteBuf);
	        modbusInfo.setPdt(temp);
 			//	响应
			logger.info(">>>请求设备,cmdCode=8e_00  ,byteMsg=" + ConverUtil.convertByteToHexString(modbusInfo.getNewFullData()) );
			logger.info(">>>请求设备,cmdCode=8e_00  ,byteMsg.len=" + modbusInfo.getNewFullData().length );
			 
			 
			ctxWriteAndFlush(ctx,modbusInfo,"删除FLASH节点");
		} catch (Exception e) {
			logger.error(">>>initstep(" + modbusInfo.getAddress_str() + ")响应设备,cmdCode=8e_00,exception ！", e);
			excuSeccess = false;
		}
		return excuSeccess;
	
	}
	
	/**
	 * 配置过程,指令响应后进入此处理
	 * 
	 * @param ctx
	 * @param modbusInfo
	 * @return
	 */
	 

	public static boolean init2_config_setTime(ChannelHandlerContext ctx,String plc_SN) {
		boolean excuSeccess = true;
		final ModbusInfo modbusInfo_8c_00 = new ModbusInfo();
		try {
			//	获取设备信息   通过chennelID  ==> devinfo

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

			ctxWriteAndFlush(ctx,modbusInfo_8c_00,"集中器设置时钟");
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
			logger.info("=====>>>initstep(" + plc_SN+ ")清除节点...===============");
			modbusInfo.setAddress(ConverUtil.hexStrToByteArr(plc_SN));
			modbusInfo.setcCode(ConverUtil.hexStrToByteArr("03"));  //01删某个ID，02删除一组  03删除所有
			modbusInfo.setCmdCode(ConverUtil.hexStrToByteArr("99")); 
			//   构造PDT 6个字节 
			byte[] temp1 = new byte[6]; 
			modbusInfo.setPdt(temp1);

			ctxWriteAndFlush(ctx,modbusInfo,"清除节点");
		} catch (Exception e) {
			logger.error(">>>initstep(" + modbusInfo.getAddress_str() + ")请求设备,,cmdCode="+modbusInfo.getCmdCode_str()
        				+"ccode="+modbusInfo.getcCode_str()+",exception1！", e);
			excuSeccess = false;
		}
		return excuSeccess;
	
	}
	
	public static void ctxWriteAndFlush(ChannelHandlerContext ctx,  ModbusInfo modbusInfo,String logmsg) throws Exception{
		logger.info(" byteMsg=" + ConverUtil.convertByteToHexString(modbusInfo.getNewFullData()));
		 
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

			ctxWriteAndFlush(ctx,modbusInfo,"删除FLASH节点");
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
	public static boolean init5_CfgNetwork(ChannelHandlerContext ctx, String plc_SN) {
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
			  
			ctxWriteAndFlush(ctx,modbusInfo,"开始组网");
		} catch (Exception e) {
			logger.error(">>>initstep(" + modbusInfo.getAddress_str() + ")请求设备,cmdCode="+modbusInfo.getCmdCode_str()
        				+"ccode="+modbusInfo.getcCode_str()+",exception1！", e);
			excuSeccess = false;
		}
		return excuSeccess;
		
	}
	
	/**
	 * 停止组网
	 * 
	 * @param ctx
	 * @param plc_SN
	 * @return
	 */
	public static boolean init6_stopCfgNetwork(ChannelHandlerContext ctx, String plc_SN) {
		boolean excuSeccess = true;
		final ModbusInfo modbusInfo = new ModbusInfo();
		try {
			logger.info("=====>>>initstep(" + plc_SN+ ") 停止组网...==63,00=============");
			Thread.currentThread().sleep(1000*5);//等待5秒钟后在做停止组网操作 ，不管组网是否达到要求的数量，这里暂时做成强制停止组网
			modbusInfo.setAddress(ConverUtil.hexStrToByteArr(plc_SN));
			modbusInfo.setcCode(ConverUtil.hexStrToByteArr("00")); 
			modbusInfo.setCmdCode(ConverUtil.hexStrToByteArr("63")); 
			// PDT 
			byte[] temp1 = new byte[0]; 
			modbusInfo.setPdt(temp1);
			ctxWriteAndFlush(ctx,modbusInfo,"停止组网");
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
			ctxWriteAndFlush(ctx,modbusInfo,"存储节点");
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
			logger.info("=====>>>initstep(" + plc_SN+ ") 配置节点...==98,03=============");
			modbusInfo.setAddress(ConverUtil.hexStrToByteArr(plc_SN));
			modbusInfo.setcCode(ConverUtil.hexStrToByteArr("03")); 
			modbusInfo.setCmdCode(ConverUtil.hexStrToByteArr("98")); 
			// PDT 
			byte[] temp1 = new byte[0]; 
			modbusInfo.setPdt(temp1);
			ctxWriteAndFlush(ctx,modbusInfo,"配置节点");
		} catch (Exception e) {
			logger.error(">>>initstep(" + modbusInfo.getAddress_str() + ")请求设备,cmdCode="+modbusInfo.getCmdCode_str()
        				+"ccode="+modbusInfo.getcCode_str()+",exception1！", e);
			excuSeccess = false;
		}
		return excuSeccess;
		
	}

}
