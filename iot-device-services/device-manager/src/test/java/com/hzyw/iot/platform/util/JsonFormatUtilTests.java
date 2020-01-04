//package com.hzyw.iot.platform.util;
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.serializer.SerializerFeature;
//import com.hzyw.iot.platform.devicemanager.service.device.DeviceAccessService;
//import com.hzyw.iot.platform.util.json.JsonFormatUtil;
//import com.hzyw.iot.platform.util.json.MessageVO;
//import com.hzyw.iot.platform.util.json.PropertyVO;
//import com.hzyw.iot.platform.util.json.TransmitDataVO;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = JsonFormatUtilTests.class)
//public class JsonFormatUtilTests {
//
//    String a1 = "{\"data\":{\"attributers\":[{\"vendor_code\":8196,\"device_type_name\":\"Camera\",\"version_software\":\"v1.01\",\"vendor_name\":\"Hikvision\",\"ipaddr_v4\":\"192.168.3.249\",\"ipaddr_v6\":\"\",\"uuid\":\"1020-f82d132f9bb018ca-2004-ffff-ed35\",\"version_hardware\":\"v1.01\",\"malfunction\":0,\"date_of_production\":\"2019-08-30\",\"mac_addr\":\"68:6d:bc:19:3e:d7\",\"device_type_code\":4128,\"up_time\":1568944797,\"model\":\"HIKVISION_007\",\"sn\":\"1234567890\"}],\"definedAttributers\":[{\"third_height\":720,\"main_height\":1080,\"onvif_name\":\"admin\",\"onvif_passwd\":\"Admin123\",\"main_stream_url\":\"rtsp://admin:Admin123@192.168.3.249:554/Streaming/Channels/101?transportmode=unicast&profile=Profile_1\",\"second_height\":576,\"second_stream_url\":\"rtsp://admin:Admin123@192.168.3.249:554/Streaming/Channels/102?transportmode=unicast&profile=Profile_2\",\"third_stream_url\":\"rtsp://admin:Admin123@192.168.3.249:554/Streaming/Channels/103?transportmode=unicast&profile=Profile_3\",\"main_width\":1920,\"third_width\":1280,\"second_width\":704}],\"definedMethods\":[\"set_ptz\",\"move_ptz\",\"move_to_ptz\",\"stop_ptz\",\"remove_ptz\",\"remove_all_ptz\",\"get_stream_url\",\"set_zoom\",\"set_aperture\",\"set_focus\",\"set_capture\"],\"id\":\"1020-f82d132f9bb018ca-2004-ffff-ed35\",\"methods\":[\"set_time\",\"reboot\"],\"signals\":[{\"signal_code\":1056769}]},\"gwId\":\"1000-f82d132f9bb018ca-2001-ffff-d28a\",\"msgId\":\"c98f4b63-b33d-40f5-8f3c-bbfb8b9efa2b\",\"timestamp\":1568944799,\"type\":\"devInfoResponse\"}";
//
//    @Autowired
//    DeviceAccessService accessService;
//
//    @Test
//    public void contextLoads() {
//        System.out.println("JsonFormat-Request");
//
//        MessageVO messageVO=new MessageVO();
//        List<PropertyVO> propertyVOS=new ArrayList<PropertyVO>();
//        List<PropertyVO> property=new ArrayList<PropertyVO>();
//        PropertyVO propertyVO1=new PropertyVO();
//        PropertyVO pro=new PropertyVO();
//        PropertyVO pro2=new PropertyVO();
//        pro.setPropertyKey("电流");
//        pro.setPropertyValue("100");
//        pro.setUnit("mA");
//        propertyVO1.setPropertyKey("电压");
//        propertyVO1.setPropertyValue("50");
//        propertyVO1.setUnit("mV");
//        pro2.setPropertyKey("有功功率");
//        pro2.setPropertyValue("100");
//        pro2.setUnit("W");
//        propertyVOS.add(pro);
//        propertyVOS.add(propertyVO1);
//        property.add(pro2);
//
//        messageVO.setId("ASD123456");
//        transmitDataVO.setData(messageVO);
//        transmitDataVO.setType("devInfoResponse");
//        String fastJson= JSON.toJSONString(transmitDataVO, SerializerFeature.DisableCircularReferenceDetect);
//        System.out.println("---------传入的模型数据-----------");
//        System.out.println(fastJson);
//        System.out.println("---------开始转换------------");
//        TransmitDataVO transmit= JsonFormatUtil.format(fastJson);
//        System.out.println("---------转换后的模型数据---------");
//        System.out.println(transmit);
//        System.out.println("---------获取模型数据里的属性值-------");
////        for (PropertyVO propertyVO:transmit.getData().getAttributers()){
////            System.out.println(propertyVO);
////        }
//    }
//
//    @Test
//    public void testFormat2(){
//        String str = "{\"data\":{\"id\":\"1010-3f7b3eb6bffe6fb1-2009-ffff-0be7\",\"methods\":[{\"method\":\"set_onoff\",\"out\":[{}]}]},\"gwId\":\"1000-f82d132f9bb018ca-2001-ffff-d28a\",\"messageCode\":0,\"msgId\":\"06e81a97-058d-4557-9b5d-c7b02c302752\",\"timestamp\":1568519825,\"type\":\"response\"}";
//        JSON.toJavaObject(JSON.parseObject(str), TransmitDataVO.class);
//    }
//
//}
