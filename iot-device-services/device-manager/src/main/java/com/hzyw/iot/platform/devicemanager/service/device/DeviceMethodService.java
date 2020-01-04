package com.hzyw.iot.platform.devicemanager.service.device;

import com.hzyw.iot.platform.devicemanager.domain.device.DeviceMethodDO;
import com.hzyw.iot.platform.models.transfer.IllegalParameterException;

import java.util.List;

/**
 * @author by early
 * @blame IOT Team
 * @date 2019/9/2.
 */
public interface DeviceMethodService {

    void deleteMethod(Integer methodId);

    DeviceMethodDO selectMethod(Integer methodId);

    /**
     * 新增或按methodId更新设备方法.
     * @param methodDO  需要新增{methodID==null}
     * @throws IllegalParameterException
     */
    void saveMethod(DeviceMethodDO methodDO) throws IllegalParameterException;

    List<DeviceMethodDO> getMethodListByType(String typeId);
}
