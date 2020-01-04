package com.hzyw.iot.platform.devicemanager.service.device.impl;

import com.hzyw.iot.platform.devicemanager.domain.device.ManufacturerDO;
import com.hzyw.iot.platform.devicemanager.mapper.device.ManufacturerDao;
import com.hzyw.iot.platform.devicemanager.service.device.DeviceManufacturerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * @author by early
 * @blame IOT Team
 * @date 2019/8/30.
 */

@Slf4j
@Service
public class DeviceManufacturerServiceImpl implements DeviceManufacturerService {

    @Resource
    ManufacturerDao manufacturerDao;

    @Override
    public ManufacturerDO getManufacturer(Integer code) {
        return manufacturerDao.getManufacturerInfo(code);
    }

    @Override
    public void deleteManufacturer(Integer code) {
        manufacturerDao.deleteManufacturer(code);
    }

    @Override
    public void saveManufacturer(ManufacturerDO manufacturer) {
        if (!StringUtils.isEmpty(manufacturer.getManufacturerCode())) {
            if (manufacturerDao.getManufacturerInfo(manufacturer.getManufacturerCode()) == null) {
                manufacturerDao.insertManufacturer(manufacturer);
            } else {
                manufacturerDao.updateManufacturer(manufacturer);
            }

        }

    }

}
