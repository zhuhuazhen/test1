package com.hzyw.iot.netty.processor;

import com.alibaba.fastjson.JSON;
import com.hzyw.iot.netty.channelhandler.ChannelManagerHandler;
import com.hzyw.iot.netty.channelhandler.CommandHandler;
import com.hzyw.iot.netty.processor.Impl.IDataProcessor;
import com.hzyw.iot.netty.processor.Impl.IplcDataProcessor;
import com.hzyw.iot.netty.processor.Impl.ProcessorAbstract;
import com.hzyw.iot.util.ByteUtils;
import com.hzyw.iot.util.constant.ConverUtil;
import com.hzyw.iot.util.constant.ProtocalAdapter;
import com.hzyw.iot.utils.CRCUtils;
import com.hzyw.iot.utils.IotInfoConstant;
import com.hzyw.iot.utils.PlcProtocolsBusiness;
import com.hzyw.iot.utils.PlcProtocolsUtils;
import com.hzyw.iot.vo.dc.GlobalInfo;
import com.hzyw.iot.vo.dc.ModbusInfo;
import com.hzyw.iot.vo.dc.RTUInfo;
import com.hzyw.iot.vo.dc.enums.ERTUChannelFlag;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.ws.Response;

/**
 * ===PLC设备接入的消息处理：==== 1，协议报文 转化成 ModbusInfo 2，CRC校验数据 3，把modbusInfo.getData
 * 数据转化成十进制数，赋值给设备的某个字段 4，构造MessageVO消息 【消息类型（根据指令码来判断），】 如，节点信息上报： {
 * deviceid:000000000001 集中器地址 methods: [{method:operator_70H,
 * out:{输入电压：100，,...}
 * 
 * }] }, tags:{key:value,key:value} //扩展 }
 * 
 * 5， kafka.send(json.toJSONstring(MessageVO)) ;上报消息到KAFKA
 * 
 */
public class PlcDataProcessor extends ProcessorAbstract implements IDataProcessor, IplcDataProcessor {
	private static final Logger LOGGER = LoggerFactory.getLogger(PlcDataProcessor.class);
	private int type;
	public PlcDataProcessor() {
		super(ERTUChannelFlag.PLC);
	}
    
	public void setType(int type){
		this.type = type;
	}

	/*@Override
	public void translate(ChannelHandlerContext ctx, ByteBuf source, RTUInfo rtuInfo) throws Exception {
		System.out.println("------PlcDataProcessor---------type=----"+this.type);
		if (checkAndToProcess(this.type))
		{  
			System.out.println("------plcDataProcessor-------处理PLC设备接入的消息------");
			byte[] req = new byte[source.readableBytes()];
			source.readBytes(req);
			try {
				String body = new String(req, "UTF-8");
				System.out.println("接收到设备传递过来的数据如下 :" + ByteUtils.bytesToHexString(req));
				
				//校验报文格式合理性
				
				//构造响应的请求
				//
				if (ByteUtils.bytesToHexString(req).contains("F0")) {
	                //登陆成功 ，补全 channelInfo信息
					String sn = "SN1";
					ChannelManagerHandler.setRTUChannelInfo(ctx, sn);
	            } else if (true) {
	                // sn
	            	System.out.println("非登陆指令 :" );
	            } else {
	                LOGGER.warn("unchecked system source: \"{}\"", body);
	            }
				
				//String openLight = "68000000000100688002f0014416"; //开灯  68000000000100680401F0C616
				//String downLight = "68000000000100680309420000000000000300EB16"; //关灯
				String b = "680000000001006803094200000000000003002216";  
				
				//ByteBuf resp = ProtocalAdapter.messageRespose(req);//Unpooled.copiedBuffer(ByteUtils.hex2byte(response));
				
				//集中器->主机
	            byte[] byteArrs=null;
	            String ss="68000000000001680401f0c616";
	            byteArrs=ConverUtil.hexStrToByteArr(ss); //字符串转16进制字节数组
	            System.out.println(messageRespose(byteArrs));
				
				
				//String resp = ProtocalAdapter.messageRequest(ccode, cmd, paramBody).messageRespose(req);//Unpooled.copiedBuffer(ByteUtils.hex2byte(response));
				System.out.println("========1111---:"+b);
				ctx.write(com.hzyw.iot.util.constant.ConverUtil.hexStrToByteArr(b)); // 回写响应  messageRequest
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			//转化成MESSAGEVO
			
			//MESSAGEVO发送到KAFKA
			

			// 格式校验

			// 截取数据 王剑把这个类型重新定义下
			// ModbusInfo modbusInfo = new ModbusInfo(source);
			// //其实读取的指针已经到最后了，格式也是符合要求的 数据对不对是另一码事
			// 校验数据
			// if (!CRCUtils.checkCRC(modbusInfo.getFullData(),
			// modbusInfo.getCrc())) {
			// LOGGER.warn("R485 bad data: {}", String.valueOf(source));//数据有问题
			// 还要继续处理 ？
			// }
			// 数据转换
			// List<byte[]> dataItemList =
			// IplcDataProcessor.subData(modbusInfo.getData());

			// 构建 rtuInfo 信息 这里修改下 定义成KAFKA 要接入的数据模型一致的类型即可 即MessageVO对象的构建
			
			 * List<ItemInfo> itemInfoList = new ArrayList<>(2); byte[] SSLL =
			 * dataItemList.get(0); String itemKeySSLL = buildDataKey("SSLL");
			 * itemInfoList.add(new ItemInfo(itemKeySSLL,
			 * String.valueOf(IR485DataProcessor.exchangeHL(SSLL)))); byte[]
			 * LJLL = dataItemList.get(1); String itemKeyLJLL =
			 * buildDataKey("LJLL"); itemInfoList.add(new ItemInfo(itemKeyLJLL,
			 * String.valueOf(IR485DataProcessor.exchangeHL(LJLL)))); if
			 * (dataItemList.size() == 3) { byte[] YL = dataItemList.get(2);
			 * String itemKeyYL = buildDataKey("YL"); itemInfoList.add(new
			 * ItemInfo(itemKeyYL,
			 * String.valueOf(IR485DataProcessor.toFloat(YL)))); }
			 * rtuInfo.setData(itemInfoList);
			 * 
			 * // 设置 消息类型 EMqExchange[] eMqExchanges = {EMqExchange.RTU_DATA,
			 * EMqExchange.RTU_HEART}; rtuInfo.setMqExchange(eMqExchanges);
			 
		} else {
			if (super.getNextProcessor() != null)
				super.getNextProcessor().translate(ctx, source, rtuInfo);
		}
	}*/
	
	 
	
	/* 
	 * 设备上报数据
	 * - 集中器 -》主机
	 * 
	 * (non-Javadoc)
	 * @see com.hzyw.iot.netty.processor.Impl.IDataProcessor#translate(io.netty.channel.ChannelHandlerContext, io.netty.buffer.ByteBuf, com.hzyw.iot.vo.dc.RTUInfo)
	 */
	@Override
	public void translate(ChannelHandlerContext ctx, ByteBuf source, RTUInfo rtuInfo) throws Exception {
		System.out.println("------PlcDataProcessor---------type=----"+this.type);
		if (checkAndToProcess(this.type))
		{  
			try{
				//test
				//byte[] req = new byte[source.readableBytes()]; 
				//source.readBytes(req);
				//String reqs= ByteUtils.bytesToHexString(req);
				//System.out.println("///////////////上报数据 :" + reqs);
				//source.resetReaderIndex();
				/*ModbusInfo modbusInfo = new ModbusInfo(source);
				PlcProtocolsUtils.resonseLogin(ctx, modbusInfo);//通知设备已成功登陆*/				
				/*//响应处理
				ProtocalAdapter protocalAdapter = new ProtocalAdapter();
				String response = protocalAdapter.testResponseCode(reqs);
				if(response!=null&!response.equals("")) {
					//响应数据到PLC
					CommandHandler.writeCommand("12345000000000100", response, 2);
				}*/
				
				//---end test
				
				ModbusInfo modbusInfo = new ModbusInfo(source);  
				if(modbusInfo == null){
	                return; 
				}
				String serverPort = String.valueOf(PlcProtocolsUtils.getPort(ctx.channel()));
				Map<String, Map<String, String>> Alldevinfo = IotInfoConstant.allDevInfo.get(serverPort);
	            // 判断此设备是否已经在平台注册
				if(Alldevinfo==null || Alldevinfo.get(modbusInfo.getAddress_str()+"_attribute") == null){
					LOGGER.warn("设备【"+modbusInfo.getAddress_str()+"】设备未注册到平台!" );
	                return;
				}
	            // CRC校验
	            if (!CRCUtils.checkCRC(modbusInfo.getFullData(), modbusInfo.getCrc())) {
	                LOGGER.warn("plc bad data: {}", ConverUtil.convertUUIDByteToHexString(modbusInfo.getFullData()));
	                return;
	            }
	            // 头部、尾部校验
	            if(!"68".equals(modbusInfo.getHeadStart_str()) && !"68".equals(modbusInfo.getHeadEnd_str()) && !"16".equals(modbusInfo.getEnd_str())){
	            	//取命令码、应答码、然后响应
	            	LOGGER.warn("设备【"+modbusInfo.getAddress_str()+"】命令码无效!" + PlcProtocolsUtils.loggerBaseInfo(modbusInfo));
	            	return;
	            }
	            // 验证当前命令码、控制码是否存在，不存在直接响应失败
	            if(!IotInfoConstant.allDevInfo.get(serverPort).get(modbusInfo.getAddress_str()+"_cmd").containsKey(modbusInfo.getCmdCode_str())){
	            	//取命令码、应答码、然后响应
	            	LOGGER.warn("设备【"+modbusInfo.getAddress_str()+"】命令码无效!" + PlcProtocolsUtils.loggerBaseInfo(modbusInfo));
	            	return;
	            }
	            if(IotInfoConstant.allDevInfo.get(serverPort).get(modbusInfo.getAddress_str()+"_req_ack").get(modbusInfo.getcCode_str())==null){
	            	LOGGER.warn("设备【"+modbusInfo.getAddress_str()+"】控制码无效!" + PlcProtocolsUtils.loggerBaseInfo(modbusInfo));
	            	return;
	            }
	             
	            // ====登陆、设备配置过程=======start >>===
	            boolean isLogin = PlcProtocolsUtils.isLogin(modbusInfo);   //是否已登陆
	            boolean isConfig =PlcProtocolsUtils.isConfig(modbusInfo); //PlcProtocolsUtils.isConfig(modbusInfo); //是否已配置设备
	            if(!isLogin && "f0".equals(modbusInfo.getCmdCode_str().toLowerCase())){
	            	isLogin = true;
	            	boolean seccess = PlcProtocolsUtils.resonseLogin(ctx, modbusInfo);//通知设备已成功登陆
	            	if(!seccess)return;//通知登陆失败，直接退出
	            	//登陆成功，如果是第一次登陆，则自动进入配置设备流程
	            	 if(!isConfig){
	            		//发起初始化配置流程
	            		 PlcProtocolsUtils.init_1(ctx, modbusInfo);
	            		 return;
	            	 }else{
	                 	LOGGER.warn("设备【"+modbusInfo.getAddress_str()+"】已配置!");
	            	 }
	            }else if(isLogin && "f0".equals(modbusInfo.getCmdCode_str().toLowerCase())){
	            	//登陆成功后，是否还会一直发登陆？和客户沟通下
	            	LOGGER.warn("设备【" + modbusInfo.getAddress_str()+"】，====已登陆  ===为什么还一直请求登陆？===" );
	            }
	            if(!isLogin){
	            	LOGGER.warn("设备【" + modbusInfo.getAddress_str()+"】未登陆，请检查登陆报文是否符合规范!" );
	            	return;
	            }
	            
	            if(!isConfig){
	            	PlcProtocolsUtils.init_2_9(ctx, modbusInfo);
	            	LOGGER.warn("设备【" + modbusInfo.getAddress_str()+"】未配置，请检查自动配置过程日志，定位失败原因!!" );
	            	return;
	            }
	            if(isConfig && isLogin){
	            	LOGGER.warn("设备【" + modbusInfo.getAddress_str()+"】已登陆 ，已配置!!" );
	            }
	            //心跳
	            PlcProtocolsUtils.heartBeat(ctx, modbusInfo);
	            
	            /*if("f7".equals(modbusInfo.getCmdCode_str().toLowerCase())){
	            	//LOGGER.warn("设备【"+modbusInfo.getAddress_str()+"】命令码= "+modbusInfo.getCmdCode_str() + " ,cCode=" +modbusInfo.getcCode_str()+" 已过滤!!!!  !"  );
	            	PlcProtocolsBusiness.protocals_process_Response(ctx, modbusInfo);
	            	return;
	            }*/
	            
	            // ====登陆、设备配置过程=======end >>===
	            
	            //（集中器-》主机）其他协议处理  ,如果想直接调试其他的控灯协议，把上面的'登陆、设备配置过程'注释掉即可
					
				//1,校验报文格式合理性    比较简单,上面已经做了校验了，这里不需要这个步骤
				//2,处理（集中器-》主机）的请求
				//3,响应（集中器）请求
				//4,组装要上报到KAFKA的消息
				//5,上报消息到KAFKA ,类型如下
					/*
					设备->主机
					    response	           上报下发请求结果       ResultMessageVO<ResponseDataVO>
						devInfoResponse	 属性上报   		  ResultMessageVO<MetricInfoResponseDataVO>    
						metricInfoResponse	设备状态数据上报          ResultMessageVO<MetricInfoResponseDataVO>
						devSignlResponse	设备信号上报                    ResultMessageVO<DevSignlResponseDataVO>
					*/
 
	            if(!("f0".equals(modbusInfo.getCmdCode_str().toLowerCase()) //忽略指令
	            		|| "f1".equals(modbusInfo.getCmdCode_str().trim().toLowerCase()))){ 
	            	System.out.println(" ----------------------------------" +modbusInfo.getCmdCode_str().toLowerCase() );
	            	System.out.println(" ----------------------------------" + !"f1".equals(modbusInfo.getCmdCode_str().trim().toLowerCase()) );
	            	source.resetReaderIndex();
	            	byte[] req = new byte[source.readableBytes()]; 
					source.readBytes(req);
					String reqs= ByteUtils.bytesToHexString(req);
					System.out.println("///////////////上报数据 :" + reqs);
	            	//byte[] req = new byte[source.readableBytes()];
	            	//String sourceStr=ByteUtils.bytesToHexString(req);
	            	ProtocalAdapter.messageRespose(req,ctx);
	            } 
	            
			}catch(Exception e){
				LOGGER.error("translate exception,",e); 
			}
		} else {
			if (super.getNextProcessor() != null)
				super.getNextProcessor().translate(ctx, source, rtuInfo);
		}
	}
	
	//ByteBuffer 转换 String：
	public static String decode(ByteBuffer bb){ 
		Charset charset = Charset.forName("ISO8859-1");
	    return charset.decode(bb).toString();
	}
	
	public static String convertByteBufToString(ByteBuf buf) {
		String str;
		if(buf.hasArray()) { // 处理堆缓冲区
			str = new String(buf.array(), buf.arrayOffset() + buf.readerIndex(), buf.readableBytes());
		} else { // 处理直接缓冲区以及复合缓冲区
			byte[] bytes = new byte[buf.readableBytes()];
			buf.getBytes(buf.readerIndex(), bytes);
			str = new String(bytes, 0, buf.readableBytes());
		}
		
		/*ByteBuf bf =req.content();
        byte[] byteArray = new byte[bf.capacity()];  
        bf.readBytes(byteArray);  
        String result = new String(byteArray);*/
        
			return str;
	}
}
