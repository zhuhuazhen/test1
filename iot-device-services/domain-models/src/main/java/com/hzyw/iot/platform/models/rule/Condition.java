package com.hzyw.iot.platform.models.rule;

/**
 * @author by early
 * @blame IOT Team
 * @date 2019/9/29.
 */
public class Condition {
    private String key;
    private Object value;
    private String constraint;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getConstraint() {
        return constraint;
    }

    public void setConstraint(String constraint) {
        this.constraint = constraint;
    }
}
