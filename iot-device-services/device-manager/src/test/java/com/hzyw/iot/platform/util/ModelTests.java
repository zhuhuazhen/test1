package com.hzyw.iot.platform.util;

import com.hzyw.iot.platform.devicemanager.domain.device.DeviceTypeDO;
import com.hzyw.iot.platform.models.equip.DeviceType;
import com.hzyw.iot.platform.models.equip.EquipmentFlag;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ModelTests.class)
public class ModelTests {

    @Test
    public void contextLoads() {
        DeviceType  a = new DeviceType("11E", EquipmentFlag.LAMP,345,"美的智能灯");
//        a.setTypeId("ssss");
        DeviceTypeDO deviceTypeDO = new DeviceTypeDO();
        deviceTypeDO.setTypeCode("11E1");
        deviceTypeDO.setDeviceDomain(3);
        deviceTypeDO.setManufacturerCode(222);
    }

}
