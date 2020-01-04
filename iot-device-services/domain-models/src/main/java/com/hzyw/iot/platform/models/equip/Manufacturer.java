package com.hzyw.iot.platform.models.equip;

import java.io.Serializable;

/**
 * @author by early
 * @blame IOT Team
 * @date 2019/8/8.
 */
public class Manufacturer implements Serializable {
    private Integer manufacturerCode;    //企业代码
    private String manufacturerName;    //企业名称
    private String address;         //企业地址
    private String contactInfo;     //联系方式

    public Manufacturer(Integer manufacturerCode) {
        this.manufacturerCode = manufacturerCode;
    }

    public Integer getManufacturerCode() {
        return manufacturerCode;
    }

    public void setManufacturerCode(Integer manufacturerCode) {
        this.manufacturerCode = manufacturerCode;
    }

    public String getManufacturerName() {
        return manufacturerName;
    }

    public void setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }
}
