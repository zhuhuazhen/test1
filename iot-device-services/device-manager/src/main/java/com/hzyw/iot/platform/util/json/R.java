package com.hzyw.iot.platform.util.json;

/**
 * R
 *
 * @blame Android Team
 */
public class R {

//    public static final String UUID = "uuid";   //设备ID
//    public static final String SN = "sn";       //设备二维码
//    public static final String DEVICE_TYPE_NAME = "device_type_name";   //设备型号名称
//    public static final String DEVICE_TYPE_CODE = "device_type_code";   //设备大类
//    public static final String VENDOR_NAME = "vendor_name";         //生产商名称
//    public static final String VENDOR_CODE = "vendor_code";         //生产商编码
//    public static final String MODEL = "model";             //设备型号编号
//    public static final String VERSION_SOFTWARE = "version_software";       //软件版本
//    public static final String VERSION_HARDWARE = "version_hardware";       //硬件版本
//    public static final String DATE_OF_PRODUCTION = "date_of_production";   //生产日期
//    public static final String UP_TIME = "up_time";     //有效期
//    public static final String IP_ADDR_V4 = "ipaddr_v4";     //IPv4地址
//    public static final String IP_ADDR_V6 = "ipaddr_v6";     //IPv6地址
//    public static final String MAC_ADDR = "mac_addr";          //MAC地址
//    public static final String ONLINE = "online";           //0表示离线，大于0表示在线
//    public static final String MALFUNCTION = "malfunction";     //故障编码
//    public static final String TYPE = "type";           //属性名称
//    public static final String VALUE = "value";         //属性值
//    public static final String COMPANY = "company";     //属性单位

    /**报文类型*/
    public static final String TYPE = "type";
    public static final String TYPE_REQUEST ="request";
    public static final String TYPE_RESPONSE ="response";
    public static final String TYPE_DEV_SIGNAL_RESPONSE ="devSignalResponse";
    public static final String TYPE_METRIC_INFO_RESPONSE ="metricInfoResponse";
    public static final String TYPE_DEV_INFO_RESPONSE = "devInfoResponse";
    public static final String GW_ID = "gwId";
    public static final String MSG_ID = "msgId";
    /**报文类型*/
    public static final String MESSAGE_CODE_INT = "messageCode";
    public static final String TIMESTAMP_LONG = "timestamp";
    public static final String DATA = "data";
    public static final String DATA_ID = "id";
    public static final String DATA_ATTRIBUTES = "attributers";
    public static final String DATA_DEFINED_ATTRIBUTES = "definedAttributers";  //注意报文里这个单词拼写错误，但是暂时不要动了，没办法
    public static final String DATA_METHODS = "methods";
    public static final String DATA_DEFINED_METHODS = "definedMethods";
    public static final String DATA_SIGNALS = "signals";
    public static final String DATA_TAGS = "tags";

    public static final String SDK_CALLBACK_URI = "/sdk/callback";
}
