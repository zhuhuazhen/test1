package com.hzyw.iot.platform.droolsmanager.domain;

import lombok.Data;

@Data
public class Condition {
    String key;
    Object value;
    String constraint;
}
