package com.mht.common.jedis.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.mht.common.config.Global;
import com.mht.common.utils.ApplicationContextUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 * 
 * @ClassName: RedisMonitorThread
 * @Description: Redis监控线程
 * @author 华强
 * @date 2017年6月5日 上午11:50:05 
 * @version 1.0.0
 */
@Component
@Scope("prototype")
public class RedisMonitorThread implements Runnable {
    private static final Log LOG = LogFactory.getLog(RedisMonitorThread.class);

    private long sleepMillis = 30000;

    // 持久化线程
    public static RedisPersistenceThread redisPersistenceThread;

    @Override
    public void run() {
        try {
            while (true) {
                try {
                    Thread.sleep(sleepMillis);
                } catch (InterruptedException e) {
                    LOG.error("RedisMonitorThread sleep Interrupted!", e);
                }
                // redis监控线程
                this.serverMonit();
                // 判断是佛需要启动持久化线程
                if (RedisSupport.isAlive == true && RedisSupport.isStart) {
                    LOG.info("Redis Monitor pulse, server is alive, ip=="
                            + Global.getConfig("redis.host"));
                    this.persistenceMonit();
                }

            }
        } catch (Exception e) {
            LOG.error("RedisMonitorThread dead, Restarting!", e);
            this.shutdownAll();
            new Thread((RedisMonitorThread) ApplicationContextUtil.getBean("redisMonitorThread")).start();
        }
    }

    /**
     * 关闭线程池中线程
     *
     * @author ligen 2015年8月10日
     */
    private void shutdownAll() {
        if (redisPersistenceThread != null) {
            redisPersistenceThread.isRunning = false;
            redisPersistenceThread = null;
        }
    }

    /**
     * 当客户端发送请求给redis服务器时，经过此方法监控，
     * 未连接上的请求加入删除队列等待处理
     *
     * @author ligen 2015年8月10日
     */
    private void serverMonit() {
        try (Jedis jedis = RedisSupport.pool.getResource()) {
            if (jedis == null) {
                RedisSupport.isAlive = false;
                this.sleepMillis = 3000;
                LOG.error("can't reach Redis server");
                return;
            }
            jedis.setex("testAlive", 3, "testAlive");

            if (RedisSupport.isAlive == false) {
                this.needDelQueue();
                this.needDelPatternsQueue();
                RedisSupport.isAlive = true;
                this.sleepMillis = 30000;
            }
        } catch (JedisConnectionException e) {
            LOG.error("can't reach Redis server, JedisConnectionException");
            RedisSupport.isAlive = false;
            this.sleepMillis = 3000;
            return;
        }
    }

    /**
     * 检测线程池的数量并启动持久化线程保存线程队列中的实体
     *
     * @author xutaog 2015年9月21日
     */
    private void persistenceMonit() {
        if (redisPersistenceThread == null) {
            redisPersistenceThread = (RedisPersistenceThread) ApplicationContextUtil
                    .getBean("redisPersistenceThread");
            new Thread(redisPersistenceThread).start();
        } else if (redisPersistenceThread != null && redisPersistenceThread.isRunning == false) {
            redisPersistenceThread.isRunning = true;
        }

    }

    /**
     * 删除队列
     *
     * @author ligen 2015年8月10日
     */
    private void needDelQueue() {
        RedisService redisService = new RedisServiceImpl();
        String keyStr = RedisSupport.needDelQueue.poll();
        while (keyStr != null) {
            redisService.delByKey(keyStr);
            keyStr = RedisSupport.needDelQueue.poll();
        }
    }

    /**
     * 多行删除队列
     *
     * @author ligen 2015年8月10日
     */
    private void needDelPatternsQueue() {
        RedisService redisService = new RedisServiceImpl();
        String keyStr = RedisSupport.needDelPatternsQueue.poll();
        while (keyStr != null) {
            redisService.delByKeys(keyStr);
            keyStr = RedisSupport.needDelPatternsQueue.poll();
        }
    }

}
