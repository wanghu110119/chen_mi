package com.mht.common.jedis;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import com.mht.common.jedis.support.RedisPersistenceDTO;
import com.mht.common.utils.ApplicationContextUtil;

/**
 * 
 * @ClassName: RedisWriteDbDao
 * @Description: redis持久化写入数据库的方法
 * @author 华强
 * @date 2017年6月5日 下午1:39:50 
 * @version 1.0.0
 */
@Repository
public class RedisWriteDbDao {

    private static final Log logger = LogFactory.getLog(RedisWriteDbDao.class);

   /**
    * 
    * @Title: createRedisEntity 
    * @Description: 反射调用方法执行保存的方法
    * @param dto void
    * @author huaqiang
    */
    public void createRedisEntity(RedisPersistenceDTO dto) {
        try {
            Object object = ApplicationContextUtil.getBean(dto.getBeanName());
            Method method = object.getClass().getDeclaredMethod(dto.getMethodName(), dto.getClassType());
            method.invoke(object, dto.getEntity());
        } catch (NoSuchMethodException | SecurityException e) {
            logger.error("保存错误！错误原因：" + e.getMessage(), e);
        } catch (IllegalAccessException e) {
            logger.error("保存错误！错误原因：" + e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            logger.error("保存错误！错误原因：" + e.getMessage(), e);
        } catch (InvocationTargetException e) {
            logger.error("保存错误！错误原因：" + e.getMessage(), e);
        }
    }

}
