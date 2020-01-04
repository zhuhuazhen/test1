package com.hzyw.iot.platform.droolsmanager.domain;


public class BigScreen extends Device {


    //亮度

    private int brightness;
    private int power;
    private String name;
    public BigScreen(int brightness,int power){
        this.brightness = brightness;
        this.power = power;
    }

    public int getBrightness() {
        return brightness;
    }

    public void setBrightness(int brightness) {
        this.brightness = brightness;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
