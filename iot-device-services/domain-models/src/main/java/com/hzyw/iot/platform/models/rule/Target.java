package com.hzyw.iot.platform.models.rule;

import java.util.Map;

/**
 * @author by early
 * @blame IOT Team
 * @date 2019/9/29.
 */
public class Target {
    private String targetId;
    private String methodName;
    private Map<String, Object> in;
    private Map<String, Object> out;

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Map<String, Object> getIn() {
        return in;
    }

    public void setIn(Map<String, Object> in) {
        this.in = in;
    }

    public Map<String, Object> getOut() {
        return out;
    }

    public void setOut(Map<String, Object> out) {
        this.out = out;
    }
}


