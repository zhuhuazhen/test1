package com.hzyw.iot.platform.util.json;

import java.util.List;
import java.util.Map;

public class MethodVO {
    private String type;
    private String method;      //方法名
    private List<Map> in;      //输入数据
    private List<Map> out;     //输出数据
    private Map<Object,Object> metaData;        //元数据

    public List<Map> getIn() {
        return in;
    }

    public void setIn(List<Map> in) {
        this.in = in;
    }

    public List<Map> getOut() {
        return out;
    }

    public void setOut(List<Map> out) {
        this.out = out;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Map<Object, Object> getMetaData() {
        return metaData;
    }

    public void setMetaData(Map<Object, Object> metaData) {
        this.metaData = metaData;
    }
}
