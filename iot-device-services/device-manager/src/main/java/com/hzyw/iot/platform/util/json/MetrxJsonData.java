package com.hzyw.iot.platform.util.json;

import lombok.Data;

/**
 * @author by early
 * @blame IOT Team
 * @date 2019/9/21.
 */
@Data
public class MetrxJsonData {
    private String type;
    private Object value;
    private String company; //应该是unit
}
