package com.hzyw.iot.platform.models.transfer;

import org.springframework.lang.NonNull;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author by early
 * @blame IOT Team
 * @date 2019/8/15.
 */
public class DeviceOptRequest implements ITransferRequest, Serializable {

    /**
     * 审计预留 UserId, RoleId, TenantId, ApplicationId;
     */
    private Map<String, String> audit = new HashMap<>();

    /**
     * 客户消息Id，用于比异步对请求与返回。
     */
    private String customMsgId;

    /**
     * 设备ID列表，方法的执行对象
     */
    @NonNull
    private List<String> deviceIds;
    /**
     * 要执行的方法名,以及上述方法的参数列表，key为属性定义名，value为属性值，注意对应的对象类型
     */
    @NonNull
    private Map<String,Map<String,Object>> method;
    private String callbackUrl;

    private String callbackName;


    public Map<String, String> getAudit() {
        return audit;
    }

    @Override
    public void addParameter(String key, Object value) {
        //TODO:
    }
    public void setAudit (Map map){
        this.audit = map;
    }

    @Override
    public void addAudit(String key, Object value) {
        //TODO:
    }

    public void setParameter(Map map) {
//        map.keySet().stream().forEach(e -> {
//            if ("method".equals(e)) {
//                method = (String) map.get(e);
//            } else if ("deviceIds".equals(e)) {
//                deviceIds = (List<String>) map.get(e);
//            } else {
//                this.attrValues.put(e.toString(), map.get(e));
//            }
//        });
    }

    public void addAudit(String key, String value) {
        audit.put(key, value);
    }

    @NonNull
    public List<String> getDeviceIds() {
        return deviceIds;
    }

    public void setDeviceIds(@NonNull List<String> deviceIds) {
        this.deviceIds = deviceIds;
    }

    public void addDeviceId(@NonNull String deviceId) {
        deviceIds.add(deviceId);
    }

    @NonNull
    public Map<String, Map<String, Object>> getMethod() {
        return method;
    }

    public void setMethod(@NonNull Map<String, Map<String, Object>> method) {
        this.method = method;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public String getCallbackName() {
        return callbackName;
    }

    public void setCallbackName(String callbackName) {
        this.callbackName = callbackName;
    }

    public String getCustomMsgId() {
        return customMsgId;
    }

    public void setCustomMsgId(String customMsgId) {
        this.customMsgId = customMsgId;
    }
}
