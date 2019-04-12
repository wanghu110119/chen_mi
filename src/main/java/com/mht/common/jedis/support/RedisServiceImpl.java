package com.mht.common.jedis.support;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 * 
 * @ClassName: RedisServiceImpl
 * @Description: Redis服务类impl
 * @author 华强
 * @date 2017年6月5日 下午1:37:37 
 * @version 1.0.0
 */
public class RedisServiceImpl implements RedisService {

    @Override
    public String setString(String key, String value, Integer seconds) {
        try (Jedis jedis = RedisSupport.pool.getResource()) {
            return seconds == null ? jedis.setex(key, RedisSupport.defaultExpireSecond, value) : jedis.setex(key,
                    seconds, value);
        } catch (JedisConnectionException e) {
            RedisSupport.isAlive = false;
            RedisSupport.needDelQueue.add(key);
            return null;
        }
    }

    @Override
    public String getString(String key) {
        try (Jedis jedis = RedisSupport.pool.getResource()) {
            return jedis.get(key);
        } catch (JedisConnectionException e) {
            RedisSupport.isAlive = false;
            RedisSupport.needDelQueue.add(key);
            return null;
        }
    }

    @Override
    public String setObject(String key, Serializable object, Integer seconds) {
        try (Jedis jedis = RedisSupport.pool.getResource()) {
            return seconds == null ? jedis.setex(key.getBytes(), RedisSupport.defaultExpireSecond,
                    SerializeUtil.serialize(object)) : jedis.setex(key.getBytes(), seconds,
                    SerializeUtil.serialize(object));
        } catch (JedisConnectionException e) {
            RedisSupport.isAlive = false;
            RedisSupport.needDelQueue.add(key);
            return null;
        }
    }

    @Override
    public Long delByKey(String key) {
        try (Jedis jedis = RedisSupport.pool.getResource()) {
            Long retLong = jedis.del(key.getBytes());
            retLong += jedis.del(key);
            return retLong;
        } catch (JedisConnectionException e) {
            RedisSupport.isAlive = false;
            RedisSupport.needDelQueue.add(key);
            return null;
        }
    }

    @Override
    public Long delByKeys(String keyPattern) {
        try (Jedis jedis = RedisSupport.pool.getResource()) {
            Set<String> keySet = jedis.keys(keyPattern);
            Iterator<String> iterator = keySet.iterator();

            String[] keys = new String[keySet.size()];
            byte[][] keysByte = new byte[keySet.size()][];
            for (int i = 0; i < keySet.size(); i++) {
                String string = iterator.next();
                keys[i] = string;
                keysByte[i] = string.getBytes();
            }
            if (keys.length > 0) {
                Long retLong = jedis.del(keys);
                retLong += jedis.del(keysByte);
                return retLong;
            }
            return 0L;
        } catch (JedisConnectionException e) {
            RedisSupport.isAlive = false;
            RedisSupport.needDelPatternsQueue.add(keyPattern);
            return null;
        }
    }

    @Override
    public Object getObject(String key) {
        try (Jedis jedis = RedisSupport.pool.getResource()) {
            return SerializeUtil.unserialize(jedis.get(key.getBytes()));
        } catch (JedisConnectionException e) {
            RedisSupport.isAlive = false;
            RedisSupport.needDelQueue.add(key);
            return null;
        }
    }

    @Override
    public void refreshJedis(String key, Integer seconds) {
        try (Jedis jedis = RedisSupport.pool.getResource()) {
            if (jedis.exists(key)) {
                jedis.expire(key, seconds);
            }
        } catch (JedisConnectionException e) {
            RedisSupport.isAlive = false;
        }
    }

    /**
     * 发布消息 
     * 当在本系统有数据更新，需要及时通知到其他系统时，就可以调用此方法
     * @param channel:发布渠道 
     * @param message:消息
     */
    @Override
    public void publish(String channel, String message) {
        try (Jedis jedis = RedisSupport.pool.getResource()) {
            jedis.publish(channel, message);
        } catch (JedisConnectionException e) {
            RedisSupport.isAlive = false;
            return;
        }
        
    }

    /**
     * 订阅redis消息
     * 用于服务器间进行传递消息
     * 目前应用于web后台修改数据化，通过发布消息，其他服务端收到消息后，进行数据同步
     * @param channel:渠道
     * @param pubSub:收到消息之后的处理handler
     * 
     */
    @Override
    public void subscribe(JedisPubSub pubSub, String channel) {
        try (Jedis jedis = RedisSupport.pool.getResource()) {
            jedis.subscribe(pubSub,channel);
        } catch (JedisConnectionException e) {
            RedisSupport.isAlive = false;
            return;
        }
        
    }

    /**
     * 返回redis 当前匹配的key集合
     */
    @Override
    public Set<String> getKeys(String keyPattern) {
        try (Jedis jedis = RedisSupport.pool.getResource()) {
           return jedis.keys(keyPattern);
        } catch (JedisConnectionException e) {
            RedisSupport.isAlive = false;
        }
        return null;
    }
}
