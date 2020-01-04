package com.hzyw.iot.platform.models.transfer;

/**
 * @author by early
 * @blame IOT Team
 * @date 2019/9/7.
 */
public class MessageDataProcessException extends DefaultDeviceException {

    private static final String ERROR_CODE = "MsgProcess_Error";

    public MessageDataProcessException(String errorMsg, Throwable cause) {
        super(ERROR_CODE, errorMsg, cause);
    }

    public MessageDataProcessException(String errorMsg) {
        super(ERROR_CODE, errorMsg);
    }

}
