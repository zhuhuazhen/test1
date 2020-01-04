package com.hzyw.iot.platform.models.transfer;

/**
 * @author by early
 * @blame IOT Team
 * @date 2019/8/15.
 */
public interface IotError {

    String getErrorCode();

    String getErrorMsg();

    Throwable getRootCause();

}
