package com.hzyw.iot.platform.kafka;

import com.hzyw.iot.platform.devicemanager.kafka.KafkaMsgProcessor;
import com.hzyw.iot.platform.models.transfer.DefaultDeviceException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author by early
 * @blame IOT Team
 * @date 2019/9/23.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {KafkaAutoConfiguration.class})
public class KafkaMsgProcessTest {

    private KafkaMsgProcessor processor;
   String a1 = "{\"data\":{\"attributers\":[{\"vendor_code\":8205,\"device_type_name\":\"LED大屏1\",\"version_software\":\"1.52.6\",\"vendor_name\":\"Colorlight interconnection\",\"ipaddr_v4\":\"192.168.3.66\",\"ipaddr_v6\":\"2001:0db8:85a3:08d3:1319:8a2e:0370:7345\",\"uuid\":\"1030-7d0b8386f7f2a120-200d-ffff-7ead\",\"version_hardware\":\"c4-1.52.6\",\"malfunction\":0,\"date_of_production\":\"2019-08-27\",\"mac_addr\":\"C1:B2:B2:C2:0B:0B\",\"device_type_code\":4144,\"up_time\":0,\"model\":\"c4\",\"sn\":\"CLCC400025C8\"}],\"definedAttributers\":[{\"company\":\"1\",\"type\":\"on_play\",\"value\":1},{\"company\":\"1\",\"type\":\"on_off\",\"value\":0},{\"company\":\"cd\",\"type\":\"brightness\",\"value\":150},{\"company\":\"pix\",\"type\":\"screen_width\",\"value\":128},{\"company\":\"pix\",\"type\":\"screen_height\",\"value\":256},{\"company\":\"ms\",\"type\":\"uptime\",\"value\":31914265},{\"company\":\"byte\",\"type\":\"mem_total\",\"value\":1073741824},{\"company\":\"byte\",\"type\":\"mem_free\",\"value\":798720000},{\"company\":\"byte\",\"type\":\"storage_total\",\"value\":5878841344},{\"company\":\"byte\",\"type\":\"storage_free\",\"value\":798720000},{\"company\":\"k\",\"type\":\"colortemp\",\"value\":10000},{\"company\":\"1\",\"type\":\"volume\",\"value\":12},{\"company\":\"frame\",\"type\":\"fps\",\"value\":60},{\"company\":\"pclk\",\"type\":\"hsync\",\"value\":26},{\"company\":\"hz\",\"type\":\"dclk\",\"value\":3000000}],\"definedMethods\":[],\"id\":\"1030-7d0b8386f7f2a120-200d-ffff-7ead\",\"methods\":[\"reboot\",\"set_time\",\"set_brightness\",\"set_onoff\",\"set_screenshot\",\"set_clean_program\",\"set_download_program\",\"set_switch_program\"],\"signals\":[{\"screen_offline\":1060865}],\"tags\":{}},\"gwId\":\"1000-f82d132f9bb018ca-2001-ffff-d28a\",\"msgId\":\"6780f2b5-84b8-42e4-b104-9e92cfa87dbc\",\"timestamp\":1569311150,\"type\":\"devInfoResponse\"}\n";

    @Before
    public void setUp(){
        processor = new KafkaMsgProcessor();
    }
    @Test
    public void contextLoads() {

//        try {
//            processor.processAccess(a1);
//        } catch (DefaultDeviceException e) {
//            e.printStackTrace();
//        }
    }
}
