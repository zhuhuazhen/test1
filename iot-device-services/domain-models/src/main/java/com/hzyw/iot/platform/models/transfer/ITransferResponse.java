package com.hzyw.iot.platform.models.transfer;

/**
 * @author by early
 * @blame IOT Team
 * @date 2019/8/16.
 */
public interface ITransferResponse {
    /**
     * 返回是否成功
     * @return
     */
    boolean success();

    /**
     * 故障时返回错误原因
     * @return
     */
    IotError getError();

    /**
     * 具体的返回值
     * @return
     */
    <T> T getResponse();

}
