package com.hzyw.iot.platform.devicemanager.mapper.device;

import com.hzyw.iot.platform.devicemanager.domain.device.DeviceAttributeDO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author by early
 * @blame IOT Team
 * @date 2019/8/30.
 */
public interface DeviceAttributeDao {

    /**
     * 查询单条属性
     */
    DeviceAttributeDO getDeviceAttribute(@Param("attrKey") String key);

    /**
     * 删除单条属性
     */
    @Delete("delete from DEVICE_ATTRIBUTE_T where attr_key=#{attrKey}")
    void deleteDeviceAttribute(@Param("attrKey") String attrKey);
    /**
     * 插入单条属性
     */
    void insertDeviceAttribute(DeviceAttributeDO vo);

    /**
     * 更新单条属性
     */
    void updateDeviceAttribute(DeviceAttributeDO vo);

    /**
     * 根据设备类型查询属性
     */
    List<DeviceAttributeDO> searchDeviceAttrByType(@Param("typeId") String typeId);

    /**
     * 维护设备属性与设备类型的关联关系。
     * @param typeId
     * @param attrKey
     */
//    @Insert("replace into DEVICETYPE_ATTR_REL_T values ('33','55') ")
    void saveAttrTypeRelation(@Param("typeId")String typeId,@Param("attrKey")String attrKey);

    List<DeviceAttributeDO> listAllDevicesAttr(Integer curPage, Integer pageSize);
}
