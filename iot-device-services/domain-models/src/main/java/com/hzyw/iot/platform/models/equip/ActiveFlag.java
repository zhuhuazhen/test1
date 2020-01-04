package com.hzyw.iot.platform.models.equip;

import java.io.Serializable;

/**
 * @author by early
 * @blame IOT Team
 * @date 2019/8/8.
 */
public enum ActiveFlag implements Serializable {
    UNACTIVATED("未激活", 0), //表示为登记注册但未入网。
    ACTIVATE("在网", 1),  //表示为入网成功并在网运行。
    DETACHED("离网", -1);  //表示为离网并注销。

    private String description;
    private int index;

    ActiveFlag(String name, int index) {
        this.description = name;
        this.index = index;
    }

    public static String getDescription(int index) {
        for (ActiveFlag flag : ActiveFlag.values()) {
            if (flag.getIndex() == index) {
                return flag.description;
            }
        }
        return null;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return this.index + "_" + this.description;
    }
}
