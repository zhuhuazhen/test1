package com.hzyw.iot.platform.devicemanager.dao;

import com.hzyw.iot.platform.devicemanager.domain.device.DeviceTypeDO;
import com.hzyw.iot.platform.devicemanager.mapper.device.DeviceTypeDao;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author by early
 * @blame IOT Team
 * @date 2019/8/31.
 */
@RunWith(SpringRunner.class)
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DeviceTypeDaoTest {

    DeviceTypeDO vo = new DeviceTypeDO();
    DeviceTypeDO vo1 = new DeviceTypeDO();
    @Resource
    private DeviceTypeDao dao;

    @Before
    public void setUp() throws Exception {
        vo.setCommunicationPorts("1");
        vo.setDeviceDomain(1122);
        vo.setHardwareVersion("12-e44");
        vo.setTypeCode("C55");
        vo.setManufacturerCode(2302);
        vo.setTypeName("Hello");

        vo1.setDeviceDomain(112);
        vo1.setHardwareVersion("12-e44");
        vo1.setTypeCode("C55");
        vo1.setManufacturerCode(2302);
        vo1.setTypeName("HelloX");
    }

    @Test
//    @Rollback(false)
    public void deviceTypeDaoCRUD() {
        dao.insertDeviceType(vo);
        dao.insertDeviceType(vo1);
        List<DeviceTypeDO> list = dao.selectDeviceType(vo.getTypeCode(), vo.getDeviceDomain(), vo.getManufacturerCode());
        Assert.assertEquals(1,list.size());
        System.out.println(list.get(0).getTypeName());
        DeviceTypeDO vv =  list.get(0);
        Assert.assertEquals(vv.getTypeName(), dao.getDeviceType(vv.getTypeCode(), vv.getDeviceDomain(), vv.getManufacturerCode()).getTypeName());

        vv.setTypeName("SSS");
        dao.updateDeviceTypeDetail(vv);
        Assert.assertEquals(vv.getTypeCode(), dao.getDeviceType(vv.getTypeCode(), vv.getDeviceDomain(), vv.getManufacturerCode()).getTypeCode());

        vo1.setTypeCode("code");
        dao.saveDeviceType(vo1);
        Assert.assertNotEquals(vo1.getTypeCode(), dao.getDeviceType(vo1.getTypeCode(), vo1.getDeviceDomain(), vo1.getManufacturerCode()).getTypeName());
        Assert.assertEquals(vo1.getTypeName(), dao.getDeviceType("code", vo1.getDeviceDomain(), vo1.getManufacturerCode()).getTypeName());


    }

    @After
    public void tearDown(){
        dao.deleteDeviceType(vo.getTypeCode(), vo.getDeviceDomain(), vo.getManufacturerCode());
        dao.deleteDeviceType(vo1.getTypeCode(), vo1.getDeviceDomain(), vo1.getManufacturerCode());
    }
}
