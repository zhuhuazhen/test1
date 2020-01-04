package com.hzyw.iot.platform.devicemanager.controller;

import com.hzyw.iot.platform.devicemanager.domain.vo.DeviceListVO;
import com.hzyw.iot.platform.devicemanager.service.device.DeviceInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * DeviceInfoController 设备列表
 *
 * @blame Android Team
 */
@Slf4j
@RestController
@RequestMapping("/device/info")
public class DeviceInfoController {

    @Autowired
    private DeviceInfoService deviceInfoService;

    /**
     * 获取设备信息
     *
     * @param
     * @return
     */
    @GetMapping("/list")
    public List<DeviceListVO> getAllDeviceInfo(@RequestParam("deviceId") String deviceId,
                                               @RequestParam("deviceType") String deviceType,
                                               @RequestParam("gatewayId") String gatewayId) {
        List<DeviceListVO> deviceListVO=deviceInfoService.getAllDeviceInfo(deviceId,deviceType,gatewayId);
        return deviceListVO;
    }


}
