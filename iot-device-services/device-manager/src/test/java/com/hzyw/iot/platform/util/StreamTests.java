package com.hzyw.iot.platform.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = StreamTests.class)
public class StreamTests {

    @Test
    public void contextLoads() {
//        EquipmentFlag.DEVICE.getIndex();
//        EquipmentFlag.valueOf("盒子");

        HashMap map = new HashMap();
        map.put("111",111);
        map.put("222",222);
        map.put("333",333);
        map.put("444",444);
        Object o = map.keySet().stream().filter(k->k.equals("222")).findAny();
        long c = map.keySet().stream().filter(k->!k.equals("222")).count();
        map.keySet().stream().filter(k->!k.equals("222")).collect(Collectors.<Integer>toList());
        map.values();
        List<String> a = new ArrayList<String>();
        a.add("112");
        a.add("113");
        a.add("3434");
        List<String> sd = a.stream().filter(e->e.contains("11")).collect(Collectors.toList());
        System.out.println(sd.size());


    //        UUID x = UUID.fromString("sss");
//        UUID b = UUID.fromString("sss");
//        System.out.println(UUID.fromString("sss").toString());
//        Assert.assertNotEquals(x.toString(),b.toString());
    }

}
