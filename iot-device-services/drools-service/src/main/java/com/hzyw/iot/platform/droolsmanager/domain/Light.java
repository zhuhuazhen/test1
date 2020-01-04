package com.hzyw.iot.platform.droolsmanager.domain;


public class Light  extends Device {

    //开关  关：0; 开：1

    private int lightpower;
    public Light(int lightpower) {
        this.lightpower = lightpower;
    }

    public int getLightpower() {
        return lightpower;
    }

    public void setLightpower(int lightpower) {
        this.lightpower = lightpower;
    }



}
