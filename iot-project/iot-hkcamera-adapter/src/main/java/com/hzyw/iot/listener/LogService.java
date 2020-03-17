package com.hzyw.iot.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.hzyw.iot.sdk.InitSdk;
import com.hzyw.iot.utils.IotInfoConstant;
import com.hzyw.iot.vo.dc.GlobalInfo;

/**
 * @author TheEmbers Guo
 * @version 1.0
 * createTime 2018-11-16 14:04
 */
@Component
public class LogService implements CommandLineRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogService.class);

    @Override
    public void run(String... strings) throws Exception {
    	InitSdk.init();
    }
}
