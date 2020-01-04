package com.hzyw.iot.platform.models.equip;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * 盒子
 *
 * @author by early
 * @blame IOT Team
 * @date 2019/8/8.
 */
public final class IotEdgeBox extends Equipment implements Serializable {
    //标签
    public static final Enum FLAG = EquipmentFlag.HZYW_BOX;
    //属性缓存
    private Map<DeviceAttribute<?>, LinkedList> statusStack = new HashMap<>();
    //特性元数据
    private Map metaData = new HashMap();

    public IotEdgeBox(Equipment equipment) {
        super(equipment.getSerialNumber(), equipment.getDeviceId(), equipment.getDeviceAlias(), equipment.getEquipmentType());
        this.setBatchNumber(equipment.getBatchNumber());
        this.setExpireDate(equipment.getExpireDate());
        this.setProductionDate(equipment.getProductionDate());
        this.setManufacturer(equipment.getManufacturer());
        buildStatusCache(equipment.getEquipmentType(DeviceType.class));
    }

    public IotEdgeBox(String serialNumber, DeviceType deviceType) {
        super(serialNumber);
        this.setEquipmentType(deviceType);
        buildStatusCache(deviceType);
    }

    private void buildStatusCache(DeviceType deviceType) {
        deviceType.getAttributes().values().stream().forEach(e -> {
            statusStack.put(e, new LinkedList<>());
        });
    }

    public Map getMetaData() {
        return metaData;
    }

    public void setMetaData(Map metaData) {
        this.metaData = metaData;
    }


//    private ArrayList<Device> proxies；


}
