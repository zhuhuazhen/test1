package com.hzyw.iot.platform.models.transfer;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.Map;

/**
 * @author by early
 * @blame IOT Team
 * @date 2019/8/15.
 */
public class DeviceServiceContext {
    public static final String DEVICE_OPERATION_CONTEXT = DeviceServiceContext.class.getName() + ".CONTEXT";
    public static final String CALLBACK_NAME_KEY= "CALLBACK_NAME";
    public static final String CALLBACK_URL_KEY= "CALLBACK_URL";

    private String transactionId;
    @NonNull
    private DeviceOptRequest deviceRequest;
    @Nullable
    private DeviceOptResponse deviceResponse;
    @Nullable
    Map<String,Object> adtData;
    @Nullable
    private Map<String, IotError> errorsMap;

    public DeviceServiceContext() {
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    @NonNull
    public DeviceOptRequest getDeviceRequest() {
        return deviceRequest;
    }

    public void setDeviceRequest(@NonNull DeviceOptRequest deviceRequest) {
        this.deviceRequest = deviceRequest;
    }

    @Nullable
    public DeviceOptResponse getDeviceResponse() {
        return deviceResponse;
    }

    public void setDeviceResponse(@Nullable DeviceOptResponse deviceResponse) {
        this.deviceResponse = deviceResponse;
    }

    @Nullable
    public Map<String, Object> getAdtData() {
        return adtData;
    }

    public Object getAdtDataByKey(String key){
        return adtData.get(key);
    }

    public Object putAdtDataByKey(String key,Object object){
        return adtData.put(key,object);
    }

    public void setAdtData(@Nullable Map<String, Object> adtData) {
        this.adtData = adtData;
    }

    @Nullable
    public Map<String, IotError> getErrorsMap() {
        return errorsMap;
    }

    public void setErrorsMap(@Nullable Map<String, IotError> errorsMap) {
        this.errorsMap = errorsMap;
    }
}
