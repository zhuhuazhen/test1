package com.hzyw.iot.utils;

/**
 * PLC接入
 * 	方法和命令码 对应关系
 * 
 * @author Administrator
 *
 */
public class IotInfoMethod {
  
    public static final	String[] plc_methods = {"operator_70:70",
    			"operator_71:71",
    			"operator_73:73",
    			"operator_82:82",
    			"operator_83:83",
    			"operator_84:84",
    			"operator_8c:8c",
    			"operator_8e:8e",
    			"operator_8f:8f",
    			"operator_96:96",
    			"operator_97:97",
    			"operator_98:98",
    			"operator_99:99",
    			"operator_f0:f0",
    			"operator_f1:f1",
    			"operator_f2:f2",
    			"operator_f3:f3",
    			"operator_f4:f4",
    			"operator_f5:f5",
    			"operator_f6:f6",
    			"set_brightness:42",  //调灯光
    			"set_onoff:42",       //开关
    			"operator_f7:f7",
    			"operator_45:45",
    			"operator_fb:fb",
    			"operator_fc:fc",
    			"operator_fd:fd",
    			"operator_9a:9a",
    			"operator_9b:9b",
    			"operator_9c:9c",
    			"operator_60:60",
    			"operator_61:61",
    			"operator_46:46",
    			"operator_fe:fe",
    			"operator_62:62",
    			"operator_63:63",
    			"operator_66:66",
    			"operator_67:67",
    			"operator_9e:9e",
    			"operator_9d:9d",
    			"operator_69:69",
    			"operator_4a:4a",
    			"operator_f8:f8",
    			"operator_f9:f9",
    			"operator_6a:6a",
    			"operator_6b:6b",
    			"operator_6c:6c",
    			"operator_6d:6d",
    			"operator_6f:6f",
    			"operator_47:47",
    			"operator_48:48",
    			"operator_49:49",
    			"operator_4b:4b",
    			"operator_4c:4c"
    	 };

	
	//双向映射，方便根据数据
    public static final String[] plc_req_ack = {
			"00:80", //控制码 ：应答码
			"01:80",
			"02:80",
			"03:80",
			"04:80",
			"80:80"
	};
    
    public static final String[] plc_ack_req = {
			"80:00", //应答码:控制码  
			"80:01",
			"80:02",
			"80:03",
			"80:04",
			"80:80"
	};
	
    public static final String[] plc_signls = {"offline:xxxxxxx"};
    
}
