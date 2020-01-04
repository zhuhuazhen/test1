package com.hzyw.iot.platform.devicemanager.controller;

import com.hzyw.iot.platform.devicemanager.service.opt.DeviceOptService;
import com.hzyw.iot.platform.models.transfer.DeviceOptRequest;
import com.hzyw.iot.platform.models.transfer.DeviceOptResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author by early
 * @blame IOT Team
 * @date 2019/8/16.
 */
@Slf4j
@RestController
@RequestMapping("/sdk")
public class DeviceOptController {

    @Autowired
    DeviceOptService optService;

    @PostMapping("/sync")
    public DeviceOptResponse syncOperation(@RequestBody DeviceOptRequest request){
        return optService.doSyncOpt(request);
    }

    @PostMapping("/device/opt")
    public DeviceOptResponse asyncOperation(@RequestBody DeviceOptRequest request){
        return optService.doAsyncOpt(request);
    }
}
