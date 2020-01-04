package com.hzyw.iot.platform.devicemanager.service.device.impl;

import com.hzyw.iot.platform.devicemanager.domain.device.DeviceAttributeDO;
import com.hzyw.iot.platform.devicemanager.domain.device.DeviceTypeDO;
import com.hzyw.iot.platform.devicemanager.mapper.device.DeviceTypeDao;
import com.hzyw.iot.platform.devicemanager.service.device.DeviceTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * DeviceTypeServiceImpl
 *
 * @blame Android Team
 */
@Slf4j
@Service
public class DeviceTypeServiceImpl implements DeviceTypeService {

    @Resource
    DeviceTypeDao deviceTypeDao;

    @Override
    public void insertDeviceType(DeviceTypeDO type) {
        deviceTypeDao.insertDeviceType(type);
    }

    @Override
    public void updateDeviceType(DeviceTypeDO type) {
        deviceTypeDao.updateDeviceTypeDetail(type);
    }

    @Override
    public void deleteDeviceType(String typeCode, Integer deviceDomain, Integer manufacturer) {
        deviceTypeDao.deleteDeviceType(typeCode,deviceDomain,manufacturer);
    }

    @Override
    public DeviceTypeDO selectDeviceType(String typeCode, Integer deviceDomain, Integer manufacturer) {
        return deviceTypeDao.getDeviceType(typeCode,deviceDomain,manufacturer);
    }

    @Override
    public List<DeviceAttributeDO> selectDeviceAttrByDeviceType(DeviceTypeDO deviceTypeDO) {
        return deviceTypeDao.selectDeviceAttrByDeviceType(deviceTypeDO);
    }


}
