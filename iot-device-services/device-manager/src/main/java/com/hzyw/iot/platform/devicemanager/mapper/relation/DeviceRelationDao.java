package com.hzyw.iot.platform.devicemanager.mapper.relation;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @author by early
 * @blame IOT Team
 * @date 2019/8/31.
 */
public interface DeviceRelationDao {

    @Select("select GATEWAY_ID from DEVICE_ACCESS_T where DEVICE_ID=#{deviceId}")
    public String getGatewayId(String deviceId);

    @Update("update DEVICE_ACCESS_T set GATEWAY_ID=#{gwId} where DEVICE_ID=#{deviceId}")
    public void updateGatewayId(String gwId, String deviceId);

}