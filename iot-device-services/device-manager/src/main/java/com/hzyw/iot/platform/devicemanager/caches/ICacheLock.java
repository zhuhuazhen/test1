package com.hzyw.iot.platform.devicemanager.caches;

import java.util.concurrent.TimeUnit;

/**
 * @author by early
 * @blame IOT Team
 * @date 2019/9/2.
 */
public interface ICacheLock<T> {

    boolean tryLock(String key, T t, long timeout, TimeUnit unit);

    boolean release(String key);

    boolean refresh(String key, long timeout, TimeUnit unit);

}
