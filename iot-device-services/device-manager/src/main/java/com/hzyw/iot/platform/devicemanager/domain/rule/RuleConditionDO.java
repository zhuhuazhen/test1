package com.hzyw.iot.platform.devicemanager.domain.rule;

import lombok.Data;

import java.util.Date;


/**
 * 规则 条件管理
 */
@Data
public class RuleConditionDO {
    private String code; //编码
    private Integer deviceType; //设备类型
    private String deviceId; //设备ID
    private String description; //条件描述
    private String content; //条件内容(选择方法)
    private Integer eventType; //事件类型（1、警告,2、联动）
    private String creater; //创建人
    private Date createTime; //创建时间
    private String updater;//更新人
    private Date updateTime;//更新时间
}
