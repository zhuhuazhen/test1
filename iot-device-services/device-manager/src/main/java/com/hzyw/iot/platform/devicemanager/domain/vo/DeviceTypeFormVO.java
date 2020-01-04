package com.hzyw.iot.platform.devicemanager.domain.vo;

import com.hzyw.iot.platform.models.equip.DeviceType;
import lombok.Data;

import java.util.List;

/**
 * @author by early
 * @blame IOT Team
 * @date 2019/9/18.
 */
@Data
public class DeviceTypeFormVO {
    private List<MethodFormVO> methods;
    private Integer manufacturerCode;
    private String typeCode;
    private String typeName;
    private Integer typeDomain;
    private List<String> attrIds;

    public String getTypeId() {
        return typeCode + DeviceType.TYPE_HYPHEN + typeDomain + DeviceType.TYPE_HYPHEN + manufacturerCode;
    }

    @Data
    public static class MethodFormVO {
        private String methodName;
        private String methodIn;
        private String methodOut;
        private String description;
    }
}
