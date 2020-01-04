package com.hzyw.iot.platform.devicemanager.dao;

import com.hzyw.iot.platform.devicemanager.domain.device.DeviceMethodDO;
import com.hzyw.iot.platform.devicemanager.mapper.device.DeviceMethodDao;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author by early
 * @blame IOT Team
 * @date 2019/8/31.
 */
@RunWith(SpringRunner.class)
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DeviceMethodDaoTest {
    DeviceMethodDO vo = new DeviceMethodDO();
    @Resource
    private DeviceMethodDao dao;

    @Before
    public void setUp() throws Exception {
        vo.setMethodId(223);
        vo.setDeviceType("VV");
        vo.setMethodDescription("this is test");
        vo.setMethodName("Name");
        vo.setMethodIn("[w,w,we]");
        vo.setMethodOut("[{sdf,ssd,w33}]");
    }

    @Test
//    @Rollback(false)
    public void deviceAttrDaoCRUD() {
        dao.insertMethod(vo);
        vo.setMethodName("HAHA");
        dao.updateMethod(vo);
        String act = dao.selectMethodById(223).getMethodName();
        Assert.assertEquals("HAHA", act);
        dao.deleteMethod(223);

    }


}
