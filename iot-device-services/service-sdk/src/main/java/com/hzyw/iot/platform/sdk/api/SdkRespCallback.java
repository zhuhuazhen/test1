package com.hzyw.iot.platform.sdk.api;

import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.SuccessCallback;

/**
 * @author by early
 * @blame IOT Team
 * @date 2019/8/21.
 */

public interface SdkRespCallback<T>  extends SuccessCallback<T>, FailureCallback {

}
