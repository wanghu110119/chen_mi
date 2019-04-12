package com.mht.modules.ident.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.mht.common.persistence.CrudDao;
import com.mht.common.persistence.annotation.MyBatisDao;
import com.mht.modules.ident.entity.Application;
import com.mht.modules.sys.entity.User;

/**
 * @ClassName: ApplicationDao
 * @Description: 应用管理Dao
 * @author com.mhout.xyb
 * @date 2017年3月29日 下午4:07:45
 * @version 1.0.0
 */
@MyBatisDao
public interface ApplicationDao extends CrudDao<Application> {

    /**
     * 
     * @Title: findByUserAndType
     * @Description: TODO 通过应用类型查询用户拥有的应用
     * @param user
     * @param string
     * @return
     * @author com.mhout.wj
     */
    List<Application> findByUserAndType(String id, String type);
    
    /**
     * 
     * @Title: findAllSystemApps
     * @Description: TODO 查询所有系统级应用
     * @return
     * @author com.mhout.wj
     */
    List<Application> findAllSystemApps();
    
    List<Application> findSys(Application application);
    
    List<User> findManagerUser( @Param("application")Application application, @Param("userId")String userId);
    
    @Select("select id from ident_application where serial=#{0} and secret=#{1}")
    List<Application> findAppAuth(String appId, String secret);
    
    /**
     * @Title: findByName 
     * @Description: 通过名称查询应用是否存在
     * @param application
     * @return
     * @author com.mhout.xyb
     */
    List<Application> findByName(Application application);

}
