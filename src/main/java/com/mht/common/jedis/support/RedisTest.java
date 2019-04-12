package com.mht.common.jedis.support;


import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.mht.common.jedis.RedisUtil;

/**
 * 
 * @ClassName: RedisTest
 * @Description: 测试redis
 * @author 华强
 * @date 2017年6月5日 下午1:38:18 
 * @version 1.0.0
 */
public class RedisTest {
    @SuppressWarnings({ "unused", "resource" })
    public static void main(String[] args) {
        /**
         * 上述的代码会带来一个问题：因为它会重新装载applicationContext.xml并实例化上下文bean,
         * 如果有些线程配置类也是在这个配置文件中，那么会造成做相同工作的的线程会被启两次。一次是web容器初始化时启动，
         * 另一次是上述代码显示的实例化了一次。这在业务上是要避免的。
         */

       
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
          "classpath*:spring/spring-redis.xml");
      

        // /city_common/src/main/resources/spring/spring-core.xml

        /*
         * JedisPool pool = new JedisPool(new
         * JedisPoolConfig(),ConfigUtils.getString("redis.server.ip", ""),
         * Integer.parseInt(ConfigUtils.getString("redis.server.port", "")));
         */
        // JedisPool pool = new JedisPool(new JedisPoolConfig(),"10.6.10.89",6379);
        // System.out.println(pool.getResource().set("12", "122"));
        // System.out.println(pool.getResource().get("12"));
        // System.out.println(ConfigUtils.getString("redis.server.ip", ""));
        // System.out.println(ConfigUtils.getString("redis.server.port", ""));
        // // RedisUtil.pool.getResource();
        // System.out.println(RedisUtil.tryLock("testLock", 3));
        // System.out.println(RedisUtil.tryLock("testLock", 3));
        // System.out.println(RedisUtil.getString("testLock"));
        // System.out.println("---------------------------------");
        // pool.close();
        // 自动记录
        // RedisMonitorThread redisMonitorThread = new RedisMonitorThread();
        // System.out.println(redisMonitorThread.executor.getActiveCount());
//        RedisServiceImpl service = new RedisServiceImpl();
//        service.setString("1", "cheng", 10);
//        System.out.println(service.getString("1"));
//        service.refreshJedis("1", 10);
//        System.out.println(service.getString("1"));
//
//        Test test = new Test();
//        test.setId(1);
//        test.setName("lg");

    }
}

class lockThread1 extends Thread {
    @Override
    public void run() {
        for (int i = 0; i < 15; i++) {
            System.out.println("1===" + RedisUtil.tryLock("testLockKey", 3));
        }
    }
}

class lockThread2 extends Thread {
    @Override
    public void run() {
        for (int i = 0; i < 15; i++) {
            System.out.println("2===" + RedisUtil.tryLock("testLockKey", 3));
        }
    }
}