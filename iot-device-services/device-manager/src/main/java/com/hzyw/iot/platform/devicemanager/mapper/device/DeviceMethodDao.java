package com.hzyw.iot.platform.devicemanager.mapper.device;

import com.hzyw.iot.platform.devicemanager.domain.device.DeviceMethodDO;
import com.hzyw.iot.platform.models.equip.DeviceMethod;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author by early
 * @blame IOT Team
 * @date 2019/8/30.
 */
@Mapper
public interface DeviceMethodDao {

    void insertMethod(DeviceMethodDO de);

    @Delete("delete from DEVICE_METHOD_T where METHOD_ID=#{methodId}")
    void deleteMethod(Integer methodId);

    DeviceMethodDO selectMethodById(@Param("methodId") Integer methodId);


    void updateMethod(DeviceMethodDO methodDO);

    /**
     * 根据设备类型查方法列表
     * @param typeId 设备型号，设备大类，生厂商，中间用短横线“-”连接
     * @return
     */
    List<DeviceMethodDO> getMethodsByDeviceType(@Param("typeId")String typeId);

}
