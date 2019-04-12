package com.mht.common.jedis.support;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.mht.common.utils.ApplicationContextUtil;

/**
 * 
 * @ClassName: RedisListener
 * @Description: 监听事件 作用是启动监控线程池并执行持久化线程
 * @author 华强
 * @date 2017年6月5日 下午1:36:07 
 * @version 1.0.0
 */
public class RedisListener implements ServletContextListener {
    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // 新建监控线程池
        RedisMonitorThread redisMonitorThread = new RedisMonitorThread();
        // 启动监控线程
        new Thread(redisMonitorThread).start();

        // 判断是佛需要启动持久化线程
        if (RedisSupport.isStart) {
            // 创建持久化线程
            RedisPersistenceThread persistenceThread = (RedisPersistenceThread) ApplicationContextUtil
                    .getBean("redisPersistenceThread");
            // 设置到静态redis的监控程序中
            RedisMonitorThread.redisPersistenceThread = persistenceThread;
            // 启动线程
            new Thread(persistenceThread).start();
        }

    }
}
