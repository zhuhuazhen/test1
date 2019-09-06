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
    public static final String dev_plc_productCode = "plc_productCode"; //设备码
    
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
    
    //def自定义属性
	public static final String dev_plc_cfg_longitude ="plc_cfg_longitude";
	public static final String dev_plc_cfg_latitude ="plc_cfg_latitude";
	public static final String dev_plc_cfg_sq ="plc_cfg_sq";
	public static final String dev_plc_cfg_gksd_start ="plc_cfg_gksd_start";
	public static final String dev_plc_cfg_gksd_end ="plc_cfg_gksd_end";
    public static final String dev_plc_cfg_systime ="plc_cfg_systime"; //时钟
    public static final String dev_plc_cfg_step5_groupAtuo ="plc_cfg_step5_groupAtuo"; //组网个数
    public static final String dev_plc_node_devCode ="plc_node_devCode"; //设备码
    
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
    			"operator_42:42",
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
    	String[] plc_req_ack = {
    			"00:80", //控制码 ：应答码
    			"01:80",
    			"02:80",
    			"03:80",
    			"04:80",
    			"80:80"
    	};
    	String[] plc_ack_req = {
    			"80:00", //应答码:控制码  
    			"80:01",
    			"80:02",
    			"80:03",
    			"80:04",
    			"80:80"
    	};
    	
    	String[] plc_signls = {"offline:xxxxxxx"};
    	
    	allDevInfo.put(base_plc_port, plc_iotInfo_);
    	 
    	//定义一个PLC
    	String plc_id = ""; //ID
    	String plc_sn = "000000000100";  //集中器地址
    	
    	//这部分未来从DB中获取，现在配置在JSON文件
    	String plc_cfg_step1_longitude = "3980"; //经度           N22°32′43.86\\″   转化成整形表示  
    	String plc_cfg_step1_latitude = "-2546"; //维度     转化成整形表示
    	String plc_cfg_step1_sq= "-8"; //时区        转化成整形表示
    	String plc_cfg_step1_gksd_start= "8:10"; //光控时段-开始时分
    	String plc_cfg_step1_gksd_end= "8:10"; //光控时段  结束时分
    	
    	String plc_cfg_step2_timestamp= String.valueOf(System.currentTimeMillis()); //时钟      时间戳
    	String plc_cfg_step5_groupAtuo = "1";    //组网个数
    	
    	String  plc_node_devCode = ""; //设备码
    	// 
    	//属性
    	IotInfo.initDevAttribute(plc_iotInfo_, plc_sn+"_attribute", plc_attributers);//默认基本属性
    	plc_iotInfo_.get(plc_sn+"_attribute").put(dev_plc_plc_sn, plc_sn);
    	//自定义属性-初始化给值
    	IotInfo.initDevAttribute(plc_iotInfo_, plc_sn+"_defAttribute", new String[0]);
    	plc_iotInfo_.get(plc_sn+"_defAttribute").put(dev_plc_dataaccess_key, dev_plc_dataaccess_value);//接入类型
    	plc_iotInfo_.get(plc_sn+"_defAttribute").put(dev_plc_cfg_longitude, plc_cfg_step1_longitude); 
    	plc_iotInfo_.get(plc_sn+"_defAttribute").put(dev_plc_cfg_latitude, plc_cfg_step1_latitude); 
    	plc_iotInfo_.get(plc_sn+"_defAttribute").put(dev_plc_cfg_sq, plc_cfg_step1_sq); 
    	plc_iotInfo_.get(plc_sn+"_defAttribute").put(dev_plc_cfg_gksd_start, plc_cfg_step1_gksd_start); 
    	plc_iotInfo_.get(plc_sn+"_defAttribute").put(dev_plc_cfg_gksd_end, plc_cfg_step1_gksd_end); 
    	plc_iotInfo_.get(plc_sn+"_defAttribute").put(dev_plc_cfg_systime, plc_cfg_step2_timestamp); 
    	plc_iotInfo_.get(plc_sn+"_defAttribute").put(dev_plc_cfg_step5_groupAtuo, plc_cfg_step5_groupAtuo);  
    	plc_iotInfo_.get(plc_sn+"_defAttribute").put(dev_plc_node_devCode, plc_node_devCode);  
    	 

    	//方法
    	IotInfo.initMethod(plc_iotInfo_, plc_sn+"_method", plc_methods);
    	//命令码
    	IotInfo.initCmd(plc_iotInfo_, plc_sn+"_cmd", plc_methods);
    	//信号
    	IotInfo.initSignl(plc_iotInfo_, plc_sn+"_signl", plc_signls); 
    	//控制码和应答码对应关系
    	IotInfo.initMethod(plc_iotInfo_, plc_sn+"_req_ack", plc_req_ack);
    	IotInfo.initMethod(plc_iotInfo_, plc_sn+"_ack_req", plc_ack_req);
    	 
    	//灯具1(节点)
    	String plc_node_id = "1"; //ID
    	String plc_node_sn = "0000020004ee"; //灯具地址
    	String plc_node_type = "OL"; //jie点类型    单灯(OL)，双灯(TL)，HID ,LED
    	//String plc_sn = "xxxx";    所属集中器
    	String plc_node_productCode = "12H"; //设备码   ,设备码只和灯具有关 ，和产品类型对齐
    	String plc_node_group = "0";   //组号   1~255
    	
    	IotInfo.addNode(plc_iotInfo_, plc_sn, plc_node_sn, plc_node_attributers);
    	plc_iotInfo_.get(plc_node_sn+"_attribute").put(dev_plc_productCode, plc_node_productCode); //设备码,属于什么设备码需要手工给值 ，看下王剑那有没有定义有从他那引用即可
    	plc_iotInfo_.get(plc_node_sn+"_attribute").put(IotInfoConstant.dev_plc_dataaccess_key, IotInfoConstant.dev_plc_dataaccess_value);
    	
    	//灯具2(节点)  弄个假的验证看 自动配置集中器会出现啥状况
    	//待...
    	
    }
  //=================接入PLC 初始化信息========end=========
    /*public static void main(String[] args){
    	//设备列表
    	for(String att_type : plc_iotInfo_.keySet()){
    		System.out.println("======type :" + att_type);
    		for(String key :plc_iotInfo_.get(att_type).keySet()){
    			System.out.println("           -" + key + "/" +plc_iotInfo_.get(att_type).get(key));
    		}
    	}
    }*/
}
