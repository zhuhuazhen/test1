package com.hzyw.iot.util.constant;

import com.alibaba.fastjson.JSONArray;
//import com.hzyw.iot.utils.PlcProtocolsUtils;
import com.hzyw.iot.utils.PlcProtocolsUtils;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang3.ArrayUtils;
import java.util.*;

/**
 * 指令参数（PDT）协议 解析
 * @author jian
 *
 */
public class PDTAdapter {
	private Map<String,String>paramStruct=new HashMap<String,String>(); //PDT 参数自定义结构
	public Map<String, String> getParamStruct() {
		return paramStruct;
	}

	public void setParamStruct(Map<String, String> paramStruct) {
		this.paramStruct = paramStruct;
	}

	/**
	 * pdt 请求协议解析
	 * JSON->16进制解析
	 * @param lenArr 参数的固定字节长度模板
	 * @param pdt 参数值   List<String[]>结构类型的JSON串
	 * @return
	 */
	public static String pdtRequstParser(int [][] lenArr,String pdt) throws Exception{
		String resutlPdt="",hexStr="";
		Integer converType=0,byteLen=0;
		byte[]codeByte;
		List<Object> paramList=JSONArray.parseArray(pdt);
		for(int j=0; j<paramList.size();j++) { //遍历多条记录的参数（如：多节点，多任务）
			JSONArray paramValArray=JSONArray.parseArray(JSONArray.toJSONString(paramList.get(j)));
			System.out.println(lenArr.length);
			System.out.println(paramValArray.size());
			if(lenArr.length!=paramValArray.size()) throw new Exception("解析请求的PDT报文错误！定义的字节长度模板与参数结构不一致!");
			for (int h = 0; h < paramValArray.size(); h++) {
                System.out.print("Array:" + paramValArray.getString(h) + "====");
                System.out.println("遍历字节长度 二维数组值:" +JSONArray.toJSONString(lenArr[h])+ " ");
                converType=lenArr[h][1]; //转换类型： 1、进制转换；2、码映射
                System.out.println("遍历字节长度 二维数组值 转换类型:" +converType);
                if(converType==1) { //进制转换（10进制->16进制）
                	byteLen=lenArr[h][0];
                	System.out.println("====每个字节定义的长度B:" +byteLen);
                	System.out.println("*************遍历字节长度 二维数组值10进制原值:" +paramValArray.getString(h));
                	hexStr=DecimalTransforUtil.toHexStr(paramValArray.getString(h),byteLen);//10进制->16进制串
                	System.out.println("*************遍历字节长度 二维数组值 转换16进制结果:" +hexStr);
                	codeByte=ConverUtil.hexStrToByteArr(hexStr); //16进制值的字符串转换为byte数组
                	if(codeByte.length>byteLen)throw new Exception("解析请求的PDT报文错误！定义的字节长度模板与参数结构不一致!");
                	hexStr=ConverUtil.convertByteToHexStr(codeByte,byteLen);
                	System.out.println("=====遍历字节长度 二维数组值 转换16进制 补位后结果:" +hexStr);
                	resutlPdt+=hexStr;
                }else if(converType==2) {//码映射
                	System.out.println("=====遍历字节长度 二维数组值 映射转码后的结果:" +paramValArray.getString(h));
                	resutlPdt+=ConverUtil.MappCODEVal(paramValArray.getString(h));
                }
            }
		}
		System.out.println("======请求的PDT报文转换的最后结果:" +resutlPdt);
		return resutlPdt;
	}
	
	/**
	 * pdt 响应协议解析
	 * 16进制->JSON->VO解析
	 * @param lenArr 参数字节长度模板
	 * @param paramNameTemp 参数属性名模板
	 * @param pdt 参数值
	 * @return
	 * @throws Exception
	 */
	public static LinkedHashMap<String,Object> pdtResposeParser(int [][] lenArr, String[] paramNameTemp, String pdt, ChannelHandlerContext ctx) throws Exception{
		String pdtParam="",byteBit="",itemVal="";
		Integer fromNum=0,toNum=0,itemStart=0,itemEnd=0,lenNum=0,pageAttr=0;
		List<Map<String,Object>>resultList=new ArrayList<Map<String,Object>>();
        LinkedHashMap<String,Object>resultMap=new LinkedHashMap<String,Object>();
		LinkedHashMap<String, Object> paramMap = new LinkedHashMap<String, Object>();
		LinkedHashMap<String,String>metricInfoMap=null;  //设备状态上报的数据
		byte[]pdtByteBuf;
		byte[] bytePdtArrs=ConverUtil.hexStrToByteArr(pdt);

		System.out.println("=============过滤 统计类值前，paramNameTemp 字节数组长度:"+paramNameTemp.length);
		System.out.println("=============过滤 统计类值前，lenArr 字节数组长度:"+lenArr.length);
        System.out.println("=============过滤 统计类值前，bytePdtArrs 字节数组长度:"+bytePdtArrs.length);
        //提取统计类属性字段的解析
        for(int h=0;h<lenArr.length;h++) {
            pageAttr=lenArr[h][3];  // 是否 统计类属性字段 标识（1:是,0:否）
            System.out.println("====["+h+"]===是否 统计类属性字段(1:是,0:否) pageAttr:"+pageAttr);
			lenNum=lenArr[h][0];
            if(pageAttr!=1) {
				fromNum+=lenNum;
				continue;
			}
			itemVal="";
            pdtByteBuf=Arrays.copyOfRange(bytePdtArrs,fromNum,fromNum+lenNum);
            pdtParam=ConverUtil.convertByteToHexString(pdtByteBuf);

            if(lenArr[h][1]==1) {//十进制转
                itemVal=ConverUtil.valueFormatUnit(pdtParam,lenArr[h][2]);
            }/*else if(lenArr[h][1]==2) {//二进制转
				if(lenArr[h][2] == 0){
					itemVal=DecimalTransforUtil.hexStringToByte(pdtParam,(pdtParam.length()*8));
				}else{
					itemVal=DecimalTransforUtil.hexStringToByte(pdtParam,lenArr[h][2]);
				}
            }else if(lenArr[h][1]==3) {//码映射
                itemVal=ConverUtil.MappCODE(pdtParam);
            }*/
			if(!"".equals(itemVal)) resultMap.put(paramNameTemp[h],itemVal);
            paramNameTemp=ArrayUtils.remove(paramNameTemp,h); //删除属性名模板中 统计类的属性名
            lenArr=ArrayUtils.remove(lenArr,h); //删除字节长度模板中，相应统计类的属性字节长度值
            for(int k=0; k<lenNum;k++){
                bytePdtArrs=ArrayUtils.remove(bytePdtArrs,fromNum);
            }
            h=0;
        }
        System.out.println("=============过滤 统计类值后，paramNameTemp 字节数组长度:"+paramNameTemp.length);
        System.out.println("=============过滤 统计类值后，lenArr 字节数组长度:"+lenArr.length);
        System.out.println("=============过滤 统计类值后，bytePdtArrs 字节数组长度:"+bytePdtArrs.length);

		int lenCount=calcByteCount(lenArr);
		//byte[]itemByteBuf= new byte[lenCount];
		System.out.println("=============过滤 统计类值后，bytePdtArrs.length :"+bytePdtArrs.length);
		//System.out.println("=============过滤 统计类值后，lenCount :"+lenCount);
		if(bytePdtArrs.length%lenCount==0) {
			int loopNum=bytePdtArrs.length/lenCount; //遍历解析响应的多条记录
            byte[]itemByteBuf= new byte[bytePdtArrs.length/loopNum];
            System.out.println("=============过滤 统计类值后，bytePdtArrs.length :"+bytePdtArrs.length);
            System.out.println("=============过滤 统计类值后，lenCount :"+lenCount);

			System.out.println("===pdtResposeParser==PDT 参数响应条数:"+loopNum);
			for(int k=0;k<loopNum;k++) {
                paramMap = new LinkedHashMap<String, Object>();
				metricInfoMap=new LinkedHashMap<String, String>();
				if(k>0)itemStart=itemEnd;
				itemEnd=itemEnd+lenCount;
				itemByteBuf=Arrays.copyOfRange(bytePdtArrs,itemStart,itemEnd);
				fromNum=0;
				toNum=0;
				for(int i=0;i<lenArr.length;i++) {
					lenNum=lenArr[i][0];
					if(i>0)fromNum=toNum;//fromNum+lenNum;
					toNum=toNum+lenNum; //对应每个参数的字节长度

					System.out.println("===pdtResposeParser==每个字节长度("+lenNum+")==每个参数 字节的启始索引:"+fromNum+" ~ "+toNum);

					pdtByteBuf=Arrays.copyOfRange(itemByteBuf,fromNum,toNum);
					itemVal="";
					pdtParam=ConverUtil.convertByteToHexString(pdtByteBuf);
					System.out.println("===pdtResposeParser==PDT 每个字节长度("+lenNum+") 对应 16进制值:"+pdtParam);
				    System.out.println("===pdtResposeParser==PDT 每个字节长度("+lenNum+") 对应 10进制值:"+DecimalTransforUtil.hexToLong(pdtParam,true));
					if(lenArr[i][1]==1) {//十进制转
						itemVal=ConverUtil.valueFormatUnit(pdtParam,lenArr[i][2]);
						metricInfoMap.put(paramNameTemp[i], itemVal);
					}else if(lenArr[i][1]==2) {//二进制转
					    int len=0;
						if(lenArr[i][2] == 0){
							itemVal=DecimalTransforUtil.hexStringToByte(pdtParam,(pdtByteBuf.length*8));
						}else{
							itemVal=DecimalTransforUtil.hexStringToByte(pdtParam,lenArr[i][2]);
						}
					}else if(lenArr[i][1]==3) {//码映射
						itemVal=ConverUtil.MappCODE(pdtParam);
						metricInfoMap.put(paramNameTemp[i], itemVal);
					}
					if(!"".equals(itemVal)) paramMap.put(paramNameTemp[i], itemVal);
				}
				//kafka  发送 设备状态上报的数据
				if(metricInfoMap.size()>0){
					System.out.println("===============kafka 发送设备状态上报数据 操作...");
					PlcProtocolsUtils.plcStateResponseSend(HEAD_TEMPLATE.getUID(),metricInfoMap,ctx);
				}
                resultList.add(paramMap);
			}
		}else {
			throw new Exception("解析 响应的PDT报文错误！‘字节长度模板’定义的字节长度 与 实际PDT参数报文的字节长度不匹配!");
		}
        resultMap.put("pdtList",resultList);
		Object resultJSON=JSONArray.toJSON(resultMap);
		//System.out.println("===pdtResposeParser==响应 解析PDT VO后 的json结构:"+JSONObject.toJSONString(resultJSON));
		return resultMap;
	}

    /**
     *   二进制位值转 char[]数组形式 JSON串 返回
     * @param byteBit
     * @param bitLen
     * @return
	 **/
	public static String bitArr2Json(String byteBit,Integer bitLen){
		byteBit=byteBit.length()>=bitLen? byteBit.substring(byteBit.length()-bitLen, byteBit.length()):byteBit;

		for(int i=0; i<(bitLen-byteBit.length()); i++){
			byteBit="0".concat(byteBit);
		}

		char[] bitChar=byteBit.toCharArray();
		return JSONArray.toJSONString(bitChar);
	}


	/**
	 * 计算 长度字节数组(入参) 的 总字节数
	 * @param lenArr
	 * @return
	 */
	public static Integer calcByteCount(int [][] lenArr) {
		Integer sumNum=0;
		if(lenArr.length>0) {
			for(int i=0;i<lenArr.length;i++) {
				sumNum+=lenArr[i][0];
			}
		}
		System.out.println("======长度字节数组(入参) 的 总字节数:" +sumNum);
		return sumNum;
	}
}
