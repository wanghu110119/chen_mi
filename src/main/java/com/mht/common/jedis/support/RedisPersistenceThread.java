package com.mht.common.jedis.support;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.mht.common.jedis.RedisUtil;
import com.mht.common.jedis.RedisWriteDbDao;

import redis.clients.jedis.Jedis;

/**
 * 
 * @ClassName: RedisPersistenceThread
 * @Description: redis持久化线程，用于从队列中读出并写入数据库
 * @author 华强
 * @date 2017年6月5日 上午11:47:20 
 * @version 1.0.0
 */
@Component
@Scope("prototype")
public class RedisPersistenceThread implements Runnable {
    private static final Log LOG = LogFactory.getLog(RedisPersistenceThread.class);

    private int delayMilSec = 2500;
    protected boolean isRunning = true;

    @Resource
    private RedisWriteDbDao redisWriteDbDao;

    @Override
    public void run() {
        while (isRunning) {
            RedisPersistenceDTO redisPersistenceDTO = null;

            try (Jedis jedis = RedisUtil.pool.getResource()) {
                byte[] tempBytes = jedis.lpop(RedisUtil.REDIS_PERS_NAME.getBytes());
                if (tempBytes != null) {
                    redisPersistenceDTO = (RedisPersistenceDTO) SerializeUtil.unserialize(tempBytes);
                }
            }
            if (redisPersistenceDTO != null) {
                try {
                    redisWriteDbDao.createRedisEntity(redisPersistenceDTO);
                } catch (Exception e) {
                    LOG.error("----------redis visitor保存失败！错误原因：" + e.getMessage());
                }
            } else {
                try {
                    Thread.sleep(this.delayMilSec);
                } catch (InterruptedException e) {
                    LOG.error("", e);
                }
            }
        }
    }
}
