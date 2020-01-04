package com.hzyw.iot.platform.devicemanager.service.alarm;

import com.hzyw.iot.platform.devicemanager.mapper.alarm.DeviceErrorDao;
import com.hzyw.iot.platform.devicemanager.mapper.alarm.DeviceSignalDao;
import com.hzyw.iot.platform.models.alarm.SignalAlarmMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author by early
 * @blame IOT Team
 * @date 2019/9/17.
 */
@Slf4j
@Service
public class SignalAlarmMsgService {

    @Resource
    DeviceErrorDao errorDao;

    @Resource
    DeviceSignalDao signalDao;

    public void saveSignal(SignalAlarmMsg sam) {
        signalDao.saveSignal(sam.getCode(),sam.getName(),sam.getMsg());
    }

    public void saveError(SignalAlarmMsg smg) {
        if(smg != null){
            errorDao.saveError(smg.getCode(),smg.getName(),smg.getMsg());
        }
    }

    public SignalAlarmMsg getAlarm(int code, ALARM_TYPE type) {
        if (type == ALARM_TYPE.ERROR) {
            return errorDao.getError(code);
        } else if (type == ALARM_TYPE.SIGNAL) {
            return signalDao.getSignal(code);
        } else if (type == ALARM_TYPE.ALL) {
            return signalDao.selectCode(code);
        } else {
            return null;
        }
    }

    public SignalAlarmMsg getAlarm(int code) {
        return getAlarm(code, ALARM_TYPE.ALL);
    }

    public List<SignalAlarmMsg> listAll(ALARM_TYPE type) {
        if (type == ALARM_TYPE.ERROR) {
            return errorDao.listError();
        } else if (type == ALARM_TYPE.SIGNAL) {
            return signalDao.listSignal();
        } else if (type == ALARM_TYPE.ALL) {
            List<SignalAlarmMsg> list = new ArrayList<>();
            list.addAll(errorDao.listError());
            list.addAll(signalDao.listSignal());
            return list;
        } else {
            return null;
        }
    }

    public static enum ALARM_TYPE {ALL, ERROR, SIGNAL}

}
