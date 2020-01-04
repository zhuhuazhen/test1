package com.hzyw.iot.platform.util;

import com.hzyw.iot.platform.util.math.CRC16;
import com.hzyw.iot.platform.util.math.DeviceIdGenerator;
import com.hzyw.iot.platform.util.math.MD5;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author by early
 * @blame IOT Team
 * @date 2019/9/9.
 */
public class MathTest {

    @Test
    public void contextLoads() {
        DeviceIdGenerator.generatorId("000002000533",4112,12289);
        System.out.println("test: com.hzyw.crc16");
        Assert.assertEquals("74a8", CRC16.crc16_modBus("10303c474cf77e1a0376200dffff".getBytes()));
        Assert.assertEquals("9de3", CRC16.crc16_modBus("1030d393f460ec55c1f3200dffff".getBytes()));
        Assert.assertEquals("05bf", CRC16.crc16_modBus("10300621babd82add79e200dffff".getBytes()));
        Assert.assertEquals("13b5", CRC16.crc16_modBus("10305fc692ab16143487200dffff".getBytes()));
        Assert.assertEquals("5bae", CRC16.crc16_modBus("10305a00228291d2497a200dffff".getBytes()));
        Assert.assertEquals("4819", CRC16.crc16_modBus("103038f6e082eb3fb313200dffff".getBytes()));
        Assert.assertEquals("cdb0", CRC16.crc16_modBus("1030ca0eac3c009f4cd8200dffff".getBytes()));
        Assert.assertEquals("5c02", CRC16.crc16_modBus("10308aacf7381352c8cf200dffff".getBytes()));
        Assert.assertEquals("61e7", CRC16.crc16_modBus("103085e87393b1b44870200dffff".getBytes()));
        Assert.assertEquals("0392", CRC16.crc16_modBus("1030bd5d721b3f8a409f200dffff".getBytes()));
        System.out.println("test: com.hzyw.md5");
        Assert.assertEquals("3c474cf77e1a0376", MD5.md5_16("CLCC40002B8A"));
        Assert.assertEquals("d393f460ec55c1f3", MD5.md5_16("CLCC40002B81"));

        int i = 8205;
        System.out.println(Integer.toHexString(i));
        int hex = 0x103001;
        Integer dec = new Integer(1060865);
        System.out.println(hex);
        Assert.assertTrue(hex == dec.intValue());

        String s = DeviceIdGenerator.generatorId("CLCC40002B8A", 4144, 8205);
        Assert.assertEquals("1030-3c474cf77e1a0376-200d-ffff-74a8", s);

        Assert.assertTrue(DeviceIdGenerator.validateDeviceId("1030-d393f460ec55c1f3-200d-ffff-9de3", "CLCC40002B81"));
        Assert.assertFalse(DeviceIdGenerator.validateDeviceId("1030-d393f460c55c1f3-200d-ffff-9de3", "CLCC4000B81"));
        Assert.assertFalse(DeviceIdGenerator.validateDeviceId("1030-d393f460ec55c1f3-200d-ffff-9ddf", "NULL"));
        Assert.assertTrue(DeviceIdGenerator.validateDeviceId("1030-d393f460ec55c1f3-200d-ffff-9de3", "NULL"));


    }


}
