package com.hzyw.iot.platform.devicemanager.domain.vo;

import com.hzyw.iot.platform.devicemanager.domain.device.DeviceAttributeDO;
import com.hzyw.iot.platform.devicemanager.domain.device.DeviceMethodDO;
import com.hzyw.iot.platform.models.equip.DeviceType;
import lombok.Data;

import java.util.List;
@Data
public class DeviceTypeInfoVO {
    private List<DeviceMethodDO> methods;
    private Integer manufacturerCode;
    private String typeCode;
    private String typeName;
    private Integer typeDomain;
    private List<DeviceAttributeDO> attrs;

    public String getTypeId() {
        return typeCode + DeviceType.TYPE_HYPHEN + typeDomain + DeviceType.TYPE_HYPHEN + manufacturerCode;
    }



}
