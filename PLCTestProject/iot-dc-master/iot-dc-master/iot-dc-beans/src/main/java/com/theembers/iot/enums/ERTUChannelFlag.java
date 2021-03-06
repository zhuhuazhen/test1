package com.theembers.iot.enums;

/**
 * RTU 通道标记
 *
 * @author TheEmbers Guo
 * @version 1.0
 * createTime 2018-10-22 13:14
 */
public enum ERTUChannelFlag {
    SYS("0#".getBytes()),
    DISPLAY("1#".getBytes()),
    R485("68".getBytes()), //PLC接入  68表示 传递的开头一个字节是 68H 
    R232("3#".getBytes()),
    SIM("4#".getBytes());
	
	//定义控制指令
    

    private byte[] flag;

    ERTUChannelFlag(byte[] flag) {
        this.flag = flag;
    }

    public byte[] getFlag() {
        return flag;
    }

    public void setFlag(byte[] flag) {
        this.flag = flag;
    }
}
