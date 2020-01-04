package com.hzyw.iot.platform.devicemanager.service.device.impl;

import com.hzyw.iot.platform.devicemanager.domain.device.DeviceMethodDO;
import com.hzyw.iot.platform.devicemanager.mapper.device.DeviceMethodDao;
import com.hzyw.iot.platform.devicemanager.service.device.DeviceMethodService;
import com.hzyw.iot.platform.models.equip.DeviceMethod;
import com.hzyw.iot.platform.models.transfer.DefaultDeviceException;
import com.hzyw.iot.platform.models.transfer.IllegalParameterException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author by early
 * @blame IOT Team
 * @date 2019/9/2.
 */
@Slf4j
@Service
public class DeviceMethodServiceImpl implements DeviceMethodService {

    @Resource
    DeviceMethodDao methodDao;

    @Override
    public void deleteMethod(Integer methodId) {
        methodDao.deleteMethod(methodId);
    }

    @Override
    public DeviceMethodDO selectMethod(Integer methodId) {
        return methodDao.selectMethodById(methodId);
    }

    @Override
    public void saveMethod(DeviceMethodDO methodDO) throws IllegalParameterException {
        String regex = "[\\w]*,";
        if(StringUtils.isEmpty(methodDO.getMethodName())){
                throw new IllegalParameterException("DeviceMethod.methodName is Empty!");
        }
        if(!methodDO.getMethodIn().matches(regex)||!methodDO.getMethodOut().matches(regex)){
            throw new IllegalParameterException("DeviceMethod.methodInOrOut format error!");
        }
        if(methodDO.getMethodId() ==null){
            methodDao.insertMethod(methodDO); //insert的SQL已经做了4要素去重校验.
        }else{
            methodDao.updateMethod(methodDO);
        }
    }

    @Override
    public List<DeviceMethodDO> getMethodListByType(String typeId) {
        return methodDao.getMethodsByDeviceType(typeId);
    }
}
