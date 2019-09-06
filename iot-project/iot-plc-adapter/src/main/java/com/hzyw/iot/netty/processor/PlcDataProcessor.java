package com.hzyw.iot.netty.processor;

import com.hzyw.iot.netty.channelhandler.ChannelManagerHandler;
import com.hzyw.iot.netty.processor.Impl.IDataProcessor;
import com.hzyw.iot.netty.processor.Impl.IplcDataProcessor;
import com.hzyw.iot.netty.processor.Impl.ProcessorAbstract;
import com.hzyw.iot.util.ByteUtils;
import com.hzyw.iot.util.constant.ConverUtil;
import com.hzyw.iot.util.constant.IotInfoConstant;
import com.hzyw.iot.util.constant.ProtocalAdapter;
import com.hzyw.iot.utils.CRCUtils;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	
	static Map<String, String> gloable = new HashMap<String, String>();

	public boolean isLogin(ModbusInfo modbusInfo) {
		String flag = gloable.get(modbusInfo.getAddress_str()+"_login"); // 1表示已登陆   0 表示未登陆  （通过心跳判断是否掉线来设置）
		if (flag != null && flag.equals("1")) {
			return true;
		}
		return false;
	}
	
	public boolean isConfig(ModbusInfo modbusInfo) {
		String flag = gloable.get(modbusInfo.getAddress_str()+"_config");//1表示已下发配置到存储 0表示未下发配置过（只需要配置一次即可，配置以后才能控灯）
		if (flag != null && flag.equals("1")) {
			return true;
		}
		return false;
	}
	
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
			 ModbusInfo modbusInfo = new ModbusInfo(source); //其实读取的指针已经到最后了，格式也是符合要求的 数据对不对是另一码事
            // 校验数据
            if (!CRCUtils.checkCRC(modbusInfo.getFullData(), modbusInfo.getCrc())) {
                LOGGER.warn("plc bad data: {}", ConverUtil.convertUUIDByteToHexString(modbusInfo.getFullData()));//数据有问题 还要继续处理 ？
            }
            
            //登陆
            boolean isLogin = isLogin(modbusInfo); //从缓存里面获取
            if(!isLogin && "F0".equals(modbusInfo.getCmdCode())){
            	isLogin = true;
            	gloable.put(modbusInfo.getAddress_str()+"_login", "1");
            	System.out.println("设备【"+modbusInfo.getAddress_str()+"】登陆成功!" );
            	//响应登陆成功请求
            	String rp ="68"+modbusInfo.getAddress_str()+"688002f0014416";   //01H：登陆成功。  02H：登陆失败。03H：主机忙。
            	ctx.write(ConverUtil.hexStrToByteArr(rp));
            	return;
            }
            if(!isLogin){
            	System.out.println("设备【"+modbusInfo.getAddress_str()+"】未登陆，请检查登陆报文是否符合规范!" );
            	return;
            }
            
            //初始化配置流程
            boolean isConfig = isConfig(modbusInfo); //从缓存里面获取
            if(!isConfig){
            	isLogin = true;
            	gloable.put(modbusInfo.getAddress_str()+"_login", "1");
            	System.out.println("=====>>>开始自动化配置===============" );
            	System.out.println("=====>>>开始组网===============" );
            	String request ="68"+modbusInfo.getAddress_str()+"688002f0014416";   //01H：登陆成功。  02H：登陆失败。03H：主机忙。
            	ModbusInfo modbusInfo_62h = new ModbusInfo();
            	//data-PDT内容
            	byte[] groupNum = new byte[1];
            	groupNum[0] = (byte)10; //组网个数=10
            	modbusInfo_62h.setPdt(groupNum);
            	
            	//控制码
            	modbusInfo_62h.setcCode(ConverUtil.hexStrToByteArr("04"));
            	
            	//数据长度 ,这里固定写死  
            	//Integer i = new Integer(1+modbusInfo_62h.getPdt().length);  可改成动态取
            	Integer i = new Integer(2);
            	byte[] length = new byte[2];
            	length[0] = (byte)2; //长度=2
            	modbusInfo_62h.setLength(length); //1个字节 + 内容的字节个数
            	
            	//操作码
            	modbusInfo_62h.setCmdCode(ConverUtil.hexStrToByteArr("62"));
            	
            	//技术校验码
            	//modbusInfo_62h.setCrc(modbusInfo_62h.crcData());//合并
            	String checksum = ConverUtil.makeChecksum(ConverUtil.convertByteToHexString(modbusInfo_62h.crcData()));
            	 
            	ctx.write(modbusInfo_62h.crcData());  //直接写字节
            	return;
            }
            if(!isConfig){
            	System.out.println("设备【"+modbusInfo.getAddress()+"】未下发配置到设备存储列表，请检查下发配置过程中失败原因!" );
            	return;
            }
	          
			//其他公共的代码
			//判断是否是登陆
			//判断数据是否符合规范
			System.out.println("------plcDataProcessor-------处理PLC设备接入的消息------");
			byte[] req = new byte[source.readableBytes()];
			source.readBytes(req);
			 
			String body = new String(req, "UTF-8");
			System.out.println("接收到设备传递过来的数据如下 :" + ByteUtils.bytesToHexString(req));
				
			//校验报文格式合理性
			//构造响应的请求    
			//响应（集中器）请求
			//上报消息到KAFKA ,类型如下
			InetSocketAddress insocket = (InetSocketAddress)ctx.channel().localAddress();
			int port =insocket.getPort();
			Map<String, String> attributers =  IotInfoConstant.allDevInfo.get(port).get("SN");
			//Map<String, String> methods =  IotInfoConstant.allDevInfo.get(port).get("SN");
				/*
				    response	           上报下发请求结果       ResultMessageVO<ResponseDataVO>
					devInfoResponse	 属性上报   		  ResultMessageVO<MetricInfoResponseDataVO>
					metricInfoResponse	设备状态数据上报  ResultMessageVO<MetricInfoResponseDataVO>
					devSignlResponse	设备信号上报   ResultMessageVO<DevSignlResponseDataVO>
				*/
			
		} else {
			if (super.getNextProcessor() != null)
				super.getNextProcessor().translate(ctx, source, rtuInfo);
		}
	}
}
