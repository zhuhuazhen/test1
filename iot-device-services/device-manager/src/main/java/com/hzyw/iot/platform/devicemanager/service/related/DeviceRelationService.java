package com.hzyw.iot.platform.devicemanager.service.related;

import com.hzyw.iot.platform.devicemanager.mapper.relation.DeviceRelationDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author by early
 * @blame IOT Team
 * @date 2019/9/7.
 */

@Slf4j
@Service
public class DeviceRelationService {

    @Resource
    private DeviceRelationDao relationDao;

    public String getEdgeGateWay(String deviceId) {
        return relationDao.getGatewayId(deviceId);
    }

    public void setEdgeGateWay(String gwId, String deviceId) {
            relationDao.updateGatewayId(gwId,deviceId);
    }

}
