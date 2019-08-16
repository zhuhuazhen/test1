package com.cube.utils.newStringUtils;

/**
 * @ClassName: Constant
 * @Description: 手表及平台发送指令常量类
 * @author Tomlin
 * @date 2015-6-9 上午10:57:42
 * @version V1.0
 */
public class Constant {

	/**
	 * 链路保持<br>
	 * 手表：CS*YYYYYYYYYY*LEN*LK；  平台：CS*YYYYYYYYYY*LEN*LK<br>
	 * 手表：CS*YYYYYYYYYY*LEN*LK,步数,翻滚次数,电量百分数； 平台：CS*YYYYYYYYYY*LEN*LK
	 */
	public static String COMMAND_LK = "LK";
	
	/**
	 * 位置数据上报<br>
	 * 手表：CS*YYYYYYYYYY*LEN*UD,位置数据(见附录一); 平台：无
	 */
	public static String COMMAND_UD = "UD";
	
	/**
	 * 盲点补传数据<br>
	 * 手表：CS*YYYYYYYYYY*LEN*UD2,位置数据(见附录一); 平台：无
	 */
	public static String COMMAND_UD2 = "UD2";
	
	/**
	 * 报警数据上报<br>
	 * 手表：CS*YYYYYYYYYY*LEN*AL,位置数据(见附录一); 平台：CS*YYYYYYYYYY*LEN*AL
	 */
	public static String COMMAND_AL = "AL";
	
	/**
	 * 请求地址指令<br>
	 * 手表：CS*YYYYYYYYYY*LEN*WAD,语言,位置数据(见附录一); 平台：CS*YYYYYYYYYY*LEN*RAD,定位类型,地址数据<br>
	 * 说明: 终端请求地址指令,其中 CH 代表中文,EN 代表英文,地址数据为 GB232 编码,定位类型分为 GPS 定位和 BASE 定位两种.
	 */
	public static String COMMAND_WAD = "WAD";
	
	public static String COMMAND_RAD = "RAD";
	
	public static String COMMAND_GPS = "GPS";
	
	public static String COMMAND_BASE = "BASE";
	/**
	 * 请求经纬度指令<br>
	 * 手表：CS*YYYYYYYYYY*LEN*WG,位置数据(见附录一); 平台：CS*YYYYYYYYYY*LEN*RG,定位类型,纬度,纬度标识,经度,经度标识<br>
	 * 说明: 用于 GPS 未定位状态下,通过基站方式向平台请求经纬度.
	 */
	public static String COMMAND_WG = "WG";
	
	public static String COMMAND_RG = "RG";
	
	public static String COMMAND_TKQ = "TKQ";
	
	/**********************平台发送********************************/
	/**
	 *数据上传间隔设置
	 */
	public static String COMMAND_UPLOAD = "UPLOAD";
	
	/**
	 * 中心号码设置
	 */
	public static String COMMAND_CENTER = "CENTER";
	
	/**
	 * 拨打电话
	 */
	public static String COMMAND_CALL = "CALL";
	
	/**
	 * 发送短信
	 */
	public static String COMMAND_SMS = "SMS";
	
	/**
	 * 监听
	 */
	public static String COMMAND_MONITOR = "MONITOR";
	/**
	 * SOS号码设置 SOS1
	 */
	public static String COMMAND_SOS1 = "SOS1";
	/**
	 * SOS号码设置 SOS2
	 */
	public static String COMMAND_SOS2 = "SOS2";
	/**
	 * SOS号码设置 SOS3
	 */
	public static String COMMAND_SOS3 = "SOS3";
	/**
	 * SOS短信报警开关
	 * [CS*YYYYYYYYYY*LEN*SOSSMS,0 或 1--->[3G*1451510836*0008*SOSSMS,0]
	 */
	public static String COMMAND_SOSSMS = "SOSSMS";
	/**
	 * 低电量报警开关  [CS*YYYYYYYYYY*LEN*LOWBAT,0 或 1]--->[3G*1451510836*0008*LOWBAT,1]
	 */
	public static String COMMAND_LOWBAT = "LOWBAT";
	
	/**
	 * 重启
	 */
	public static String COMMAND_RESET = "RESET";
	
	/**
	 * 定位指令
	 */
	public static String COMMAND_CR = "CR";
	
	/**
	 * 关机指令
	 */
	public static String COMMAND_POWEROFF = "POWEROFF";
	/**
	 * 取下手环报警开关 平台发送:[CS*YYYYYYYYYY*LEN*REMOVE,0 或者 1] [3G*1451510836*0008*REMOVE,1]
	 */
	public static String COMMAND_REMOVE = "REMOVE";
	/**
	 * 小红花个数设置指令
	 */
	public static String COMMAND_FLOWER = "FLOWER";
	/**
	 * 闹钟设置
	 */
	public static String COMMAND_REMIND = "REMIND";
	/**
	 * 找手表铃声设置
	 */
	public static String COMMAND_FIND = "FIND";
	/**
	 * 
	 */
	public static String COMMAND_3G = "3G";
	/**
	 *指令长度LEN
	 */
	public static String COMMAND_LEN_2 = "002";
	public static String COMMAND_LEN_3 = "003";
	public static String COMMAND_LEN_4 = "004";
	public static String COMMAND_LEN_5 = "005";
	public static String COMMAND_LEN_6 = "006";
	public static String COMMAND_LEN_7 = "007";
	public static String COMMAND_LEN_8 = "008";
	public static String COMMAND_LEN_9 = "009";
	public static String COMMAND_LEN_10 = "0010";
	public static String COMMAND_LEN_11 = "0011";
	public static String COMMAND_LEN_12 = "0012";
	public static String COMMAND_LEN_13 = "0013";
	public static String COMMAND_LEN_14 = "0014";
	public static String COMMAND_LEN_15 = "0015";
	//发送短信接口
	public static String COMMAND_LEN_001C = "001C";
	
	
	/**
	 * 分隔符 "*"
	 */
	public static String COMMAND_SEPARATOR_1 = "*";
	/**
	 * 分隔符 ","
	 */
	public static String COMMAND_SEPARATOR_2 = ",";
	/**
	 * 开始字符串
	 */
	public static String COMMAND_BEGIN_STR = "[app$[";
	/**
	 * 结束字符串
	 */
	public static String COMMAND_END_STR = "]]";
	
	
	
	
	
	
	
}
