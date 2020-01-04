package com.hzyw.iot.platform.devicemanager.mapper.device;


import com.hzyw.iot.platform.devicemanager.domain.device.DeviceAttributeDO;
import com.hzyw.iot.platform.devicemanager.domain.device.DeviceTypeDO;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * DeviceTypeDao
 *
 * @blame Android Team
 */
@Mapper
public interface DeviceTypeDao {

    /**
     * 按主键查询设备型号信息
     * @param typeCode
     * @param deviceDomain
     * @param manufacturerCode
     * @return
     */
     DeviceTypeDO getDeviceType(@Param("typeCode") String typeCode, @Param("deviceDomain") Integer deviceDomain,@Param("manufacturerCode") Integer manufacturerCode);

    /**
     * 按条件查询设备型号信息
     *
     * @param typeCode
     * @param deviceDomain
     * @param manufacturerCode
     * @return
     */
     List<DeviceTypeDO> selectDeviceType(@Param("typeCode") String typeCode, @Param("deviceDomain")  Integer deviceDomain, @Param("manufacturerCode")  Integer manufacturerCode);

    /**
     * 新增设备类型
     *
     * @param type
     */
    void insertDeviceType(DeviceTypeDO type);


    /**
     * 更新设备类型的详细信息，严格按主键（设备型号Code，设备大类，生厂商Code）查找更新，
     *
     * @param type
     */
    void updateDeviceTypeDetail(DeviceTypeDO type);

    /**
     * 保存设备类型，联合主键（设备型号Code，设备大类，生厂商Code）一致则更改，否则新增。
     * @param type
     */
    void saveDeviceType(DeviceTypeDO type);

    /**
     * 删除设备类型
     *
     * @param typeCode
     * @param deviceDomain
     * @param manufacturerCode
     */
    @Delete("delete from DEVICE_TYPE_T where TYPE_CODE=#{typeCode} " +
            "and DEVICE_DOMAIN=#{deviceDomain} and MANUFACTURER_CODE=#{manufacturerCode}")
    void deleteDeviceType(String typeCode, Integer deviceDomain, Integer manufacturerCode);

    List<DeviceAttributeDO> selectDeviceAttrByDeviceType(DeviceTypeDO deviceTypeDO);
}
