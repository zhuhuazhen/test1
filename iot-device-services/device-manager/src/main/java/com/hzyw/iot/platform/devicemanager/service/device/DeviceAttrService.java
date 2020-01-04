package com.hzyw.iot.platform.devicemanager.service.device;

import com.hzyw.iot.platform.devicemanager.domain.device.DeviceAttributeDO;
import com.hzyw.iot.platform.models.transfer.IllegalParameterException;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

/**
 * @author by early
 * @blame IOT Team
 * @date 2019/8/31.
 */
public interface DeviceAttrService {

    void insertDeviceAttr(DeviceAttributeDO vo) throws IllegalParameterException;

    void saveDeviceAttr(DeviceAttributeDO vo);

    DeviceAttributeDO selectDeviceAttr(String key);

    void deleteDeviceAttr(String key);

    /**
     * saveAttrTypeRelation
     *
     * @param deviceTypeId
     * @param attrKey
     */
    void saveAttrTypeRelation(String deviceTypeId, String attrKey);

    /**
     * searchDeviceAttrByType
     *
     * @param deviceTypeId
     * @return
     */
    List<DeviceAttributeDO> searchDeviceAttrByType(String deviceTypeId);

    /**
     * ListDeviceAttr
     *
     * @param curPage
     * @param pageSize
     * @return
     */
    List<DeviceAttributeDO> ListDeviceAttr(Integer curPage, Integer pageSize);

}
