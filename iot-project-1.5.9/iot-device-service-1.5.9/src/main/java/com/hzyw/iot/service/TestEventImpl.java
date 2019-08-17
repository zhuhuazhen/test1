package com.hzyw.iot.service;

import com.hzyw.iot.adapter.event.IStartupEvent;
import com.hzyw.iot.adapter.event.ITestEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 *  
 */
@Component
public class TestEventImpl implements ITestEvent {
    private static Logger logger = LoggerFactory.getLogger(TestEventImpl.class);
    @Override
    public int order() {
        return 0;
    }

    @Override
    public void execute(Object... objects) {
        logger.info("iot-device-service接收到消息啦。。。。。。");

    }
}
