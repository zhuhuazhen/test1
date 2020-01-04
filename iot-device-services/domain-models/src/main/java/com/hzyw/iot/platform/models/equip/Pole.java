package com.hzyw.iot.platform.models.equip;

/**
 * @author by early
 * @blame IOT Team
 * @date 2019/8/8.
 */
public final class  Pole extends Equipment {
    //标签
    public static final Enum FLAG = EquipmentFlag.POLE;
    //产品型号代号
    private String typeCode;
    //经度
    private String longitude;
    //纬度
    private String latitude;
    //地图格式
    private String locationType;
    //是否虚拟灯杆
    private boolean isVirtual;
    //接入状态


    public Pole(String serialNumber) {
        super(serialNumber);
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    public boolean isVirtual() {
        return isVirtual;
    }

    public void setVirtual(boolean virtual) {
        isVirtual = virtual;
    }
}
