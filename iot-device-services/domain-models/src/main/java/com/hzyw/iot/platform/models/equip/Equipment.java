package com.hzyw.iot.platform.models.equip;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * 设备（泛指）基类
 *
 * @author by early
 * @blame IOT Team
 * @date 2019/8/4.
 */
public class Equipment implements Serializable {
    //设备序列号
    private final String serialNumber;
    //设备全局ID(根据序列号+算法生成)
    private String deviceId;
    //设备常用名
    private String deviceAlias;
    //设备类型
    private EquipmentType equipmentType;
    //生产日期
    private Date productionDate;
    //生产批号
    private String batchNumber;
    //生产厂家
    private Manufacturer manufacturer;
    //有效期
    private Date expireDate;

    public Equipment(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Equipment(String serialNumber,String deviceId) {
        this.serialNumber = serialNumber;
        this.deviceId = deviceId;
    }

//    public Equipment(String serialNumber,String deviceId,String deviceAlias) {
//        this.serialNumber = serialNumber;
//        this.deviceId = deviceId;
//        this.deviceAlias = deviceAlias;
//    }
//
//    public Equipment(String serialNumber,String deviceId,EquipmentType equipmentType) {
//        this.serialNumber = serialNumber;
//        this.deviceId = deviceId;
//        this.equipmentType = equipmentType;
//    }

    public Equipment(String serialNumber,String deviceId,String deviceAlias,EquipmentType equipmentType) {
        this.serialNumber = serialNumber;
        this.deviceId = deviceId;
        this.deviceAlias= deviceAlias;
        this.equipmentType = equipmentType;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceAlias() {
        return deviceAlias;
    }

    public void setDeviceAlias(String deviceAlias) {
        this.deviceAlias = deviceAlias;
    }

    public EquipmentType getEquipmentType(){
        return equipmentType;
    }

    public <T extends EquipmentType>T getEquipmentType(Class<T> equipmentTypeClass) {
        Objects.requireNonNull(equipmentType);
        return equipmentTypeClass.cast(equipmentType);
    }

    public void setEquipmentType(EquipmentType equipmentType) {
        this.equipmentType = equipmentType;
    }

    public Date getProductionDate() {
        return productionDate;
    }

    public void setProductionDate(Date productionDate) {
        this.productionDate = productionDate;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }
}
