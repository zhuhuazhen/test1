package com.hzyw.iot.platform.sdk.rest;

import com.hzyw.iot.platform.models.equip.DeviceAttribute;
import com.hzyw.iot.platform.models.equip.DeviceType;
import com.hzyw.iot.platform.models.equip.Equipment;
import com.hzyw.iot.platform.models.transfer.DeviceOptRequest;
import com.hzyw.iot.platform.models.transfer.DeviceOptResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @author by early
 * @blame IOT Team
 * @date 2019/8/21.
 */

@FeignClient(name = "${sdk.server.name}",url = "${sdk.server.url}")//iotdevicemanager
public interface DeviceMgrFeignClient {

    @GetMapping("/sdk/hello")
    public String hello(@RequestParam("hello") String hello);

    @PostMapping("/sdk/device/opt")
    public DeviceOptResponse deviceOpt(DeviceOptRequest request);

//    @PostMapping("/device/status/getLastAttributes")
//    public Map<DeviceAttribute,Object> getLastAttributes(String deviceId,Map audit);

//    @PostMapping("/device/status/getLastAttribute")
//    public String getLastAttributeByKey(@RequestParam("deviceId") String deviceId,@RequestParam("attribute")DeviceAttribute attribute,Map audit);

//    @PostMapping("/sdk/device/getDeviceInfo")
//    public Equipment getDeviceInfo(@RequestParam("deviceId") String deviceId, Map audit);

//    @PostMapping("/sdk/device/getDeviceInfo")
//    public Equipment getDeviceAllDetails(@RequestParam("deviceId")String deviceId, Map audit);

//    @PostMapping("/sdk/device/getRelation")
//    public String deviceOpt(DeviceOptRequest request);

//    @PostMapping("/sdk/device/getTypeInfo")
//    public DeviceType getDeviceType(@RequestParam("typeId")String typeId,Map audit);

}
