package com.hzyw.iot.platform.devicemanager.controller;

import com.hzyw.iot.platform.devicemanager.caches.StatusHistoryCacheService;
import com.hzyw.iot.platform.models.equip.DeviceAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author by early
 * @blame IOT Team
 * @date 2019/8/16.
 */

@RestController
@RequestMapping("/device/status")
public class StatusHistroyController {

    @Autowired
    private StatusHistoryCacheService statusHistoryCache;

    /**
     * 查询所有设备属性值
     * @param deviceId
     * @param audit
     * @return
     */
    @PostMapping("/getLastAttributes")
    public Map<DeviceAttribute, Object> getLastAttributes(String deviceId, Map audit) {
        Map<DeviceAttribute, Object> deviceAttributeObjectMap = statusHistoryCache.get(deviceId).getLastAttributes();
        return deviceAttributeObjectMap;
    }

}
