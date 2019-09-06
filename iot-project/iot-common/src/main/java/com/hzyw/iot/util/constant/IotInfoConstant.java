package com.hzyw.iot.util.constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hzyw.iot.vo.dataaccess.DevInfoDataVO;
import com.hzyw.iot.vo.dc.IotInfo;
 
public class IotInfoConstant {


    /**
     * 端口 - 设备  关系
     *          
     *     1：allDevInfo= Map<port, Map<sn设备地址_attribute, Map<key设备字段, value设备字段默认值>>>
     *     2：allDevInfo= Map<port, Map<sn设备地址_method, Map<方法名, 命令码>>>
     *     3：allDevInfo= Map<port, Map<sn设备地址_cmd, Map<命令码, 方法名>>>
     *     4：allDevInfo= Map<port, Map<sn设备地址_signl, Map<key信号字段, value信号字段默认值>>>	   如节点离线，节点告警
     */
    public static final Map<String, Map<String, Map<String, String>>> allDevInfo = new HashMap<String, Map<String, Map<String, String>>>();
    private static final Map<String, Map<String, String>> plc_iotInfo_ = new HashMap<String, Map<String, String>>();
    
    //=================接入PLC 初始化信息========start=========
     
    //公共
    public static final String base_plc_port = "12345";
    public static final String dev_plc_dataaccess_key = "dataAccess"; //接入类型
    public static final String dev_plc_dataaccess_value = "plc"; //每种接入，初始化数据的时候应该要指定接入类型
    public static final String dev_plc_devCode = "plc_devCode"; //设备码
    
    //PLC集中器
    public static final String dev_plc_plc_sn ="plc_sn"; //集中器地址，唯一标识
    //public static final String dev_plc_objectName ="plc_objectName"; //集中器名称字段
    public static final String dev_plc_a_voltage ="plc_a_voltage"; 			//集中器，A相电压
    public static final String dev_plc_a_electric ="plc_a_electric"; //集中器，A相电流
    public static final String dev_plc_a_power ="plc_a_power"; 				//集中器，A相功率:
    public static final String dev_plc_a_pf ="plc_a_pf"; 					//集中器，A相功率因素
    public static final String dev_plc_elect_energy ="plc_elect_energy"; //集中器，电能
    public static final String dev_plc_ad1_in ="plc_ad1_in"; //集中器，AD1输入
    public static final String dev_plc_b_voltage ="plc_b_voltage";          //集中器，B相电压
    public static final String dev_plc_b_electric ="plc_b_electric";        //集中器，B相电流
    public static final String dev_plc_b_power ="plc_b_power"; 				//集中器，B相功率
    public static final String dev_plc_b_pf ="plc_b_pf"; 					//集中器，B相功率因素
    public static final String dev_plc_all_power ="plc_all_power"; 		//集中器，总功率
    public static final String dev_plc_ad2_in ="plc_ad2_in"; 			//集中器，AD2输入
    public static final String dev_plc_c_voltage ="plc_c_voltage";   //集中器，C相电压
    public static final String dev_plc_c_electric ="plc_c_electric"; //集中器，C相电流
    public static final String dev_plc_c_power ="plc_c_power"; //集中器，C相功率
    public static final String dev_plc_c_pf ="plc_c_pf";       //集中器，C相功率因素
    public static final String dev_plc_3_power ="plc_3_power"; //集中器，三相功率因素
    
    //灯
    public static final String dev_plc_node_sn ="plc_node_sn"; //节点地址
    public static final String dev_plc_node_type ="plc_node_type";  //节点类型  :  单灯，双灯，HID,LED
    public static final String dev_plc_node_temperature ="plc_node_temperature";//温度
    public static final String dev_plc_node_voltage_in ="plc_node_voltage_in";//输入电压
    public static final String dev_plc_node_voltage_out ="plc_node_voltage_out";//输出电压
    public static final String dev_plc_node_a_electri_in ="plc_node_a_electri_in";//A路输入电流
    public static final String dev_plc_node_a_electri_out ="plc_node_a_electri_out";//A路输出电流
    public static final String dev_plc_node_a_power ="plc_node_a_power";//A路有功功率
    public static final String dev_plc_node_a_pf ="plc_node_a_pf";//A路功率因数
    public static final String dev_plc_node_a_lighting ="plc_node_a_lighting";//A亮度
    public static final String dev_plc_node_a_status ="plc_node_a_status";//状态 
    
    static{
    	//加载PLC硬件设备属性参数
    	String[] plc_attributers={dev_plc_plc_sn,dev_plc_a_voltage,dev_plc_a_electric,dev_plc_a_power,dev_plc_a_pf
				,dev_plc_elect_energy,dev_plc_ad1_in,dev_plc_b_voltage,dev_plc_b_electric,dev_plc_b_power
				,dev_plc_b_pf,dev_plc_all_power,dev_plc_ad2_in,dev_plc_c_voltage,dev_plc_c_electric
				,dev_plc_c_power,dev_plc_c_pf,dev_plc_3_power};
    	
    	//加载灯节点设备属性参数
    	String[] plc_node_attributers={dev_plc_node_sn,dev_plc_node_type,dev_plc_node_temperature,dev_plc_node_voltage_in
    			,dev_plc_node_voltage_out,dev_plc_node_a_electri_in,dev_plc_node_a_electri_out,dev_plc_node_a_power
    			,dev_plc_node_a_pf,dev_plc_node_a_lighting,dev_plc_node_a_status};
    	
    	String[] plc_methods = {"operator_70:70",
    			"operator_71:71",
    			"operator_73:73",
    			"operator_82:82",
    			"operator_83:83",
    			"operator_84:84",
    			"operator_8C:8C",
    			"operator_8E:8E",
    			"operator_8F:8F",
    			"operator_96:96",
    			"operator_97:97",
    			"operator_98:98",
    			"operator_99:99",
    			"operator_F0:F0",
    			"operator_F1:F1",
    			"operator_F2:F2",
    			"operator_F3:F3",
    			"operator_F4:F4",
    			"operator_F5:F5",
    			"operator_F6:F6",
    			"operator_42:42",
    			"operator_F7:F7",
    			"operator_45:45",
    			"operator_FB:FB",
    			"operator_FC:FC",
    			"operator_FD:FD",
    			"operator_9A:9A",
    			"operator_9B:9B",
    			"operator_9C:9C",
    			"operator_60:60",
    			"operator_61:61",
    			"operator_46:46",
    			"operator_FE:FE",
    			"operator_62:62",
    			"operator_63:63",
    			"operator_66:66",
    			"operator_67:67",
    			"operator_9E:9E",
    			"operator_9D:9D",
    			"operator_69:69",
    			"operator_4A:4A",
    			"operator_F8:F8",
    			"operator_F9:F9",
    			"operator_6A:6A",
    			"operator_6B:6B",
    			"operator_6C:6C",
    			"operator_6D:6D",
    			"operator_6F:6F",
    			"operator_47:47",
    			"operator_48:48",
    			"operator_49:49",
    			"operator_4B:4B",
    			"operator_4C:4C"};
    	
    	String[] plc_signls = {"offline:xxxxxxx"};
    	
    	allDevInfo.put(base_plc_port, plc_iotInfo_);
    	//定义一个PLC
    	String plc_sn = "000000000100";
    	//属性
    	IotInfo.initDevAttribute(plc_iotInfo_, plc_sn+"_attribute", plc_attributers);//所有属性
    	plc_iotInfo_.get(plc_sn+"_attribute").put(dev_plc_plc_sn, plc_sn); //自定义属性给值 ,有的属性上报的时候就已经默认有值,通过手工配置默认值
    	plc_iotInfo_.get(plc_sn+"_attribute").put(dev_plc_dataaccess_key, dev_plc_dataaccess_value);//接入类型
    	plc_iotInfo_.get(plc_sn+"_attribute").put(dev_plc_devCode, ""); //设备码,属于什么设备码需要手工给值 ，看下王剑那有没有定义有从他那引用即可
    	//方法
    	IotInfo.initMethod(plc_iotInfo_, plc_sn+"_method", plc_methods);
    	//命令码
    	IotInfo.initCmd(plc_iotInfo_, plc_sn+"_cmd", plc_methods);
    	//信号
    	IotInfo.initSignl(plc_iotInfo_, plc_sn+"_signl", plc_signls); 
    	
    	 
    	//实例化灯(节点)
    	String plc_node_sn = "xxxx";
    	IotInfo.addNode(plc_iotInfo_, plc_sn, plc_node_sn, plc_node_attributers);
    	
    }
  //=================接入PLC 初始化信息========end=========
    
}
