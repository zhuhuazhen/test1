package com.hzyw.iot.platform.devicemanager.domain.vo;

import com.hzyw.iot.platform.devicemanager.domain.device.DeviceAttributeDO;
import lombok.Data;

/**
 * @author by early
 * @blame IOT Team
 * @date 2019/9/19.
 */

@Data
public class DeviceAttrVO {
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

}
