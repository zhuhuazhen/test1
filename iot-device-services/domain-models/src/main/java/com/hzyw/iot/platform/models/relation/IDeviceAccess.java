package com.hzyw.iot.platform.models.relation;

import com.hzyw.iot.platform.models.equip.ActiveFlag;
import com.hzyw.iot.platform.models.equip.IotEdgeBox;
import com.hzyw.iot.platform.models.equip.IotTerminal;
import com.hzyw.iot.platform.models.equip.Pole;

import java.util.Date;
import java.util.List;

/**
 * @author by early
 * @blame IOT Team
 * @date 2019/8/8.
 */
public interface IDeviceAccess {
    IotEdgeBox getSelfAgent();
    List<IotTerminal> listInSelfAgency();
    Pole getSelfLocatedPole();
    ActiveFlag getActiveStatus();
    Date getActionEffectiveTime(ActiveFlag flag);
}
