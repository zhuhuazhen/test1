package com.hzyw.iot.platform.models.equip;

import java.io.Serializable;
import java.util.Arrays;

/**
 * DeviceMethod
 *
 * @blame IOT Team
 */
public class DeviceMethod implements Serializable {
    private String methodName;      //方法名
    private String[] input;      //输入对象Key
    private String[] output;     //输出对象key
//    private Map methodMeta;        //元数据

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String[] getOutput() {
        return output;
    }

    public void setOutput(String[] output) {
        this.output = output;
    }

    public String[] getInput() {
        return input;
    }

    public void setInput(String[] input) {
        this.input = input;
    }

    public boolean containsAllInput(Object[] arg) {
        long l = Arrays.stream(input).filter(s -> !Arrays.asList(arg).contains(s)).count();
        if (l == 0) {
            return true;
        }
        return false;
    }
}
