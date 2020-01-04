package com.hzyw.iot.platform.sdk.api;

import com.hzyw.iot.platform.sdk.rest.DeviceMgrFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author by early
 * @blame IOT Team
 * @date 2019/8/16.
 */
@Slf4j
public class TestHelloApi {

    @Autowired(required = false)
    DeviceMgrFeignClient deviceMgrFeignClient;

    public String testHello(String string){
         return deviceMgrFeignClient.hello(string);
    }
}
