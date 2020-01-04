package com.hzyw.iot.platform.devicemanager.domain.device;

import lombok.Data;

import java.util.Date;

/**
 * 设备表 设备的静态属性
 *
 * @blame Android Team
 */
@Data
public class DeviceInfoDO {
    /**
     * 设备ID 平台定义的设备唯一ID,主键
     */
    private String deviceId;
    /**
     * 设备SN 设备硬件序列号,
     */
    private String serialNumber;
    /**
     * 设备名称 设备逻辑名
     */
    private String deviceName;
    /**
     * 设备型号编号 (小类，如:美的W901A)
     */
    private String deviceType;
    /**
     * 生产厂商编码
     */
    private Integer manufacturerCode;
    /**
     * 设备型号大类
     */
    private Integer deviceDomain;
    /**
     * 生产日期 出厂日期
     */
    private Date productDate;
    /**
     * 到期时间 有效期（过期时间）
     */
    private Date expireDate;
    /**
     * 生产批号 生产批号
     */
    private String batchNumber;
    /**
     * 商品条码 商品交易，售后等使用的商品条形码
     */
    private String barCode;
    /**
     * 设备MAC地址
     */
    private String macAddr;
    /**
     * 设备扩展元数据
     */
    private String metadata;
}
