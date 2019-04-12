/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.mht.modules.unifiedauth.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.mht.common.persistence.CrudDao;
import com.mht.common.persistence.annotation.MyBatisDao;
import com.mht.modules.ident.entity.Application;
import com.mht.modules.sys.entity.Office;
import com.mht.modules.sys.entity.User;
import com.mht.modules.unifiedauth.entity.AuthUser;

/**
 * @ClassName: AuthUserDao
 * @Description: 用户授权dao
 * @author com.mhout.sx
 * @date 2017年3月30日 下午2:25:01
 * @version 1.0.0
 */
@MyBatisDao
public interface AuthUserDao extends CrudDao<AuthUser> {

    /**
     * @Title: findListByUser
     * @Description: TODO获取应用列表，附带用户是否拥有权限
     * @param officeId
     * @return
     * @author com.mhout.sx
     */
    @Select("select au.id id,au.access_auth accessAuth,iaa.id \"apply.id\",iaa.name \"apply.name\" from "
            + "ident_application iaa left join auth_user au on (iaa.id=au.app_id and au.user_id=#{1}) where iaa.del_flag='0'")
    public List<AuthUser> findListByUser(String userId);

    /**
     * @Title: deleteByUser
     * @Description: TODO删除用户的应用权限
     * @param officeId
     * @author com.mhout.sx
     */
    @Delete("delete from auth_user where user_id=#{1}")
    public void deleteByUser(String userId);

    /**
     * @Title: getApplicationByUser
     * @Description: TODO获取用户拥有的应用
     * @param officeId
     * @return
     * @author com.mhout.sx
     */
    @Select("select ia.id id,ia.name name,ia.serial serial "
            + "from ident_application ia,auth_user au where ia.id=au.app_id and au.user_id=#{1} and au.access_auth='1' and ia.del_flag='0'")
    public List<Application> getApplicationByUser(String userId);

    /**
     * @Title: findOfficeByParent
     * @Description: TODO 通过父组织结构获取子阻止机构
     * @param parentId
     * @return
     * @author com.mhout.sx
     */
    @Select("select id,name from sys_office where parent_id=#{1} and del_flag='0' order by sort asc")
    public List<Office> findOfficeByParent(String parentId);

    /**
     * @Title: findUserByOffice
     * @Description: TODO 获取对应组织机构下用户
     * @param parentId
     * @return
     * @author com.mhout.sx
     */
    @Select("select u.id,u.name from sys_user u inner join sys_user_office uo on (uo.office_id=#{0} and uo.user_id=u.id) where u.del_flag='0' ")
    public List<User> findUserByOffice(String parentId);

    /**
     * @Title: findUserByName
     * @Description: TODO 搜索用户
     * @param userName
     * @return
     * @author com.mhout.sx
     */
    @Select("select u.id id,u.name name,u.login_name loginName,o.name \"office.name\" "
            + " from sys_user u left join sys_office o on u.office_id=o.id where u.name like #{1} and u.del_flag='0'")
    public List<User> findUserByName(String userName);
    
    /**
     * 
     * @Title: findByAppId
     * @Description: TODO 通过应用id,获取用户认证信息
     * @param id
     * @return
     * @author com.mhout.wj
     */
    @Select("select au.*,u.name as 'user.name',u.login_name as 'user.loginName' from auth_user au left join sys_user u on au.user_id=u.id where au.app_id = #{id}")
    public List<AuthUser> findByAppId(String id);
    
    /**
     * @Title: findByUserOfficeIds
     * @Description: 用户组织Ids
     * @param name
     * @return
     * @author com.mhout.xyb
     */
    @Select("SELECT DISTINCT so.id AS 'id',	so.parent_ids AS 'parentIds' FROM sys_user_office AS o LEFT JOIN sys_user AS u ON u.id = o.user_id "
    		+ "LEFT JOIN sys_office AS so ON so.id = o.office_id WHERE u.`name` LIKE concat(concat('%',#{name}),'%')")
    public List<Office> findByUserOfficeIds(String name);
    
    /**
     * @Title: findByUserIdOfficeIds 
     * @Description: 获取用户部门信息
     * @param userId
     * @return
     * @author com.mhout.xyb
     */
    @Select("SELECT o.id as 'id', o.parent_ids as 'parentIds'  FROM sys_office AS o LEFT JOIN sys_user AS u "
    		+ "ON u.office_id = o.id WHERE u.id = #{userId}")
    public List<Office> findByUserIdOfficeIds(String userId);
    
    /**
     * @Title: findListByAuthUser 
     * @Description: 查找用户应用权限
     * @param userId
     * @return
     * @author com.mhout.xyb
     */
    public List<AuthUser> findListByAuthUser(@Param("userId") String userId, @Param("type") String type,
			@Param("status") String status, @Param("accessway") String accessway);
}
