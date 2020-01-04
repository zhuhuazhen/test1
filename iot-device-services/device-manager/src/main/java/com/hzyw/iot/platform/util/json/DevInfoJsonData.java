package com.hzyw.iot.platform.util.json;

import lombok.Data;

/**
 * @author by early
 * @blame IOT Team
 * @date 2019/9/22.
 */

@Data
public class DevInfoJsonData {

    private String uuid;
    //设备ID
    private String sn;
    //设备二维码
    private String device_type_name;
    //设备型号名称
    private Integer device_type_code;
    //设备大类
    private String vendor_name;
    //生产商名称
    private Integer vendor_code;
    //生产商编码
    private String model;
    //设备型号编号
    private String version_software;
    //软件版本
    private String version_hardware;
    //硬件版本
    private String date_of_production;
    //生产日期
    private Integer up_time;
    //有效期
    private String ipaddr_v4;
    //IPv4地址
    private String ipaddr_v6;
    //IPv6地址
    private String mac_addr;
    //MAC地址
    private Integer malfunction;
    //故障编码
}
