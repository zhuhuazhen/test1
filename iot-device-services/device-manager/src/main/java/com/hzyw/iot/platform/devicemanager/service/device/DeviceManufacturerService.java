package com.hzyw.iot.platform.devicemanager.service.device;

import com.hzyw.iot.platform.devicemanager.domain.device.ManufacturerDO;
import com.hzyw.iot.platform.models.equip.Manufacturer;

/**
 * DeviceManufacturerService
 *
 * @blame Android Team
 */
public interface DeviceManufacturerService {

    ManufacturerDO getManufacturer(Integer code);

    void deleteManufacturer(Integer code);

    void saveManufacturer(ManufacturerDO manufacturer);


}
