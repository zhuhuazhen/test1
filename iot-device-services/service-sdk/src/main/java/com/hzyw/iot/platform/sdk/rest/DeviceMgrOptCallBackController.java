package com.hzyw.iot.platform.sdk.rest;

import com.hzyw.iot.platform.models.transfer.DeviceOptResponse;
import com.hzyw.iot.platform.sdk.api.SdkRespCallback;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author by early
 * @blame IOT Team
 * @date 2019/8/21.
 */
@Slf4j
@RestController
@RequestMapping("/sdk")
public class DeviceMgrOptCallBackController {

    @Autowired
    SdkRespCallback sdkRespCallback;

    @PostMapping("/callback")
    public void continueDown(@RequestBody DeviceOptResponse response) {
        if (response.isDown() && response.success()) {
            sdkRespCallback.onSuccess(response.getResponse());
        } else if (!response.success()) {
            if (response.getError() != null) {
                sdkRespCallback.onFailure(response.getError().getRootCause());
            } else {
                sdkRespCallback.onFailure(new RuntimeException("UNDEFINED"));
            }
        } else if (!response.isDown()) {
            sdkRespCallback.onFailure(new RuntimeException("UNKNOWN"));
        }
    }
}
