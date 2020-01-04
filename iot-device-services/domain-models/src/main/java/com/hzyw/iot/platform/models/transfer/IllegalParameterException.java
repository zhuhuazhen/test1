package com.hzyw.iot.platform.models.transfer;

/**
 * @author by early
 * @blame IOT Team
 * @date 2019/9/4.
 */
public class IllegalParameterException extends DefaultDeviceException {

    private static final String ERROR_CODE="Illegal_Parameter";

    public IllegalParameterException(String errorCode, String errorMsg, Throwable cause) {
        super(errorCode, errorMsg, cause);
    }

    public IllegalParameterException(String errorMsg) {
        super(ERROR_CODE, errorMsg);
    }
}
