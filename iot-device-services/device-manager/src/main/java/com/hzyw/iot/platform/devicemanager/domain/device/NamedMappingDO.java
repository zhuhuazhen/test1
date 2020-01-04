package com.hzyw.iot.platform.devicemanager.domain.device;

import lombok.Data;

/**
 * @author by early
 * @blame IOT Team
 * @date 2019/8/30.
 */

@Data
public class NamedMappingDO {
    private Integer id; // '条目ID'(主键。自增)
    private String attrKey; //'属性键(产品注册用的标准商品名)'
    private String keyAlias; //'属性别名(与标准模型中含义相同但是名称不同的定义，用于设备灵活接入)'
    private String information; //'必要描述(描述与备注)'
    private String deviceDomain; //'设备域（大类）--区分灯还是屏的模型共有属性'
    private String deviceType; //'设备类型编号(不同型号的特定属性)'
}
