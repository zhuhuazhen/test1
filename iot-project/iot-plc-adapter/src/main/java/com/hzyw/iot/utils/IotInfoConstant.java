package com.hzyw.iot.utils;

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

    //=================接入PLC 初始化信息========start=========
     
    //公共
    public static final String base_plc_port = "12345";
    public static final String dev_plc_dataaccess_key = "dataAccess"; //接入类型
    public static final String dev_plc_dataaccess_value = "plc"; //每种接入，初始化数据的时候应该要指定接入类型
    
    //设备-基本属性 （直接把盒子的搬过来了，PLC集中器，灯具都应该有这基本属性）
    /*"uuid": "10308f00b204e9800998200dffff70a0",  //等价于 deviceId
    "sn": "CLCC40002B8A",    //sn
    "device_type_name": "LED screen",
    "device_type_code": 4144,       
    "vendor_name": "Colorlight interconnection", //生产厂家 
    "vendor_code": 8205,      //生产厂家 编号  
    "model": "c4",            //设备型号
    "version_software": "1.52.6",      //软件版本
    "version_hardware": "c4-1.52.6",  //硬件版本
    "date_of_production": "2019-08-27",  //生产日期
    "up_time": 0,    //设备生命周期
    "ipaddr_v4": "192.168.3.104", //IP4
    "ipaddr_v6": "2001:0db8:85a3:08d3:1319:8a2e:0370:7344", //IP6
    "mac_addr": "C1:B2:B2:C2:0B:0A",    //MCK地址
    "online": 0,   //在线1 0离线
    "malfunction": 0   //故障编码
    */   
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
    
    //PLC集中器-属性
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
    
    static String[] plc_def_attributers={dev_plc_plc_id,dev_plc_plc_sn,dev_plc_a_voltage,dev_plc_a_electric,dev_plc_a_power,dev_plc_a_pf
			,dev_plc_elect_energy,dev_plc_ad1_in,dev_plc_b_voltage,dev_plc_b_electric,dev_plc_b_power
			,dev_plc_b_pf,dev_plc_all_power,dev_plc_ad2_in,dev_plc_c_voltage,dev_plc_c_electric
			,dev_plc_c_power,dev_plc_c_pf,dev_plc_3_power};
    
    //集中器属性(设备接入前配置引入的属性)
	public static final String dev_plc_cfg_longitude ="plc_cfg_longitude";
	public static final String dev_plc_cfg_latitude ="plc_cfg_latitude";
	public static final String dev_plc_cfg_sq ="plc_cfg_sq";
	public static final String dev_plc_cfg_gksd_start ="plc_cfg_gksd_start";
	public static final String dev_plc_cfg_gksd_end ="plc_cfg_gksd_end";
    public static final String dev_plc_cfg_systime ="plc_cfg_systime"; //时钟
    public static final String dev_plc_cfg_step5_groupAtuo ="plc_cfg_step5_groupAtuo"; //组网个数   
    
    static String[] plc_def_attributers_initcfg={ dev_plc_cfg_longitude, dev_plc_cfg_latitude, dev_plc_cfg_sq ,dev_plc_cfg_gksd_start 
    		,dev_plc_cfg_gksd_end,dev_plc_cfg_systime ,dev_plc_cfg_step5_groupAtuo };
     
    
    //灯具属性
    public static final String dev_plc_node_id ="plc_node_id"; //deviceId 
    public static final String dev_plc_node_sn ="plc_node_sn"; //节点地址
    //public static final String dev_plc_node_type ="plc_node_type";  //节点类型  :jie点类型    单灯(OL)，双灯(TL)，HID ,LED
    public static final String dev_plc_node_temperature ="plc_node_temperature";//温度
    public static final String dev_plc_node_voltage_in ="plc_node_voltage_in";//输入电压
    public static final String dev_plc_node_voltage_out ="plc_node_voltage_out";//输出电压
    public static final String dev_plc_node_a_electri_in ="plc_node_a_electri_in";//A路输入电流	
    public static final String dev_plc_node_a_electri_out ="plc_node_a_electri_out";//A路输出电流
    public static final String dev_plc_node_a_power ="plc_node_a_power";//A路有功功率
    public static final String dev_plc_node_a_pf ="plc_node_a_pf";//A路功率因数
    //public static final String dev_plc_node_a_lighting ="plc_node_a_lighting";//A亮度
    public static final String dev_plc_node_a_brightness ="plc_node_a_brightness";//A亮度   0-100
    //灯具属性  (设备接入前配置引入的属性)
	public static final String dev_plc_node_type ="plc_node_type";        //控制类型   单灯A(01)，单灯B(02),双灯(03) 
	public static final String dev_plc_node_devCode ="plc_node_devCode";  //设备码
	public static final String dev_plc_node_group ="plc_node_group";  	  //节点所属组
	//灯具属性 (调等协议引入)
	public static final String dev_plc_node_cCode ="plc_node_cCode";  //控制码
    public static final String dev_plc_node_a_status ="plc_node_a_status";// 在线状态  0-离线 1-在线
    public static final String dev_plc_node_a_onoff ="plc_node_a_onoff";  // 开关状态  0-开灯 1-关灯
	
	static String[] plc_node_def_attributers={dev_plc_node_sn,dev_plc_node_type,dev_plc_node_temperature,dev_plc_node_voltage_in
			,dev_plc_node_voltage_out,dev_plc_node_a_electri_in,dev_plc_node_a_electri_out,dev_plc_node_a_power
			,dev_plc_node_a_pf,dev_plc_node_a_brightness,dev_plc_node_a_status,dev_plc_node_a_onoff
			,dev_plc_node_devCode,dev_plc_node_group,dev_plc_node_cCode};
     
    static{
    	try{
    		allDevInfo.put(base_plc_port, plc_iotInfo_);
       	 
        	//========new PLC设备======
        	String plc_id = "1000-f82d132f9bb018ca-2001-ffff-d28a"; //deviceID  有算法计算
        	String plc_sn = "000000000100";  //集中器地址
        	
        	//这部分未来从DB中获取，现在配置在JSON文件
        	String plc_cfg_step1_longitude = "3980"; //经度           N22°32′43.86\\″   转化成整形表示  
        	String plc_cfg_step1_latitude = "-2546"; //维度     转化成整形表示
        	String plc_cfg_step1_sq= "-8"; //时区        转化成整形表示
        	String plc_cfg_step1_gksd_start= "8:10"; //光控时段-开始时分
        	String plc_cfg_step1_gksd_end= "8:10"; //光控时段  结束时分
        	
        	String plc_cfg_step2_timestamp= String.valueOf(System.currentTimeMillis()); //时钟      时间戳
        	String plc_cfg_step5_groupAtuo = "1";    //组网个数
        	
        	//基本属性
        	IotInfo.initDevAttribute(plc_iotInfo_, plc_sn+"_attribute", base_attributers);  
        	plc_iotInfo_.get(plc_sn+"_attribute").put(dev_plc_plc_sn, plc_sn);
        	//def属性
        	IotInfo.initDevAttribute(plc_iotInfo_, plc_sn+"_defAttribute", plc_def_attributers);//硬件自带
        	IotInfo.initDevAttribute(plc_iotInfo_, plc_sn+"_defAttribute", plc_def_attributers_initcfg);//设备接入前配置引入的属性
        	plc_iotInfo_.get(plc_sn+"_defAttribute").put(dev_plc_plc_id, plc_id);   //deviceId
        	plc_iotInfo_.get(plc_sn+"_defAttribute").put(dev_plc_dataaccess_key, dev_plc_dataaccess_value); //接入类型
        	plc_iotInfo_.get(plc_sn+"_defAttribute").put(dev_plc_cfg_longitude, plc_cfg_step1_longitude); 
        	plc_iotInfo_.get(plc_sn+"_defAttribute").put(dev_plc_cfg_latitude, plc_cfg_step1_latitude); 
        	plc_iotInfo_.get(plc_sn+"_defAttribute").put(dev_plc_cfg_sq, plc_cfg_step1_sq); 
        	plc_iotInfo_.get(plc_sn+"_defAttribute").put(dev_plc_cfg_gksd_start, plc_cfg_step1_gksd_start); 
        	plc_iotInfo_.get(plc_sn+"_defAttribute").put(dev_plc_cfg_gksd_end, plc_cfg_step1_gksd_end); 
        	plc_iotInfo_.get(plc_sn+"_defAttribute").put(dev_plc_cfg_systime, plc_cfg_step2_timestamp); 
        	plc_iotInfo_.get(plc_sn+"_defAttribute").put(dev_plc_cfg_step5_groupAtuo, plc_cfg_step5_groupAtuo);  
        	//---->其他def属性
        	//...
        	
        	//方法
        	IotInfo.initMethod(plc_iotInfo_, plc_sn+"_method", IotInfoMethod.plc_methods);
        	//命令码
        	IotInfo.initCmd(plc_iotInfo_, plc_sn+"_cmd", IotInfoMethod.plc_methods);
        	//信号
        	IotInfo.initSignl(plc_iotInfo_, plc_sn+"_signl", IotInfoMethod.plc_signls); 
        	//控制码和应答码对应关系
        	IotInfo.initMethod(plc_iotInfo_, plc_sn+"_req_ack", IotInfoMethod.plc_req_ack);
        	IotInfo.initMethod(plc_iotInfo_, plc_sn+"_ack_req", IotInfoMethod.plc_ack_req);
        	 
        	 
        	//===========new 灯具1 =========
        	String plc_node_id = "1010-3f7b3eb6bffe6fb1-2009-ffff-be7"; //deviceID
        	String plc_node_sn = "0000020004ee"; //灯具地址  0000020004ee
        	String plc_node_type = "01";      // 
        	String plc_node_devCode = "12";   //设备码   ,设备码只和灯具有关 ，和产品类型对齐
        	String plc_node_group = "1";      //组号   1~255
        	
        	//----查询节点信息
        	IotInfo.initDevAttribute(plc_iotInfo_, plc_node_sn+"_attribute", base_attributers);  //基本属性
        	IotInfo.initDevAttribute(plc_iotInfo_, plc_node_sn+"_defAttribute", plc_node_def_attributers);//自定义属性
    		plc_iotInfo_.get(plc_node_sn+"_defAttribute").put(IotInfoConstant.dev_plc_plc_sn, plc_sn);
    		plc_iotInfo_.get(plc_node_sn+"_defAttribute").put(IotInfoConstant.dev_plc_node_sn, plc_node_sn);
        	plc_iotInfo_.get(plc_node_sn+"_defAttribute").put(IotInfoConstant.dev_plc_dataaccess_key, IotInfoConstant.dev_plc_dataaccess_value);
        	plc_iotInfo_.get(plc_node_sn+"_defAttribute").put(plc_node_id, plc_node_id);  //deviceId
        	plc_iotInfo_.get(plc_node_sn+"_defAttribute").put(dev_plc_node_type, plc_node_type); 
        	plc_iotInfo_.get(plc_node_sn+"_defAttribute").put(dev_plc_node_devCode, plc_node_devCode); 
        	 
        	//----plc_sn和node关系
        	Map<String,List<Map<String, String>>> plc_nodes = new HashMap<String,List<Map<String, String>>>();
        	plc_relation_plcsnToNodelist.put(base_plc_port, plc_nodes);
        	List<Map<String, String>> nodelist = new ArrayList<Map<String, String>>();
        	Map<String, String> it = new HashMap<String,String>();
        	it.put(dev_plc_plc_sn, plc_sn);  //sn (plc)
        	it.put(dev_plc_node_sn, plc_node_sn);  //sn (node)
        	it.put(dev_plc_node_type, plc_node_type);  //设备类型
        	it.put(dev_plc_node_devCode, plc_node_devCode); //设备码
        	it.put(dev_plc_node_group, plc_node_group); //组   必须 1~255
        	nodelist.add(it);
        	plc_nodes.put(plc_sn , nodelist);          
        	
        	//---deviceId和SN关系
        	plc_relation_deviceToSn.put(plc_id, plc_sn);  
        	plc_relation_deviceToSn.put(plc_node_id, plc_node_sn);
        	
        	//灯具2(节点) 
    	}catch(Exception e){
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
