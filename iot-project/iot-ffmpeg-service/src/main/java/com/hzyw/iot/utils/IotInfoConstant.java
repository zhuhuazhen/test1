package com.hzyw.iot.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.hzyw.iot.commandManager.CommandManager;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
 
public class IotInfoConstant {
 
    /**
     * 缓存CommandManager对象，提供根据设备ID来停止此设备下所运行的线程
     * Map<设备ID, Map<消息ID, CommandManager>>
     * --消息ID表示 某次操作的唯一标识 ,也可以是任务ID CommandTasker.id
     */
    public static final Map<String, Map<String, CommandManager>> gloable_CommandManager = new HashMap<String, Map<String, CommandManager>>();
    /**
     * 端口 - 设备  关系
     *          
     *     1：allDevInfo= Map<port, Map<sn设备地址_attribute, Map<key设备字段, value设备字段默认值>>>
     *     1：allDevInfo= Map<port, Map<sn设备地址_defAttribute, Map<key设备字段, value设备字段默认值>>>
     *     2：allDevInfo= Map<port, Map<sn设备地址_method, Map<方法名, 命令码>>>
     *     3：allDevInfo= Map<port, Map<sn设备地址_cmd, Map<命令码, 方法名>>>
     *     4：allDevInfo= Map<port, Map<sn设备地址_signl, Map<key信号字段, value信号字段默认值>>>	   如节点离线，节点告警
     *    
     */
    public static final Map<String, Map<String, Map<String, String>>> allDevInfo = new HashMap<String, Map<String, Map<String, String>>>();
    private static final Map<String, Map<String, String>> plc_iotInfo_ = new HashMap<String, Map<String, String>>();
    
    
    /**
     * 根据port, plc_sn 查询 plc下对应的节点列表和详细信息
     * Map<port, Map<plc_sn, Map<节点属性, 值>>>   
     */
    public static final Map<String, Map<String, List<Map<String,String>>>> plc_relation_plcsnToNodelist = new HashMap<String, Map<String, List<Map<String,String>>>>();//plc和node关联关系
    
    
    /**
     * 根据 deviceID查询 , sn
     * 
     * Map<Map<id,  sn> deviceID肯定是唯一的
     */
    public static final Map<String, String> plc_relation_deviceToSn = new HashMap<String, String>();//plc和node关联关系

    //=================接入摄像头 初始化信息========start=========
    //公共
    public static final String base_plc_port = "12345";
    public static final String dev_plc_dataaccess_key = "dataAccess";    //接入类型
    public static final String dev_plc_dataaccess_value = "camera";      //每种接入，初始化数据的时候应该要指定接入类型
    
    //基本属性
    public static final String dev_base_uuid ="uuid";  //=deviceId
    public static final String dev_base_sn ="sn";
    public static final String dev_base_device_type_name ="device_type_name";
    public static final String dev_base_device_type_code ="device_type_code";
    public static final String dev_base_vendor_name ="vendor_name";
    public static final String dev_base_vendor_code ="vendor_code";
    public static final String dev_base_model ="model";
    public static final String dev_base_version_software ="version_software";
    public static final String dev_base_version_hardware ="version_hardware";
    public static final String dev_base_date_of_production ="date_of_production";
    public static final String dev_base_up_time ="up_time";
    public static final String dev_base_ipaddr_v4 ="ipaddr_v4";
    public static final String dev_base_ipaddr_v6 ="ipaddr_v6";
    public static final String dev_base_mac_addr ="mac_addr";
    public static final String dev_base_online ="online";
    public static final String dev_base_malfunction ="malfunction";
    
    static String[] base_attributers={dev_base_uuid,dev_base_sn,dev_base_device_type_name,dev_base_device_type_code, dev_base_vendor_name,dev_base_vendor_code 
			,dev_base_model ,dev_base_version_software , dev_base_version_hardware,dev_base_date_of_production , dev_base_up_time
			,dev_base_ipaddr_v4 ,dev_base_ipaddr_v6 ,dev_base_mac_addr ,dev_base_online ,dev_base_malfunction  };
    
    //摄像头-自定义属性
    /**
     * deviceId
     */
    public static final String dev_plc_plc_id ="plc_plc_id";  //deviceId
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
    public static final String dev_plc_c_power ="plc_c_power"; 			//集中器，C相功率
    public static final String dev_plc_c_pf ="plc_c_pf";       			//集中器，C相功率因素
    public static final String dev_plc_3_power ="plc_3_power"; 			//集中器，三相功率因素
    
    /*static String[] plc_def_attributers={dev_plc_plc_id,dev_plc_plc_sn,dev_plc_a_voltage,dev_plc_a_electric,dev_plc_a_power,dev_plc_a_pf
			,dev_plc_elect_energy,dev_plc_ad1_in,dev_plc_b_voltage,dev_plc_b_electric,dev_plc_b_power
			,dev_plc_b_pf,dev_plc_all_power,dev_plc_ad2_in,dev_plc_c_voltage,dev_plc_c_electric
			,dev_plc_c_power,dev_plc_c_pf,dev_plc_3_power};
     
	static String[] plc_node_def_attributers={dev_plc_node_sn,dev_plc_node_type,dev_plc_node_temperature,dev_plc_node_voltage_in
			,dev_plc_node_voltage_out,dev_plc_node_a_electri_in,dev_plc_node_a_electri_out,dev_plc_node_a_power
			,dev_plc_node_a_pf,dev_plc_node_a_brightness,dev_plc_node_a_status,dev_plc_node_a_onoff
			,dev_plc_node_devCode,dev_plc_node_group,dev_plc_node_cCode};
	*/
	
	
	public static String plc_json = "";
    public static String plc_node_json = ""; 
    public static void initData(){
    	try{}catch(Exception e){
    		System.out.println("===============初始化摄像头数据!!!===================");
    		e.printStackTrace();
    	}
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
