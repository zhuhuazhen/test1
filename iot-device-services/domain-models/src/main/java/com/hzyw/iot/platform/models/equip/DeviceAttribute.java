package com.hzyw.iot.platform.models.equip;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

/**
 * 设备属性定义
 *
 * @blame IOT Team
 */
public class DeviceAttribute<T> implements Serializable {
    public static final String NUMBER = "number";
    public static final String CHAR = "char";
    public static final String INT = "int";
    public static final String STRING = "string";
    public static final String ARRAY = "array";
    public static final String BOOLEAN = "boolean";

    /**
     * 属性的标准的科学代号或标识
     */
    private final String attributeKey;
    /**
     * 属性的标准业务名称或解释
     */
    private String attributeName;
    /**
     * 属性值的数据类型
     */
    private Class<T> valueType;
    /**
     * 属性值的标准计量单位
     */
    private String unit;
    /**
     * 扩展元数据
     */
    private Map attributeMeta;

    /**
     * new DeviceAttribute,当不指定值的类型时，统一视为String类型处理。
     *
     * @param attributeKey
     */
    public DeviceAttribute(String attributeKey) {
        this.attributeKey = attributeKey;
        this.valueType = (Class<T>) String.class;
    }

    /**
     * new DeviceAttribute
     *
     * @param attributeKey
     * @param classType
     */
    public DeviceAttribute(String attributeKey, String classType) {
        this.attributeKey = attributeKey;
        if (NUMBER.equals(classType)) {
            this.valueType = (Class<T>) BigDecimal.class;
        } else if (BOOLEAN.equals(classType)) {
            this.valueType = (Class<T>) Boolean.class;
        } else if (STRING.equals(classType)) {
            this.valueType = (Class<T>) String.class;
        } else if (CHAR.equals(classType)) {
            this.valueType = (Class<T>) Integer.class;
        } else if (INT.equals(classType)) {
            this.valueType = (Class<T>) Integer.class;
        } else if (ARRAY.equals(classType)) {
            this.valueType = (Class<T>) Arrays.class;
        } else {
            this.valueType = (Class<T>) String.class;
        }
    }

    /**
     * new DeviceAttribute
     *
     * @param attributeKey
     * @param valueType
     * @param attributeName
     * @param unit
     */
    public DeviceAttribute(String attributeKey, Class<T> valueType, String attributeName, String unit) {
        this.attributeKey = attributeKey;
        this.attributeName = attributeName;
        this.valueType = valueType;
        this.unit = unit;
    }

    public String getAttributeKey() {
        return attributeKey;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public Class<T> getValueType() {
        return valueType;
    }

    public void setValueType(Class<T> valueType) {
        this.valueType = valueType;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Map getAttributeMeta() {
        return attributeMeta;
    }

    public void setAttributeMeta(Map attributeMeta) {
        this.attributeMeta = attributeMeta;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DeviceAttribute that = (DeviceAttribute) o;
        return Objects.equals(attributeKey, that.attributeKey) && Objects.equals(valueType, that.valueType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(attributeKey);
    }

}


