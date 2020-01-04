package com.hzyw.iot.platform.models.transfer;

import java.util.Map;

/**
 * @author by early
 * @blame IOT Team
 * @date 2019/8/16.
 */
public interface ITransferRequest {

    /**
     * 设置参数
     * @param key
     * @param value
     */
    void addParameter(String key, Object value);

    /**
     * 设置审计（身份与权限）对象，安全开启的状态下，缺失审计会被拒绝调用
     * @param key
     * @param value
     */
    void addAudit(String key, Object value);
}
