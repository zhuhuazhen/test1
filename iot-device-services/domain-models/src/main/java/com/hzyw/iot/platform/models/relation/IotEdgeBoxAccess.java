package com.hzyw.iot.platform.models.relation;

import com.hzyw.iot.platform.models.equip.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author by early
 * @blame IOT Team
 * @date 2019/8/11.
 */
public class IotEdgeBoxAccess implements IDeviceAccess {
    private IotEdgeBox box;
    private List<IotTerminal> terminals;
    private Pole pole;

    public IotEdgeBoxAccess(IotEdgeBox box) {
        this.box = box;
    }

    @Override
    public IotEdgeBox getSelfAgent() {
        return box;
    }

    @Override
    public List<IotTerminal> listInSelfAgency() {
        //TODO： search Terminals By Box Id
        ArrayList a = new ArrayList<IotTerminal>();
//        a.add(new IotTerminal("xxxccxc", new DeviceType("fsss", EquipmentFlag.getEquipmentFlagByIndex(1), "sd")));
        return a;
    }

    @Override
    public Pole getSelfLocatedPole() {
        //TODO: search pole by box Id;
        return new Pole("dfsfsss");
    }

    @Override
    public ActiveFlag getActiveStatus() {
        //TODO: search ActiveFlag at now.
        return ActiveFlag.ACTIVATE;
    }

    @Override
    public Date getActionEffectiveTime(ActiveFlag flag) {
        //TODO: search Active History
        return new Date();
    }
}
