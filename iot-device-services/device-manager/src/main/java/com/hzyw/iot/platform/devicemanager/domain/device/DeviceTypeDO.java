package com.hzyw.iot.platform.devicemanager.domain.device;

import lombok.Data;
import org.springframework.data.annotation.Id;

/**
 * POJO 设备类型
 *
 * @blame IOT Team
 */
@Data
public class DeviceTypeDO {
    private String typeCode; //'设备类型编号(主键，对应的是一个具体的型号)'
    private String typeName; //'设备名称(产品注册用的标准商品名)'
    private Integer deviceDomain; //'设备域(大类)--每一种大类有不同的领域模型'
    private Integer manufacturerCode; //'生产商编号(企业代码，注意不是企业名称)'
    private String softwareVersion; // '软件版本'
    private String hardwareVersion;// 硬件版本
    private String communicationPorts; //'支持的通信接口'
    private String supportProtocols; //'支持的协议集合'
    private String meta; //扩展元数据

}