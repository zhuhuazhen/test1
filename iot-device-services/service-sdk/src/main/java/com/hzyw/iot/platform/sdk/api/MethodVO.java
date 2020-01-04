package com.hzyw.iot.platform.sdk.api;

import java.util.Map;

/**
 * @author by early
 * @blame IOT Team
 * @date 2019/8/21.
 */
public class MethodVO {
    private String name;
    private Map<String,Object> paras;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Object> getParas() {
        return paras;
    }

    public void setParas(Map<String, Object> paras) {
        this.paras = paras;
    }

    @Override
    public String toString() {
        return "MethodVO{" +
                "name='" + name + '\'' +
                ", paras=" + paras +
                '}';
    }
}
