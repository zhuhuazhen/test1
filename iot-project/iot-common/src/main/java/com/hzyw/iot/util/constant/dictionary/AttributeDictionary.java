package com.hzyw.iot.util.constant.dictionary;

/**
 * PCL 属性字典表
 */
public class AttributeDictionary {
    public static final String 灯节点 ="id";  //
    public static final String AB灯 ="ab"; // 目前对接 缺这个属性
    public static final String 调光值 ="level"; //

    //上游 "method" 入参属性 对应指令码
    public static final String set_onoff ="42H"; //节点调光(42H)

    /**
     *节点调光(42H)
     */
    static String[] 节点调光={AttributeDictionary.灯节点,AttributeDictionary.AB灯,AttributeDictionary.调光值};




}


