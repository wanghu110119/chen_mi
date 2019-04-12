package com.mht.common.jedis.support;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Queue;
import java.util.concurrent.LinkedTransferQueue;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import com.mht.common.config.Global;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 
 * @ClassName: RedisSupport
 * @Description: Redis支撑类
 * @author 华强
 * @date 2017年6月5日 下午1:37:54 
 * @version 1.0.0
 */
@Component
public class RedisSupport implements InitializingBean {

    protected static JedisPool pool = new JedisPool(new JedisPoolConfig(),
    		Global.getConfig("redis.host"), 
    		Integer.valueOf(Global.getConfig("redis.port")), 
    		Integer.valueOf(Global.getConfig("redis.timeout")),
    		Global.getConfig("redis.password"));
    // 3 month 60 * 60 * 24 * 30 * 3
    protected final static int defaultExpireSecond = 7776000;
    // 是否存活
    public static boolean isAlive = true;

    // 是否启动持久化线程
    public static boolean isStart = false;
    // 删除队列
    protected static Queue<String> needDelQueue = new LinkedTransferQueue<>();
    protected static Queue<String> needDelPatternsQueue = new LinkedTransferQueue<>();
    protected static RedisService redisService;
    static {
        RedisService redisServiceTemp = new RedisServiceImpl();
        InvocationHandler redisUtilProxy = new RedisUtilProxy(redisServiceTemp);
        redisService = (RedisService) Proxy.newProxyInstance(redisUtilProxy.getClass().getClassLoader(),
                redisServiceTemp.getClass().getInterfaces(), redisUtilProxy);
        //获取持久化配置
        String startStr = Global.getConfig("redis.persistence.start");
        if (StringUtils.isNotEmpty(startStr)) {
            isStart = Boolean.parseBoolean(startStr);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
   
    }
}
