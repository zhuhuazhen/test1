package com.hzyw.iot.platform.models.transfer;

import org.springframework.lang.Nullable;

import java.io.Serializable;

/**
 * @author by early
 * @blame IOT Team
 * @date 2019/8/15.
 */
public class DeviceOptResponse implements ITransferResponse, Serializable {
    /**
     * 操作是否完成，用于异步通信
     */
    private boolean isDown = false;
    /**
     * 操作是否成功
     */
    private boolean success = false;
    @Nullable
    private IotError error;
    @Nullable
    private Object respObj;

    @Override
    public boolean success() {
        return success;
    }

    public boolean isDown() {
        return isDown;
    }

    public DeviceOptResponse setDown(boolean down) {
        this.isDown = down;
        return this;
    }

    public String getErrorCode() {
        if (this.error == null) {
            return null;
        }
        return error.getErrorCode();
    }

    public String getErrorMsg() {
        if (error == null) {
            return null;
        }
        return error.getErrorMsg();
    }

    @Override
    public IotError getError() {
        return error;
    }

    public DeviceOptResponse setError(@Nullable IotError error) {
        this.error = error;
        return this;
    }

    @Nullable
    @Override
    public Object getResponse() {
        return respObj;
    }

    public boolean isSuccess() {
        return success;
    }

    public DeviceOptResponse setSuccess(boolean isSuccess) {
        this.success = isSuccess;
        return this;
    }

    public DeviceOptResponse setRespObj(@Nullable Object respObj) {
        this.respObj = respObj;
        return this;
    }
}
