//package com.hzyw.iot.platform.util.json;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
///**
// * 消息体
// */
//
//public class MessageVO {
//    private String Id;        //设备编码
//    private String status;          //状态
//    private List<Map> attributers;
//    private List<MethodVO> methods;
//    private List<Map> definedAttributers;
//    private List<MethodVO> definedMethods;
//    private Map<String,Integer> signals;
//
//    public String getId() {
//        return Id;
//    }
//
//    public void setId(String id) {
//        Id = id;
//    }
//
//    public List<MethodVO> getMethods() {
//        return methods;
//    }
//
//    public void setMethods(List<MethodVO> methods) {
//        this.methods = methods;
//    }
//
//    public List<Map> getAttributers() {
//        return attributers;
//    }
//
//    public void setAttributers(List<Map> attributers) {
//        this.attributers = attributers;
//    }
//
//    public List<Map> getDefinedAttributers() {
//        return definedAttributers;
//    }
//
//    public void setDefinedAttributers(List<Map> definedAttributers) {
//        this.definedAttributers = definedAttributers;
//    }
//
//    public List<MethodVO> getDefinedMethods() {
//        return definedMethods;
//    }
//
//    public void setDefinedMethods(List<MethodVO> definedMethods) {
//        this.definedMethods = definedMethods;
//    }
//
//    public String getStatus() {
//        return status;
//    }
//
//    public void setStatus(String status) {
//        this.status = status;
//    }
//
//    public Map<String, Integer> getSignals() {
//        return signals;
//    }
//
//    public void setSignals(Map<String, Integer> signals) {
//        this.signals = signals;
//    }
//
//    @Override
//    public String toString() {
//        return "MessageVO{" +
//                "Id='" + Id + '\'' +
//                ", status='" + status + '\'' +
//                ", attributer=" + attributers +
//                ", methods=" + methods +
//                ", definedAttributer=" + definedAttributers +
//                ", definedMethod=" + definedMethods +
//                ", signals='" + signals + '\'' +
//                '}';
//    }
//}
