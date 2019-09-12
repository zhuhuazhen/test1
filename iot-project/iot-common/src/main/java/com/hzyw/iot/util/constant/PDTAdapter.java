package com.hzyw.iot.util.constant;

import java.nio.ByteBuffer;
import java.util.*;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.ArrayUtils;

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
                	//hexStr=ConverUtil.toHex(Integer.parseInt(paramValArray.getString(h))); //10进制->16进制串
                	System.out.println("*************遍历字节长度 二维数组值10进制原值:" +paramValArray.getString(h));
                	hexStr=DecimalTransforUtil.toHexStr(paramValArray.getString(h),byteLen);
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
	public static LinkedHashMap<String,Object> pdtResposeParser(int [][] lenArr,String[] paramNameTemp,String pdt) throws Exception{
        //ArrayUtils.remove(bytePdtArrs,0);
		String pdtParam="",byteBit="",itemVal="";
		Integer fromNum=0,toNum=0,itemStart=0,itemEnd=0,lenNum=0,pageAttr=0;
		List<Map<String,String>>resultList=new ArrayList<Map<String,String>>();
        LinkedHashMap<String,Object>resultMap=new LinkedHashMap<String,Object>();
		LinkedHashMap<String, String> paramMap = new LinkedHashMap<String, String>();
		int lenCount=calcByteCount(lenArr);
		byte[]pdtByteBuf; 
		byte[]itemByteBuf= new byte[lenCount];
		byte[] bytePdtArrs=ConverUtil.hexStrToByteArr(pdt);

        System.out.println("=============过滤 统计类值前，字节数组长度:"+bytePdtArrs.length);
        //提取统计类属性字段的解析
        for(int h=0;h<lenArr.length;h++) {
            pageAttr=lenArr[h][3];  // 是否 统计类属性字段 标识（1:是,0:否）
            System.out.println("====["+h+"]===是否 统计类属性字段(1:是,0:否) pageAttr:"+pageAttr);

            if(pageAttr!=1) continue;
            if(lenArr[h][1]==1) {//十进制转
                itemVal=ConverUtil.valueFormatUnit(pdtParam,lenArr[h][2]);
            }else if(lenArr[h][1]==2) {//二进制转
                byteBit=DecimalTransforUtil.hexStringToByte(pdtParam,lenArr[h][2]);
                itemVal=byteBit;
            }else if(lenArr[h][1]==3) {//码映射
                itemVal=ConverUtil.MappCODE(pdtParam);
            }
            resultMap.put(paramNameTemp[h],itemVal);
            paramNameTemp=ArrayUtils.remove(paramNameTemp,h); //删除属性名模板中 统计类的属性名
            lenArr=ArrayUtils.remove(lenArr,h); //删除字节长度模板中，相应统计类的属性字节长度值
            for(int k=0; k<lenNum;k++){
                bytePdtArrs=ArrayUtils.remove(bytePdtArrs,0);  //注： 这里删除起始位置不合理灵活，但目录需求不影响，后面优化修改。
            }
            h=0;
        }
        System.out.println("=============过滤 统计类值后，字节数组长度:"+bytePdtArrs.length);

		if(bytePdtArrs.length%lenCount==0) {
			int loopNum=bytePdtArrs.length/lenCount; //遍历解析响应的多条记录
			System.out.println("===pdtResposeParser==PDT 参数响应条数:"+loopNum);
			for(int k=0;k<loopNum;k++) {
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
					
					pdtParam=ConverUtil.convertByteToHexString(pdtByteBuf);
					System.out.println("===pdtResposeParser==PDT 每个字节长度("+lenNum+") 对应 16进制值:"+pdtParam);
				    System.out.println("===pdtResposeParser==PDT 每个字节长度("+lenNum+") 对应 10进制值:"+DecimalTransforUtil.hexToLong(pdtParam,true));
					if(lenArr[i][1]==1) {//十进制转
						itemVal=ConverUtil.valueFormatUnit(pdtParam,lenArr[i][2]);
					}else if(lenArr[i][1]==2) {//二进制转
						byteBit=DecimalTransforUtil.hexStringToByte(pdtParam,lenArr[i][2]);
						itemVal=byteBit;
						//itemVal=bitArr2Json(byteBit,lenArr[i][2]);
					}else if(lenArr[i][1]==3) {//码映射
						itemVal=ConverUtil.MappCODE(pdtParam);
					}
			        paramMap.put(paramNameTemp[i], itemVal);
				}
			}
			 resultList.add(paramMap);
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
	 */
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
