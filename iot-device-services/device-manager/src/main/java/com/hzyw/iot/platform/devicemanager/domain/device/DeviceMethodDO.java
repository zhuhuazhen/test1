package com.hzyw.iot.platform.devicemanager.domain.device;

import lombok.Data;

import java.io.Serializable;

/**
 * DeviceMethodVO
 *
 * @blame Android Team
 */
@Data
public class DeviceMethodDO {
    private Integer methodId;        //方法名
    private String methodName;      //方法说明
    private String methodIn;        //设备输入属性
    private String methodOut;       //设备输出属性
    private String deviceType;      //设备型号
    private String methodDescription; //方法描述
}
