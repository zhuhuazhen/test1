package com.hzyw.iot.platform.devicemanager.domain.vo;

import lombok.Data;

/**
 * 页面设备列表
 */
@Data
public class DeviceListVO {

    private String deviceId; //'设备ID 平台定义的设备唯一ID',
    private String deviceName; //'设备名称 设备逻辑名',
    private String deviceType; //'设备类型编号(主键，对应的是一个具体的型号)'
    private String manufacturerCode; //'生产商编号'
    private String manufacturerName; // '生产商名称(企业名称)'
    private String gatewayId; // '所属网关ID'
    private Integer registration; // '在网状态(1.在网/0.注销)'

}
