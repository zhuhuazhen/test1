package com.hzyw.iot.platform.devicemanager.domain.device;

import com.alibaba.fastjson.JSONObject;
import com.hzyw.iot.platform.models.equip.DeviceAttribute;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Map;

/**
 * @POJO 设备元模型
 * @blame Android Team
 */
@Data
public class DeviceAttributeDO {
    /**
     * 属性键
     */
    private String attrKey;
    /**
     * 属性名
     */
    private String attrName;
    /**
     * 数据类型
     */
    private String valueType;
    /**
     * 衍生单位
     */
    private String unit;
    /**
     * 标准单位
     */
    private String stdUnit;
    /**
     * 衍生/标准换算比,正数为乘数，负数为商数
     */
    private Integer ratio;
    /**
     * 预留元数据
     */
    private String metaData;
}
