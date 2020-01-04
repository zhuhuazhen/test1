package com.hzyw.iot.platform.sdk.api;

import com.hzyw.iot.platform.models.equip.DeviceAttribute;
import com.hzyw.iot.platform.sdk.rest.DeviceMgrFeignClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
 * @author by early
 * @blame IOT Team
 * @date 2019/8/16.
 */


public class DeviceStatusApi {

    @Autowired
    DeviceMgrFeignClient deviceMgrFeignClient;

//    public Map<DeviceAttribute, Object> getLastAttributes(String deviceId, Map audit) {
//        Map<DeviceAttribute, Object> deviceAttributeObjectMap = deviceMgrFeignClient.getLastAttributes(deviceId, audit);
//        return deviceAttributeObjectMap;
//    }
//
//    public Map<String, String> getLastAttributesValue(String deviceId, Map audit) {
//        Map<DeviceAttribute, Object> deviceAttributeObjectMap = deviceMgrFeignClient.getLastAttributes(deviceId, audit);
//        Map<String, String> attributes = null;
//        if (deviceAttributeObjectMap != null) {
//            attributes = new HashMap<>();
//            for (DeviceAttribute attribute : deviceAttributeObjectMap.keySet()) {
//                attributes.put(attribute.getAttributeKey(), deviceAttributeObjectMap.get(attribute).toString());
//            }
//        }
//        return attributes;
//    }
//
//
//    public Object getLastStatusByAttr(String deviceId, DeviceAttribute attribute, Map audit) {
//        String deviceAttributeObjectMap = deviceMgrFeignClient.getLastAttributeByKey(deviceId, attribute, audit);
//        Object value = new Object();
//        if (deviceAttributeObjectMap != null) {
//            for (DeviceAttribute attribute : deviceAttributeObjectMap.keySet()) {
//                if (key.equals(attribute.getAttributeKey())) {
//                    value = deviceAttributeObjectMap.get(attribute);
//                }
//            }
//        }
//        return value;
//    }

}
