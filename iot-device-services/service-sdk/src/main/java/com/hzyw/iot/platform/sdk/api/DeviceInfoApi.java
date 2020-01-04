package com.hzyw.iot.platform.sdk.api;

import com.hzyw.iot.platform.models.equip.DeviceType;
import com.hzyw.iot.platform.models.equip.Equipment;
import com.hzyw.iot.platform.sdk.rest.DeviceMgrFeignClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * @author by early
 * @blame IOT Team
 * @date 2019/8/16.
 */


public class DeviceInfoApi {

    @Autowired
    DeviceMgrFeignClient deviceMgrFeignClient;

//    Equipment getDeviceInfo(String deviceId, Map audit) {
//        return deviceMgrFeignClient.getDeviceInfo(deviceId, audit);
//    }
//
//    Equipment getDeviceAllDetails(String deviceId, Map audit) {
//        return deviceMgrFeignClient.getDeviceAllDetails(deviceId, audit);
//
//    }
//
//    DeviceType getDeviceTypeInfo(String deviceType, Map audit) {
//        return deviceMgrFeignClient.getDeviceType(deviceType, audit);
//    }

}
