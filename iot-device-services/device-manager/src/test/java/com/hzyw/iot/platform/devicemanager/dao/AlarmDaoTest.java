package com.hzyw.iot.platform.devicemanager.dao;

import com.hzyw.iot.platform.devicemanager.mapper.alarm.DeviceErrorDao;
import com.hzyw.iot.platform.devicemanager.mapper.alarm.DeviceSignalDao;
import com.hzyw.iot.platform.models.alarm.SignalAlarmMsg;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.annotation.Rollback;
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
public class AlarmDaoTest {
    @Resource
    DeviceErrorDao errorDao;
    @Resource
    DeviceSignalDao signalDao;
    private SignalAlarmMsg sam = new SignalAlarmMsg();
    private SignalAlarmMsg sam2 = new SignalAlarmMsg();

    @Before
    public void setUp() {
        sam.setCode(3);
        sam.setName("AAA");
        sam.setMsg("hahahahahaha");
        sam2.setCode(4);
        sam2.setName("BBB");
        sam2.setMsg("sssssssss");
    }

    @Test
//    @Rollback(false)
    public void deviceAlarmDaoCRUD() {
        errorDao.saveError(sam.getCode(), sam.getName(), sam.getMsg());
        Assert.assertEquals(sam.getName(), errorDao.getError(sam.getCode()).getName());
        signalDao.saveSignal(sam2.getCode(),sam2.getName(),sam2.getMsg());
        Assert.assertEquals(sam2.getName(), signalDao.getSignal(sam2.getCode()).getName());
        signalDao.selectCode(10001);
        List<SignalAlarmMsg> a = signalDao.listSignal();
        List<SignalAlarmMsg> b = errorDao.listError();
        List<SignalAlarmMsg> c = new ArrayList<>();
        c.addAll(a);
        c.addAll(b);
        System.out.println(c.size());
    }
}
