package com.hzyw.iot.platform.devicemanager.dao;

import com.hzyw.iot.platform.devicemanager.domain.device.DeviceAccessDO;
import com.hzyw.iot.platform.devicemanager.mapper.alarm.DeviceErrorDao;
import com.hzyw.iot.platform.devicemanager.mapper.alarm.DeviceSignalDao;
import com.hzyw.iot.platform.devicemanager.mapper.device.DeviceAccessDao;
import com.hzyw.iot.platform.devicemanager.mapper.relation.DeviceRelationDao;
import com.hzyw.iot.platform.models.alarm.SignalAlarmMsg;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author by early
 * @blame IOT Team
 * @date 2019/9/17.
 */
@RunWith(SpringRunner.class)
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RelationDaoTest {

    @Resource
    DeviceRelationDao relationDao;
    @Resource
    DeviceAccessDao accessDao;

    private DeviceAccessDO accessDO;

    @Before
    public void setUp() {

    }

    @Test
//    @Rollback(false)
    public void deviceRelation() {
        accessDO = accessDao.getDeviceAccess("1010-341bfdc842f3af1f-3001-ffff-9bad");
        System.out.println(accessDO.getGatewayId());
        String gwId = relationDao.getGatewayId("1010-341bfdc842f3af1f-3001-ffff-9bad");
        Assert.assertEquals(gwId,accessDO.getGatewayId());
    }
}
