package com.hzyw.iot.platform.droolsmanager.domain;

import java.io.Serializable;

public class Device implements Serializable {

    //设备全局ID(根据序列号+算法生成)
    private String deviceId;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

}
