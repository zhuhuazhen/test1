package com.hzyw.iot.platform.devicemanager.service.device.impl;

import com.hzyw.iot.platform.devicemanager.domain.device.DeviceAttributeDO;
import com.hzyw.iot.platform.devicemanager.mapper.device.DeviceAttributeDao;
import com.hzyw.iot.platform.devicemanager.service.device.DeviceAttrService;
import com.hzyw.iot.platform.models.transfer.IllegalParameterException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @author by early
 * @blame IOT Team
 * @date 2019/8/31.
 */
@Slf4j
@Service
public class DeviceAttrServiceImpl implements DeviceAttrService {

    @Resource
    DeviceAttributeDao deviceAttributeDao;

    @Override
    public void insertDeviceAttr(DeviceAttributeDO vo) throws IllegalParameterException {
        if (Objects.isNull(vo) || StringUtils.isEmpty(vo.getAttrKey())) {
            throw new IllegalParameterException("DeviceAttribute.attributeKey is Empty!");
        }
        deviceAttributeDao.insertDeviceAttribute(vo);
        log.info("DeviceAttrServiceImpl.insertDeviceAttr --key:{}", vo.getAttrKey());
    }


    @Override
    public void saveDeviceAttr(DeviceAttributeDO vo) {
        DeviceAttributeDO attributeDO = deviceAttributeDao.getDeviceAttribute(vo.getAttrKey());
        if(attributeDO == null){
            deviceAttributeDao.insertDeviceAttribute(vo);
        }else {
            deviceAttributeDao.updateDeviceAttribute(vo);
        }
    }

    @Override
    public DeviceAttributeDO selectDeviceAttr(String key) {
        return deviceAttributeDao.getDeviceAttribute(key);
    }

    @Override
    public void deleteDeviceAttr(String key) {
        deviceAttributeDao.deleteDeviceAttribute(key);
    }

    @Override
    public void saveAttrTypeRelation(String deviceTypeId, String attrKey) {
        deviceAttributeDao.saveAttrTypeRelation(deviceTypeId, attrKey);
    }

    @Override
    public List<DeviceAttributeDO> searchDeviceAttrByType(String deviceTypeId) {
        return deviceAttributeDao.searchDeviceAttrByType(deviceTypeId);
    }

    @Override
    public List<DeviceAttributeDO> ListDeviceAttr(Integer curPage,Integer pageSize) {
        return deviceAttributeDao.listAllDevicesAttr(curPage,pageSize);
    }

}
