package com.hzyw.iot.util.constant;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.ByteBuffer;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonArray;

public class ConverUtil {
	/**
	 * 将表示16进制值的字符串转换为byte数组， 和public static String byteArr2HexStr(byte[] arrB)
	 * 互为可逆的转换过程
	 *
	 * @param strIn 需要转换的字符串
	 * @return 转换后的byte数组
	 * @throws Exception 本方法不处理任何异常，所有异常全部抛出
	 * @author <a href="mailto:leo841001@163.com">LiGuoQing</a>
	 */
	public static byte[] hexStrToByteArr(String strIn) throws Exception {
		byte[] arrB = strIn.getBytes("ISO8859-1");// getBytes();
		int iLen = arrB.length;

		// 两个字符表示一个字节，所以字节数组长度是字符串长度除以2
		byte[] arrOut = new byte[iLen / 2];
		for (int i = 0; i < iLen; i = i + 2) {
			String strTmp = new String(arrB, i, 2);
			arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
		}
		return arrOut;
	}

	/**
	 * 将byte数组转化为16进制输出
	 * 
	 * @param bytes
	 * @return
	 */
	public static String convertByteToHexString(byte[] bytes) {
		String result = "";
		for (int i = 0; i < bytes.length; i++) {
			int temp = bytes[i] & 0xff;
			String tempHex = Integer.toHexString(temp);
			if (tempHex.length() < 2) {
				result += "0" + tempHex;
			} else {
				result += tempHex;
			}
		}
		return result;
	}

	public static String convertUUIDByteToHexString(byte[] bytes) {
		String result = "";
		for (int i = 0; i < bytes.length; i++) {
			int temp = bytes[i] & 0xff;
			String tempHex = Integer.toHexString(temp);
			if (tempHex.length() < 2) {
				result += "0" + tempHex;
			} else {
				result += tempHex;
			}
		}
		// System.out.println("======aaa:"+(6 - bytes.length));
		for (int j = 0; j < 6 - bytes.length; j++) {
			result = "00" + result;
		}
		return result;
	}

	public static String convertByteToHexStr(byte[] bytes, Integer lengthNum) {
		String result = "";
		for (int i = 0; i < bytes.length; i++) {
			int temp = bytes[i] & 0xff;
			String tempHex = Integer.toHexString(temp);
			if (tempHex.length() < 2) {
				result += "0" + tempHex;
			} else {
				result += tempHex;
			}
		}
		for (int j = 0; j < lengthNum - bytes.length; j++) {
			result = "00" + result;
		}
		return result;
	}

	/**
	 * 将byte转化为16进制输出
	 * 
	 * @param bytes
	 * @return
	 */
	public static String convertByteToHexStr(byte bytes) {
		String result = "";
		int temp = bytes & 0xff;
		String tempHex = Integer.toHexString(temp);
		if (tempHex.length() < 2) {
			result += "0" + tempHex;
		} else {
			result += tempHex;
		}
		return result;
	}

	/**
	 *
	 * 获取16进制累加和 如：hexstr = "68 00 00 00 00 00 01 68 00 01 73"
	 * 
	 * @param hexdata
	 * @return
	 */
	public static String makeChecksum(String hexdata) {
		if (hexdata == null || hexdata.equals("")) {
			return "00";
		}
		hexdata = hexdata.replaceAll(" ", "");
		int total = 0;
		int len = hexdata.length();
		if (len % 2 != 0) {
			return "00";
		}
		int num = 0;
		while (num < len) {
			String s = hexdata.substring(num, num + 2);
			total += Integer.parseInt(s, 16);
			num = num + 2;
		}
		String data = hexInt(total);
		if (data.length() > 2) {
			data = data.substring(data.length() - 2, data.length());
		}
		return data;
	}

	/**
	 * byte数组转换为二进制字符串,每个字节以","隔开
	 **/
	public static String byteArrToBinStr(byte[] b) {
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			// String tail = Integer.toBinaryString(b[i] & 0xFF).replace(' ', '0');
			String tail = Long.toString(b[i] & 0xff, 2) + ",";
			// result.append(Long.toString(b[i] & 0xff, 2) + ",");
			int tailLen = tail.length();
			for (int j = 0; j <= 8 - tailLen; j++) {
				// result.insert(0,"0");
				tail = "0".concat(tail);
			}
			result.append(tail);
		}
		return result.toString().substring(0, result.length() - 1);
	}
	
	public static String byteArrToBinString(byte[] b) {
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			// String tail = Integer.toBinaryString(b[i] & 0xFF).replace(' ', '0');
			String tail = Long.toString(b[i] & 0xff, 2) + ",";
			// result.append(Long.toString(b[i] & 0xff, 2) + ",");
			int tailLen = tail.length();
			for (int j = 0; j <= 8 - tailLen; j++) {
				// result.insert(0,"0");
				tail = "0".concat(tail);
			}
			result.append(tail);
		}
		return result.toString().substring(0, result.length() - 1);
	}

	private static String hexInt(int total) {
		int a = total / 256;
		int b = total % 256;
		if (a > 255) {
			// return hexInt(a) + format(b);
			return hexInt(a) + format(b);
		}
		return format(a) + format(b);
	}

	private static String format(int hex) {
		String hexa = Integer.toHexString(hex);
		int len = hexa.length();
		if (len < 2) {
			hexa = "0" + hexa;
		}
		return hexa;
	}

	// 转换为二进制
	public static String toBin(int num) {
		return conversion(num, 1, 1);
	}

	// (转换为八进制
	public static String tooct(int num) {
		return conversion(num, 7, 3);
	}

	// 转换为十六进制
	public static String toHex(int num) {
		return conversion(num, 15, 4);
	}

	// 转换
	public static String conversion(int num, int diwei, int yiwei) {
		String result = "";
		// 如果num等于0，结果输出为0
		if (num == 0) {
			return "00";
		}
		// 定义一个包含二进制、八进制、十六进制的表
		char[] chs = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', };
		// 定义一个临时容器
		char[] arr = new char[32];
		// 定义一个操作数组的指针
		int pos = arr.length;
		// 利用与低位最大值的方式取出低位，存到临时数组中
		while (num != 0) {
			arr[--pos] = chs[num & diwei];// --pos倒著往临时容器里存
			num >>= yiwei;// 无条件右移相应位数
		}
		// 返回结果
		for (int x = pos; x < arr.length; x++) {
			result = arr[x] + result;
		}
		System.out.print("=====进制转换后结果：" + result);
		return result;
	}

	/**
	 * 映射 码的16进制值（00H~FFH） 如：00H->00
	 * 
	 * @param code
	 * @return
	 */
	public static String MappCODEVal(String code) throws Exception {
		if (StringUtils.isEmpty(code) || StringUtils.isBlank(code))
			return "";
		if (code.endsWith("H")) {
			code = code.substring(0, code.lastIndexOf("H"));
			System.out.println("====映射 码的16进制值:" + code);
			code = isNumeric(code) ? code : code.toLowerCase();
			byte[] codeByte = hexStrToByteArr(code); // 转换时 验证值是否在16进制范围内
			if (codeByte.length > 1)
				throw new Exception("PDT协议 解析失败！映射码值超过一个1B长度");
			;
			return convertByteToHexString(codeByte);
		} else {
			String hexStr = toHex(Integer.parseInt(code));
			System.out.println("====非 映射 码的16进制值， 转16进制值：" + hexStr);
			return toHex(Integer.parseInt(code));
		}
	}

	/**
	 * 映射 16进制值转码 如：00->00H
	 * 
	 * @param code
	 * @return
	 * @throws Exception
	 */
	public static String MappCODE(String code) throws Exception {
		byte[] codeByte = hexStrToByteArr(code); // 转换时 验证值是否在16进制范围内
		if (codeByte.length > 1)
			throw new Exception("PDT协议 解析失败！值映射转码超过一个1B长度");
		;
		code = convertByteToHexString(codeByte);
		return code.concat("H");
	}

	public final static boolean isNumeric(String s) {
		if (s != null && !"".equals(s.trim()))
			return s.matches("^[0-9]*$");
		else
			return false;
	}
	
	
	/**
	 * 参数值 格式，单位映射转化
	 * 16进制->10进制
	 * 1、相电压(例:220V),2、相电流(例:9.8A,10A),3、相功率(例:2156W),4、相功率因数(例:98%),5、电能(例:1579.5kWh),6、AD输入电压(例:3.75V)
	 * @param val
	 * @param type
	 */
	public static String valueFormatUnit(String val,Integer type) {
		Long decimalVal=0L;
		String numVal="";
		if(type==1) {
			decimalVal=DecimalTransforUtil.hexToLong(val,true); //16进制转10进制
			numVal= new DecimalFormat("###").format(decimalVal);
			numVal=numVal.length()>=3?numVal.substring(0,3).concat("V"):numVal.concat("V");
			System.out.println("====相电压值:"+numVal);
		}else if(type==2) {
			decimalVal=DecimalTransforUtil.hexToLong(val,true); //16进制转10进制
			BigDecimal bi1 = new BigDecimal(decimalVal.toString());
			BigDecimal bi2 = new BigDecimal("1000");
			BigDecimal bi3 = new BigDecimal("10");
			BigDecimal divide = bi1.divide(bi2, 1, RoundingMode.HALF_UP);
			numVal=divide.compareTo(bi3)>-1? numVal= new DecimalFormat("##").format(divide):divide.toString();
			numVal=numVal.concat("A");
			System.out.println("====相电流值:"+numVal);
		}else if(type==3) {
			decimalVal=DecimalTransforUtil.hexToLong(val,true); //16进制转10进制
			numVal= new DecimalFormat("####").format(decimalVal);
			numVal+="W";
			System.out.println("====相功率值:"+numVal);
		}else if(type==4) {
			decimalVal=DecimalTransforUtil.hexToLong(val,true); //16进制转10进制
			numVal=String.format("%d%%", decimalVal);
			System.out.println("====相功率因数值:"+numVal);
		}else if(type==5) {
			decimalVal=DecimalTransforUtil.hexToLong(val,true); //16进制转10进制
			BigDecimal bi1 = new BigDecimal(decimalVal.toString());
			BigDecimal bi2 = new BigDecimal("10");
			BigDecimal bi3 = new BigDecimal("10");
			BigDecimal divide = bi1.divide(bi2, 5, RoundingMode.HALF_UP);
			numVal=divide.compareTo(bi3)>-1? numVal= new DecimalFormat("####.#").format(divide):divide.toString();
			numVal=numVal.concat("kWh");
		}else if(type==6) {
			decimalVal=DecimalTransforUtil.hexToLong(val,true); //16进制转10进制
			BigDecimal bi1 = new BigDecimal(decimalVal.toString());
			BigDecimal bi2 = new BigDecimal("1000");
			BigDecimal bi3 = new BigDecimal("10");
			BigDecimal divide = bi1.divide(bi2, 2, RoundingMode.HALF_UP);
			numVal=divide.compareTo(bi3)>-1? numVal= new DecimalFormat("###.#").format(divide):divide.toString();
			numVal=numVal.concat("V");
		}
		return numVal;
	}

	public static void main(String[] s) {
		/*
		 * List<String[]> aa=new ArrayList<String[]>(); String[] hh=new
		 * String[]{"aa","11","22"}; aa.add(hh); aa.add(new String[]{"ee","hh","kk"});
		 * aa.add(new String[]{"1e","h1","k1"});
		 * 
		 * String a11=JSONArray.toJSONString(aa); //JSONObject.toJSONString(aa);
		 * List<Object> bb=JSONArray.parseArray(a11); for(int i=0; i<bb.size();i++) {
		 * System.out.println("======第"+i+"条，数据记录值："+JSONArray.toJSONString(bb.get(i)));
		 * JSONArray
		 * transitListArray=JSONArray.parseArray(JSONArray.toJSONString(bb.get(i)));
		 * 
		 * for (int h = 0; h < transitListArray.size(); h++) {
		 * System.out.println("Array:" + transitListArray.getString(h) + " "); }
		 * 
		 * 
		 * String[]ab=(String[]) bb.get(i); for(int j=0; j<ab.length;j++) {
		 * System.out.println("======第"+i+"条，数据记录值："+ab[j]); }
		 * 
		 * }
		 * 
		 * 
		 * 
		 * //System.out.println("jsonObject2：" + JSONObject.toJSON.toJSONString(aa));
		 * 
		 * System.out.println("jsonArray FROM HASHMAP：" + JSONArray.toJSONString(aa));
		 */

		//System.out.println("===result：" + valueFormatUnit("0EA6",6));
		
		/*
		 *  int [] data = new int[]{1,2,3,4,5,6,7,8,9};
		 *  int [] newData= Arrays.copyOfRange(data,2,7);  for(int i:newData){
		 *  System.out.print(i+" "); }
		 */
		
		
	}

}