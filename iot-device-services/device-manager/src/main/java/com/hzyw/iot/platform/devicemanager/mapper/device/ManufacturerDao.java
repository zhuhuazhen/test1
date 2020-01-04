package com.hzyw.iot.platform.devicemanager.mapper.device;

import com.hzyw.iot.platform.devicemanager.domain.device.ManufacturerDO;
import com.hzyw.iot.platform.models.equip.Manufacturer;
import org.apache.ibatis.annotations.*;

/**
 * ManufacturerDao
 *
 * @blame Android Team
 */
@Mapper
public interface ManufacturerDao {

    /**
     * 查询生厂商信息
     * @param manufacturerCode
     * @return
     */
     ManufacturerDO getManufacturerInfo(@Param("manufacturerCode") Integer manufacturerCode);

    /**
     * 新增生产商信息
     * @param manufacturer
     */
    void insertManufacturer(ManufacturerDO manufacturer);

    /**
     * 修改生产商信息
     */
    void updateManufacturer(ManufacturerDO manufacturer);

    /**
     * 删除生厂商信息
     * @param manufacturerCode
     */
    @Delete("delete from DEVICE_MANUFACTURER_T where MANUFACTURER_CODE=#{manufacturerCode}")
    void deleteManufacturer(@Param("manufacturerCode") Integer manufacturerCode);
}
