package com.hzyw.iot.platform.models.rule;

/**
 * @author by early
 * @blame IOT Team
 * @date 2019/9/29.
 */
public enum Expression {
    /**
     * 等于
     */
    EQUAL("=="),
    /**
     * 不等于
     */
    NOT_EQUAL("!="),
    /**
     * 大于
     */
    BIGGER(">"),
    /**
     * 小于
     */
    LESS("<"),
    /**
     * 大于等于
     */
    NOT_LESS(">="),
    /**
     * 小于等于
     */
    NOT_BIGGER("<="),
    /**
     * 包含
     */
    CONTAINS("contains"),
    /**
     * 不包含
     */
    NOT_CONTAINS("not contains"),
    /**
     * 属于
     */
    MEMBER_OF("memberOf"),
    /**
     * 不属于
     */
    NOT_MEMBER_OF("not memberOf"),
    /**
     * 匹配
     */
    MATCHES("matches"),
    /**
     * 不匹配
     */
    NOT_MATCHES("not matches");

    private String symbol;

    Expression(String str) {
        this.symbol = str;
    }

    public static String getSymbol(Expression expression) {
        return expression.getSymbol();
    }

    public String getSymbol() {
        return symbol;
    }
}
