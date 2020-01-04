package com.hzyw.iot.platform.devicemanager.dao;

import com.hzyw.iot.platform.devicemanager.domain.device.DeviceAttributeDO;
import com.hzyw.iot.platform.devicemanager.mapper.device.DeviceAttributeDao;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
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
public class DeviceAttrDaoTest {
    DeviceAttributeDO vo = new DeviceAttributeDO();
    DeviceAttributeDO vo2 = new DeviceAttributeDO();
    @Resource
    private DeviceAttributeDao deviceAttributeDao;

    @Before
    public void setUp() throws Exception {
        vo.setAttrKey("VV");
        vo.setAttrName("XXX");
        vo.setUnit("V");
        vo.setValueType("string");
        vo2.setAttrKey("V2");
        vo2.setAttrName("XXX2");
        vo2.setUnit("V2");
        vo2.setValueType("object");
    }

    @Test
    public void deviceAttrDaoCRUD() {
        deviceAttributeDao.insertDeviceAttribute(vo);
        deviceAttributeDao.insertDeviceAttribute(vo2);
        Assert.assertEquals("XXX", deviceAttributeDao.getDeviceAttribute("VV").getAttrName());
        vo.setUnit("CC");
        deviceAttributeDao.updateDeviceAttribute(vo);
        Assert.assertEquals("CC", deviceAttributeDao.getDeviceAttribute("VV").getUnit());
        deviceAttributeDao.saveAttrTypeRelation("dd-33-23", vo.getAttrKey());
        deviceAttributeDao.saveAttrTypeRelation("dd-33-23", vo2.getAttrKey());
        //test replace
        deviceAttributeDao.saveAttrTypeRelation("dd-33-23", vo2.getAttrKey());
        List<DeviceAttributeDO> list = deviceAttributeDao.searchDeviceAttrByType("dd-33-23");
        Assert.assertEquals(2, list.size());
        System.out.println(list.get(0).getAttrName());

    }
    @After
    public void tearDown(){
        deviceAttributeDao.deleteDeviceAttribute(vo.getAttrKey());
        deviceAttributeDao.deleteDeviceAttribute(vo2.getAttrKey());
    }
}
