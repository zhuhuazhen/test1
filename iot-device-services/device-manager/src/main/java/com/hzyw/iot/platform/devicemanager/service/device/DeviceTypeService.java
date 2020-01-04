package com.hzyw.iot.platform.devicemanager.service.device;

import com.hzyw.iot.platform.devicemanager.domain.device.DeviceAttributeDO;
import com.hzyw.iot.platform.devicemanager.domain.device.DeviceTypeDO;

import java.util.List;

/**
 * @author by early
 * @blame IOT Team
 * @date 2019/8/29.
 */
public interface DeviceTypeService {

    /**
     * 新增设备类型
     *
     * @param type
     * @throws Exception
     */
    void insertDeviceType(DeviceTypeDO type);


    /**
     * 更新设备类型
     *
     * @param type
     * @throws Exception
     */
    void updateDeviceType(DeviceTypeDO type);

    /**
     * 删除设备类型
     * @param typeCode
     * @param deviceDomain
     * @param manufacturer
     */
    void deleteDeviceType(String typeCode,Integer deviceDomain,Integer manufacturer);

    /**
     * 通过业务主键查询设备类型。
     *
     * @param typeCode
     * @param deviceDomain
     * @param manufacturer
     * @return
     */
    DeviceTypeDO selectDeviceType(String typeCode, Integer deviceDomain, Integer manufacturer);


    /**
     * 通过设备类型ID查业务属性
     *
     */
    List<DeviceAttributeDO> selectDeviceAttrByDeviceType(DeviceTypeDO deviceTypeDO);

}
