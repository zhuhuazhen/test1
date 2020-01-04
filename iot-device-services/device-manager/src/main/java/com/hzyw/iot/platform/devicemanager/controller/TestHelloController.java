package com.hzyw.iot.platform.devicemanager.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author by early
 * @blame IOT Team
 * @date 2019/8/22.
 */

@RestController
@RequestMapping("/sdk")
public class TestHelloController {
    @GetMapping("/hello")
    public String testHello(String hello) {
        return hello + ", connection complete!";
    }
}
