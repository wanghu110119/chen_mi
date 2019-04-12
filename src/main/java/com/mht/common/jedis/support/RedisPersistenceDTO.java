package com.mht.common.jedis.support;

import java.io.Serializable;

/**
 * 
 * @ClassName: RedisPersistenceDTO
 * @Description: redis持久化实体类
 * @author 华强
 * @date 2017年6月5日 下午1:36:37 
 * @version 1.0.0
 */
public class RedisPersistenceDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 实体或者list对象
     */
    private Object entity;
    /**
     * 调用类
     */
    private String beanName;
    /**
     * 执行的方法
     */
    private String methodName;

    /**
     * 参数类型
     */
    private Class<?>[] classType;

    public RedisPersistenceDTO(Object entity, String beanName, String methodName, Class<?>[] clazzType) {
        this.entity = entity;
        this.beanName = beanName;
        this.methodName = methodName;
        this.classType = clazzType;
    }

    public Object getEntity() {
        return entity;
    }

    public Class<?>[] getClassType() {
        return classType;
    }

    public void setClassType(Class<?>[] classType) {
        this.classType = classType;
    }

    public void setEntity(Object entity) {
        this.entity = entity;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
}
