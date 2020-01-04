package com.hzyw.iot.platform.models.relation;

import com.hzyw.iot.platform.models.equip.ActiveFlag;
import com.hzyw.iot.platform.models.equip.IotEdgeBox;
import com.hzyw.iot.platform.models.equip.IotTerminal;
import com.hzyw.iot.platform.models.equip.Pole;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author by early
 * @blame IOT Team
 * @date 2019/8/8.
 */
public class IotTerminalAccess implements IDeviceAccess {

    private IotTerminal terminal;
    private IotEdgeBox box;
    private Pole pole;

    public IotTerminalAccess(IotTerminal terminal) {
        this.terminal = terminal;
    }

    @Override
    public IotEdgeBox getSelfAgent() {
        String id = terminal.getDeviceId();
        //TODO：search edgeBox Id  for DB or cache with Terminal Id;
//        box = new IotEdgeBox("xxxx","sss");
        return null;
    }

    @Override
    public List<IotTerminal> listInSelfAgency() {
        return new ArrayList<IotTerminal>();
    }

    @Override
    public Pole getSelfLocatedPole() {
        String id = terminal.getDeviceId();
        //TODO：search pole Id  for DB or cache with Terminal Id;
       pole = new Pole("xxxxx");
        return pole;
    }

    @Override
    public ActiveFlag getActiveStatus() {
//        TODO：
        return ActiveFlag.ACTIVATE;
    }

    @Override
    public Date getActionEffectiveTime(ActiveFlag flag) {
//        TODO：
        switch (flag){
            case ACTIVATE:
                return new Date();
            case DETACHED:
                return new Date();
            case UNACTIVATED:
                return new Date();
            default:
                return null;
        }
    }
}
