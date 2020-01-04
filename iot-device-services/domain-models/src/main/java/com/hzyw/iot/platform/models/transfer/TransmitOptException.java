package com.hzyw.iot.platform.models.transfer;

import com.hzyw.iot.platform.models.alarm.SignalAlarmMsg;

/**
 * @author by early
 * @blame IOT Team
 * @date 2019/9/16.
 */
public class TransmitOptException extends DefaultDeviceException {
    public static final TransmitOptException UNDEFINED = new TransmitOptException(99999, "UNDEFINED", "UNDEFINED ERROR!");

    private static final String COLON = ":";
    private int code;
    private String errMsg;
    private String errName;
    private String transactionId;

    public TransmitOptException(int errorCode, String errName, String errorMsg) {
        super(errorCode + COLON + errName, errorMsg);
        this.code = errorCode;
        this.errName = errName;
        this.errMsg = errorMsg;
    }

    public TransmitOptException(SignalAlarmMsg smm) {
        super(smm.getCode() + COLON + smm.getName(), smm.getMsg());
        this.code = smm.getCode();
        this.errName = smm.getName();
        this.errMsg = smm.getMsg();
    }

    public int getCode() {
        return code;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public String getErrName() {
        return errName;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}
