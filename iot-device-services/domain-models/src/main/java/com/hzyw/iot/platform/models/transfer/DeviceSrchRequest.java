package com.hzyw.iot.platform.models.transfer;

import com.hzyw.iot.platform.models.equip.Equipment;

import java.util.HashMap;
import java.util.Map;

/**
 * @author by early
 * @blame IOT Team
 * @date 2019/8/21.
 */
public class DeviceSrchRequest implements ITransferRequest {

    /**
     * 审计预留 UserId, RoleId, TenantId, ApplicationId;
     */
    private Map<String, String> audit = new HashMap<>();

    private Map<String, Equipment> equipments = new HashMap<>();

    public void setParameter(Map map) {
        map.keySet().stream().forEach(e -> {
            if (map.get(e) instanceof Equipment) {
                equipments.put(e.toString(), (Equipment) map.get(e));
            }
        });
    }

    public void setAudit(Map map) {

    }

    @Override
    public void addParameter(String key, Object value) {

    }

    @Override
    public void addAudit(String key, Object value) {

    }
}
