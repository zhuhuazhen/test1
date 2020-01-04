package com.hzyw.iot.platform.models.equip;

/**
 * @author by early
 * @blame IOT Team
 * @date 2019/9/29.
 */

public class DeviceDomain {

    private EquipmentFlag name;
    private int value;

    public EquipmentFlag getName() {
        return name;
    }

    public void setName(EquipmentFlag name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
