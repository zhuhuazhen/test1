package com.hzyw.iot.platform.models.equip;

import com.hzyw.iot.platform.models.relation.IotTerminalAccess;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @author by early
 * @blame IOT Team
 * @date 2019/8/8.
 */
public class IotTerminal extends Equipment implements Serializable {
    //标签
    public static final Enum FLAG = EquipmentFlag.DEVICE;
    //属性缓存
    private StatusHistory history = new StatusHistory();

    public IotTerminal(String serialNumber, DeviceType type) {
        super(serialNumber);
        this.setEquipmentType(type);
        history.init(type);
    }

    public IotTerminal(String serialNumber, String deviceId, String deviceAlias, DeviceType type) {
        super(serialNumber, deviceId, deviceAlias, type);
        history.init(type);

    }

    public StatusHistory getHistory() {
        return history;
    }

    public void setHistory(StatusHistory history) {
        this.history = history;
    }

    /**
     * 将最新的状态数据存入缓存
     * @param key
     * @param value
     */
    public void pushStatus(String key, Object value) {
        DeviceAttribute attr = this.getEquipmentType(DeviceType.class).getAttribute(key);
        this.history.push(attr, value);
    }

    public Map invoke(String methodName, Map args) throws NoSuchMethodException {
        IotTerminalAccess access = new IotTerminalAccess(this);
        IotEdgeBox box = access.getSelfAgent();
        if (box != null) {
            //box.invoke
        } else {
            DeviceType deviceType = this.getEquipmentType(DeviceType.class);
            DeviceMethod method = deviceType.getMethod(methodName);
            if (!method.containsAllInput(args.keySet().toArray())) {
                throw new IllegalArgumentException("args");
            }

//           this.invoke()
        }
        return null;
    }

    public Callable<Map> invoke(String methodName, Map args, Map callBack) {
        //验证方法，
        //准备参数
        //开启异步
        //组织报文
        //调用接入网关
        //等待执行结果？
        //收到执行结果
        //
        return null;
    }

    @Override
    public DeviceType getEquipmentType() {
        DeviceType t = super.getEquipmentType(DeviceType.class);
        if (t == null) {
            throw new RuntimeException("No DeviceType!");
        } else {
            return t;
        }
    }

}
