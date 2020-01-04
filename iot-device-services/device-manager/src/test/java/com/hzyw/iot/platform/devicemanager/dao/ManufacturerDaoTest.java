package com.hzyw.iot.platform.devicemanager.dao;

import com.hzyw.iot.platform.devicemanager.domain.device.DeviceAttributeDO;
import com.hzyw.iot.platform.devicemanager.domain.device.ManufacturerDO;
import com.hzyw.iot.platform.devicemanager.mapper.device.DeviceAttributeDao;
import com.hzyw.iot.platform.devicemanager.mapper.device.ManufacturerDao;
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
public class ManufacturerDaoTest {
    ManufacturerDO vo = new ManufacturerDO();
    @Resource
    private ManufacturerDao dao;

    @Before
    public void setUp() throws Exception {
        vo.setAddress("222222");
        vo.setContactInfo("DDD");
        vo.setManufacturerCode(3315);
        vo.setManufacturerName("TTT");
    }

    @Test
    public void manufacturerDaoCRUD() {
        dao.insertManufacturer(vo);
        Assert.assertEquals(vo.getManufacturerName(),dao.getManufacturerInfo(vo.getManufacturerCode()).getManufacturerName());
        ManufacturerDO vo1 = dao.getManufacturerInfo(vo.getManufacturerCode());
        vo1.setAddress("33333");
        dao.updateManufacturer(vo1);
        Assert.assertEquals(vo1.getAddress(),dao.getManufacturerInfo(vo1.getManufacturerCode()).getAddress());
        dao.deleteManufacturer(vo.getManufacturerCode());
    }


}
