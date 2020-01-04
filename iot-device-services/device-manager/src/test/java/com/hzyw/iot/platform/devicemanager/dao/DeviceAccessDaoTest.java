package com.hzyw.iot.platform.devicemanager.dao;

import com.hzyw.iot.platform.devicemanager.domain.device.DeviceAccessDO;
import com.hzyw.iot.platform.devicemanager.mapper.device.DeviceAccessDao;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;

/**
 * @author by early
 * @blame IOT Team
 * @date 2019/8/31.
 */
@RunWith(SpringRunner.class)
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ComponentScan(basePackages = {"com.hzyw.iot.platform.dao"})
public class DeviceAccessDaoTest {
    DeviceAccessDO vo = new DeviceAccessDO();
    @Resource
    private DeviceAccessDao dao;

    @Before
    public void setUp() throws Exception {
        vo.setDeviceDomain(12);
        vo.setDeviceId("112212-werwer-343113");
        vo.setLatitude("11W");
        vo.setLongitude("22E");
        vo.setProtocol("HTTP");
        vo.setRegistration(1);
        vo.setLocationType("locationType");
        vo.setAccessTime(new Date());
        System.out.println(vo.getAccessTime());
        vo.setProtocolVersion("version");
        vo.setCreateTime(Calendar.getInstance().getTime());
        vo.setLeaveTime(null);

        vo.setDeviceIPv4("12.1.1.1");
        vo.setDeviceIPv6("23:sr3r:we:w");
        vo.setDevicePort("303");
        vo.setGatewayId("12345768ffs");
    }

    @Test
    public void deviceAccessDaoCRUD() {
        dao.insertDeviceAccess(vo);
        DeviceAccessDO vo1 = dao.getDeviceAccess(vo.getDeviceId());
        Assert.assertEquals(vo.getLatitude(), vo1.getLatitude());
        Assert.assertEquals(vo.getDeviceDomain(), vo1.getDeviceDomain());
        Assert.assertEquals(vo.getDeviceId(), vo1.getDeviceId());
        Assert.assertEquals(vo.getAccessTime().getDate(), vo1.getAccessTime().getDate());
        Assert.assertEquals(vo.getProtocolVersion(), vo1.getProtocolVersion());
        Assert.assertEquals(vo.getDeviceIPv4(), vo1.getDeviceIPv4());
        Assert.assertEquals(vo.getGatewayId(),vo1.getGatewayId());
        vo.setDevicePort("2233");
        vo.setGatewayId("sssd3");
        dao.updateDeviceAccess(vo);
        Assert.assertEquals(vo.getDevicePort(), dao.getDeviceAccess(vo.getDeviceId()).getDevicePort());
        Assert.assertEquals("sssd3",vo.getGatewayId());
        dao.deleteDeviceAccess(vo1.getDeviceId());
    }


}
