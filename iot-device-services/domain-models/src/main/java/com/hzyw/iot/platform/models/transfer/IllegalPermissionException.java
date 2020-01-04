package com.hzyw.iot.platform.models.transfer;

/**
 * @author by early
 * @blame IOT Team
 * @date 2019/9/7.
 */
public class IllegalPermissionException extends DefaultDeviceException {

    private static final String ERROR_CODE = "Illegal_Permission";

    public IllegalPermissionException(String errorMsg, Throwable cause) {
        super(ERROR_CODE, errorMsg, cause);
    }

    public IllegalPermissionException(String errorMsg) {
        super(ERROR_CODE, errorMsg);
    }

}
