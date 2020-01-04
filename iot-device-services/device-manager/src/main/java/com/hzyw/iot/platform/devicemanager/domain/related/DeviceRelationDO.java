package com.hzyw.iot.platform.devicemanager.domain.related;

import lombok.Data;

/**
 * @author by early
 * @blame IOT Team
 * @date 2019/8/30.
 */

@Data
public class DeviceRelationDO {
    private String poleId;
    private String edgeId;
    private String terminalId;
    private Integer terminalDomain;
}
