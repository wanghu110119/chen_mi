package com.mht.common.jedis.support;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 
 * @ClassName: RedisUtilProxy
 * @Description:  动态代理
 * @author 华强
 * @date 2017年6月5日 下午1:38:51 
 * @version 1.0.0
 */
public class RedisUtilProxy implements InvocationHandler {
    private Object subject;

    public RedisUtilProxy(Object subject) {
        this.subject = subject;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (RedisSupport.isAlive == false) {
            RedisSupport.needDelQueue.add((String) args[0]);
            return null;
        }

        Object retObj = method.invoke(subject, args);
        return retObj;
    }

}
