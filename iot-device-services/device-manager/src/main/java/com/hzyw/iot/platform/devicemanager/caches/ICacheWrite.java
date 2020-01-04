package com.hzyw.iot.platform.devicemanager.caches;

/**
 * @author by early
 * @blame IOT Team
 * @date 2019/9/2.
 */
public interface ICacheWrite<T> {

    /**
     * 将指定的值放入缓存，无论原先是否有值。
     *
     * @param key
     * @param t
     * @return
     */
    T put(String key, T t);

    /**
     * 删除物理存储，并删除缓存.
     *
     * @param key
     */
    void delete(String key);

    /**
     * 清理整个缓存容器
     *
     * @param cacheName
     */
    void clear(String cacheName);


}
