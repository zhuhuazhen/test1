package com.hzyw.iot.platform.devicemanager.listener;


import com.hzyw.iot.platform.devicemanager.caches.AttributeCacheService;
import com.hzyw.iot.platform.devicemanager.caches.DeviceTypeCacheService;
import com.hzyw.iot.platform.devicemanager.caches.EquipmentCacheService;
import com.hzyw.iot.platform.devicemanager.caches.ICacheRead;
import com.hzyw.iot.platform.devicemanager.service.device.DeviceAttrService;
import com.hzyw.iot.platform.models.equip.DeviceAttribute;
import com.hzyw.iot.platform.models.equip.DeviceType;
import com.hzyw.iot.platform.models.equip.Equipment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * DeviceTypeInfoCache
 *
 * @blame IOT Team
 */
@Component
public class CacheInitialRunner implements CommandLineRunner {

    @Autowired
    private AttributeCacheService attributeCacheService;
    @Autowired
    private DeviceTypeCacheService deviceTypeCacheService;
    @Autowired
    private EquipmentCacheService equipmentCacheService;
    @Autowired
    private DeviceAttrService attrService;
//    @Autowired
//    private RedisTemplate redisTemplate;

    @Override
    public void run(String... args) throws Exception {

//        attrService.se
//        attributeCacheService.
//        attributeCacheService.get()
//        DeviceType de = deviceTypeCache.getDeviceTypeByKey("C4-8205-4");
//        DeviceAttribute deviceAttribute = deviceTypeCache.getDeviceAttributeByKey("sec");
//        System.out.println("DeviceType.info:" + de.getTypeCode());
//        System.out.println("DeviceAttribute.info:" + deviceAttribute.getAttributeKey());
       // Object result=redisTemplate.opsForValue().get(deviceAttribute.getAttributeKey());
      //  String attributeJson= JSON.toJSONString(result);
      //  DeviceAttribute attribute = JSONObject.parseObject(attributeJson, DeviceAttribute.class);
        // String result2= (String) redisTemplate.opsForHash().get(deviceAttribute.getAttributeKey(),deviceAttribute.getAttributeKey()).toString();
       // DeviceAttribute attribute=deviceAttributeCache.putAttributeCache("current",deviceAttribute);
      //  System.out.println("result:"+result);
      //  System.out.println("attribute::"+attribute.getAttributeName());
    }
}
