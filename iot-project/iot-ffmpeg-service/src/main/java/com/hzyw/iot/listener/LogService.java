package com.hzyw.iot.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
//import com.hzyw.iot.utils.IotInfoConstant;
//import com.hzyw.iot.vo.dc.GlobalInfo;

/**
 * @author TheEmbers Guo
 */
@Component
public class LogService implements CommandLineRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogService.class);

    @Override
    public void run(String... strings) throws Exception {
       /* while (true) {
            StringBuilder sb = new StringBuilder("GlobalInfo.SN_CHANNEL_INFO_MAP.size(): " + GlobalInfo.SN_CHANNEL_INFO_MAP.size() + "\n");
            if (!CollectionUtils.isEmpty(GlobalInfo.SN_CHANNEL_INFO_MAP)) {
                GlobalInfo.SN_CHANNEL_INFO_MAP.forEach((k, v) -> sb.append("sn: ").append(k).append(" , info: ").append(v).append("\n"));
            }
            LOGGER.info(sb.toString());
            StringBuilder sbsb = new StringBuilder("GlobalInfo.iotMapper.size(): " + IotInfoConstant.allDevInfo.get("12345").size() + "\n");
            if (!CollectionUtils.isEmpty(IotInfoConstant.allDevInfo.get("12345"))) {
            	IotInfoConstant.allDevInfo.get("12345").forEach((k, v) -> sbsb.append("sn: ").append(k).append(" , DEV: ").append(JSON.toJSONString(v)).append("\n"));
            }
            LOGGER.info(sbsb.toString());
            
            StringBuilder sbsbsb = new StringBuilder("GlobalInfo.CHANNEL_INFO_MAP.size(): " + GlobalInfo.CHANNEL_INFO_MAP.size() + "\n");
            if (!CollectionUtils.isEmpty(GlobalInfo.CHANNEL_INFO_MAP)) {
                GlobalInfo.CHANNEL_INFO_MAP.forEach((k, v) -> sbsbsb.append("sn: ").append(k).append(" , channel: ").append(JSON.toJSONString(v)).append("\n"));
            }
            LOGGER.info(sbsbsb.toString());
            
            Thread.sleep(1000*20);
        }*/
    }
}
