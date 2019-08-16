package com.cube.event;

public enum EventEnum {

    /**
     * 手表-->TCP server
     */
     ONE((short) 1),
    /**
     * app发消息-->TCP server-->手表
     */
     TWO((short) 2),
     /**
      * 超长指令app->tcp 服务-->手表
      */
     THREE((short) 3);

    private short v;

    private EventEnum(short v) {
        this.v = v;
    }

    public short getVal() {
        return v;
    }

    public static EventEnum valuesOf(short e) {
        EventEnum[] vs = EventEnum.values();
        if (vs == null || vs.length == 0) {
            return null;
        }
        for (EventEnum event : vs) {
            if (event.getVal() == e) {
                return event;
            }
        }
        return null;
    }

}
