package com.mht.common.jedis;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mht.common.jedis.support.RedisPersistenceDTO;
import com.mht.common.jedis.support.RedisSupport;
import com.mht.common.jedis.support.SerializeUtil;
import com.mht.common.json.AjaxJson;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 * 
 * @ClassName: RedisUtil
 * @Description: Redis缓存对外提供服务类 
 * @author 华强
 * @date 2017年6月5日 下午12:01:57 
 * @version 1.0.0
 */
public class RedisUtil extends RedisSupport {

    private static final Log logger = LogFactory.getLog(RedisUtil.class);

    public static int DEFAULT_SEESION_SECONDS = 20 * 24 * 60 * 60;

    /**
     * 持久化对象的名称
     */
    public static final String REDIS_PERS_NAME = "redisPersistenceVisitor";

    /**
     * 获取并发锁
     *
     * @param lockKey 锁名称
     * @param expSeconds 过期时间
     * @return 成功则返回true，失败则返回false
     * @throws JedisConnectionException
     * @author sunjianliang 2014年11月17日
     */
    public static boolean tryLock(String lockKey, int expSeconds) throws JedisConnectionException {
        try (Jedis jedis = RedisUtil.pool.getResource()) {
            String status = jedis.set(lockKey, "", "NX", "EX", expSeconds);
            return "OK".equals(status);
        } catch (JedisConnectionException e) {
            RedisSupport.isAlive = false;
            throw e;
        }
    }

    /**
     * 解锁并发锁，能保证解锁成功
     *
     * @param lockKey
     * @author sunjianliang 2014年11月17日
     * @return 如果连接失败，则返回null
     */
    public static Long unLock(String lockKey) {
        return redisService.delByKey(lockKey);
    }

    /**
     * 缓存String键值对，如果连接异常则删除此键值对
     *
     * @param key
     * @param value
     * @param seconds 过期秒数，为null则表示可以缓存很长时间
     * @return 如果存储失败，则返回null
     * @author sunjianliang 2014年11月5日
     */
    public static String setString(String key, String value, Integer seconds) {
        return redisService.setString(key, value, seconds);
    }

    /**
     * 获取String键值对，如果连接异常则删除此键值对
     *
     * @param key
     * @return
     * @author sunjianliang 2014年11月17日
     */
    public static String getString(String key) {
        return redisService.getString(key);
    }

    /**
     * 缓存Object键值对，如果连接异常则删除此键值对
     *
     * @param key
     * @param object
     * @param seconds 过期秒数，为null则表示可以缓存很长时间
     * @return 如果存储失败，则返回null
     * @author sunjianliang 2014年11月5日
     */
    public static String setObject(String key, Serializable object, Integer seconds) {
        return redisService.setObject(key, object, seconds);
    }

    /**
     * 删除缓存，可保证删除成功
     *
     * @param key
     * @return 删除的数量，如果连接失败，则返回null
     * @author sunjianliang 2014年11月13日
     */
    public static Long delByKey(String key) {
        return redisService.delByKey(key);
    }

    /**
     * 谨慎使用此方法，效率比较低下。切忌直接使用*做匹配
     * 按正则表达式批量删除缓存，可保证删除成功
     * Glob style patterns examples:
     * h?llo will match hello hallo hhllo
     * h*llo will match hllo heeeello
     * h[ae]llo will match hello and hallo, but not hillo
     * Use \ to escape special chars if you want to match them verbatim
     *
     * @param keyPattern
     * @return 删除的数量，如果连接失败，则返回null
     * @author sunjianliang 2014年11月17日
     */
    public static Long delByKeys(String keyPattern) {
        return redisService.delByKeys(keyPattern);
    }

    /**
     * 获取Object键值对，如果连接异常则删除此键值对
     *
     * @param key
     * @return
     * @author sunjianliang 2014年11月17日
     */
    public static Object getObject(String key) {
        return redisService.getObject(key);
    }

    /**
     * 刷新redis缓存时间
     *
     * @param key 需要刷新的对象的key值（redis中的（key,value）键值对中的key）
     * @param seconds 时间（秒）
     * @author ligen 2015年8月11日
     */
    public static void refreshJedis(String key, Integer seconds) {
        redisService.refreshJedis(key, seconds);
    }

    /**
     * 存储redis的持久化实体
     *
     * @param entity 实现了序列的参数实体 如User对象
     * @param beanName 执行保存的bean的id 如保存User的beanName是userMapper
     * @param methodName 执行保存的方法 如保存User的方法是createUser
     * @param classType 执行保存的实体参数的类型 如User的参数为new Class[] { User.class }
     * @return
     * @author xutaog 2015年9月23日
     */
    public static AjaxJson persistenceEntity(Object entity, String beanName, String methodName, Class<?>[] classType) {
    	AjaxJson ajaxJson = new AjaxJson();
        if (!RedisSupport.isAlive) {
            logger.error("redis server is down!");
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg("redis服务已关闭!");
        } else {
            try (Jedis jedis = RedisSupport.pool.getResource()) {
                jedis.rpush(REDIS_PERS_NAME.getBytes(),
                        SerializeUtil.serialize(new RedisPersistenceDTO(entity, beanName, methodName, classType)));
            } catch (JedisConnectionException e) {
                RedisSupport.isAlive = false;
                logger.error("错误原因：" + e.getMessage(), e);
                ajaxJson.setSuccess(false);
                ajaxJson.setMsg("存储队列失败！错误原因：" + e.getMessage());                
            }
        }
        return ajaxJson;

    }

    /**
     * 持久化传输过来的list数组
     * methodName是保存的方法且保存单个实体的方法
     * 不支持批量保存
     *
     * @param datas list数组 datas中存放的是单个实体且是实现了序列化的参数实体 如 实现了序列的参数实体 如User对象
     * @param beanName 执行保存的bean的id 如保存User的beanName是userMapper
     * @param methodName 执行保存的方法 如保存User的方法是createUser
     * @param classType 执行保存的实体参数的类型 如User的参数为new Class[] { User.class }
     * @return
     * @author xutaog 2015年9月23日
     */
    public static AjaxJson persistenceList(List<?> datas, String beanName, String methodName, Class<?>[] classType) {
    	AjaxJson ajaxJson = new AjaxJson();
        if (datas != null) {
            if (!RedisSupport.isAlive) {
                logger.error("redis server is down!");
                ajaxJson.setSuccess(false);
                ajaxJson.setMsg("redis服务已关闭!");
            } else {
                try (Jedis jedis = RedisSupport.pool.getResource()) {
                    // 循环向队列中添加实体对象
                    for (int i = 0; i < datas.size(); i++) {
                        jedis.rpush(REDIS_PERS_NAME.getBytes(), SerializeUtil.serialize(new RedisPersistenceDTO(datas
                                .get(i), beanName, methodName, classType)));
                    }
                } catch (JedisConnectionException e) {
                    RedisSupport.isAlive = false;
                    logger.error("错误原因：" + e.getMessage(), e);
                    ajaxJson.setSuccess(false);
                    ajaxJson.setMsg("存储队列失败！错误原因：" + e.getMessage());     
                }
            }
        } else {
        	logger.error("传输的list实体对象为空！");
        	ajaxJson.setSuccess(false);
            ajaxJson.setMsg("传输的list实体对象为空！"); 
        }
        return ajaxJson;
    }
    
   /**
    * 
    * @Title: publish 
    * @Description: redis发布消息的方法，订阅此渠道的用户，就能收到此消息
    * @param channel
    * @param message void
    * @author huaqiang
    */
    public static void publish(String channel,String message){
        redisService.publish(channel, message);
    }
    
    /**
     * 
     * @Title: subscribe 
     * @Description: 订阅redis消息
     * 				   用于服务器间进行传递消息
     * 				   目前应用于web后台修改数据化，通过发布消息，其他服务端收到消息后，进行数据同步
     * @param pubSub
     * @param channel void
     * @author huaqiang
     */
    public static void subscribe(JedisPubSub pubSub,String channel){
        redisService.subscribe(pubSub, channel);
    }
    
    /**
     * 模糊查找keys
     * 方法的注释
     *
     * @param keyPattern
     * @return
     * @author zhongdt 2016年3月29日
     */
    public static Set<String> getKeys(String keyPattern){
        return redisService.getKeys(keyPattern);
    }

}
