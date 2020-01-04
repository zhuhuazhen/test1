package com.hzyw.iot.platform.models.transfer;

/**
 * @author by early
 * @blame IOT Team
 * @date 2019/9/4.
 */
public class DefaultDeviceException extends Throwable implements IotError {

    private static final String COLON = ":";
    private String errCode;
    private String errMsg;

    public DefaultDeviceException(String errorCode, String errorMsg, Throwable cause) {
        super(errorCode + COLON + errorMsg, cause);
        errCode = errorCode;
        errMsg = errorMsg;
    }

    public DefaultDeviceException(String errorCode, String errorMsg) {
        super(errorCode + COLON + errorMsg);
        errCode = errorCode;
        errMsg = errorMsg;
    }

    @Override
    public String getErrorCode() {
        return errCode;
    }

    @Override
    public String getErrorMsg() {
        return errMsg;
    }

    @Override
    public Throwable getRootCause() {
        return super.getCause();
    }
}
