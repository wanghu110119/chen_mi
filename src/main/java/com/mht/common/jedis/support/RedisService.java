package com.mht.common.jedis.support;

import java.io.Serializable;
import java.util.Set;

import redis.clients.jedis.JedisPubSub;

/**
 * 
 * @ClassName: RedisService
 * @Description: Redis服务类
 * @author 华强
 * @date 2017年6月5日 下午1:37:10 
 * @version 1.0.0
 */
public interface RedisService {

    public String setString(String key, String value, Integer seconds);

    public String getString(String key);

    public String setObject(String key, Serializable object, Integer seconds);

    public Long delByKey(String key);

    public Long delByKeys(String keyPattern);

    public Object getObject(String key);

    public void refreshJedis(String key, Integer seconds);
    
    //发布通知
    public void publish(String channel,String message);
    //订阅通知
    public void subscribe(JedisPubSub pubSub,String channel);
    
    //模糊查找keys
    public Set<String> getKeys(String keyPattern);

}
