/**
 * Copyright &copy; 2015-2020 <a href="http://www.mht.org/">mht</a> All rights reserved.
 */
package com.mht.modules.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.mht.common.persistence.TreeDao;
import com.mht.common.persistence.annotation.MyBatisDao;
import com.mht.modules.sys.entity.Role;

/**
 * 角色DAO接口
 * 
 * @author mht
 * @version 2013-12-05
 */
@MyBatisDao
public interface RoleDao extends TreeDao<Role> {

    public Role getByName(Role role);

    public Role getByEnname(Role role);

    public List<Role> findUserRole(Role role);

    /**
     * 维护角色与菜单权限关系
     * 
     * @param role
     * @return
     */
    public int deleteRoleMenu(Role role);

    public int insertRoleMenu(Role role);
    
    public int deleteRoleRootMenu(@Param("roleId")String roleId, @Param("menuId")String menuId);

    /**
     * 维护角色与公司部门关系
     * 
     * @param role
     * @return
     */
    public int deleteRoleOffice(Role role);

    public int insertRoleOffice(Role role);

    /**
     * @Title: getAllRoleForUser
     * @Description: TODO 获取所有，一次加载树结构
     * @return
     * @author com.mhout.sx
     */
    @Select("select id,name,enname,parent_ids parentIds,parent_id \"parent.id\" from sys_role where del_flag='0'")
    public List<Role> getAllRoleForUser();

    /**
     * @Title: getAllRoleForUser2
     * @Description: TODO 获取所有，一次加载树结构 ，并选中用户已拥有的
     * @param id
     * @return
     * @author com.mhout.sx
     */
    @Select("select id,name,enname,parent_ids parentIds,parent_id \"parent.id\",ur.user_id otherId "
            + "from sys_role r left join sys_user_role ur on r.id=ur.role_id and ur.user_id=#{0} where r.del_flag='0'")
    public List<Role> getAllRoleForUser2(String id);

    /**
     * @Title: deleteRoleUser
     * @Description: TODO 删除用户与角色对应
     * @param userId
     * @author com.mhout.sx
     */
    @Delete("delete from sys_user_role where user_id=#{0}")
    public void deleteRoleUser(String userId);

    /**
     * @Title: insertRoleUser
     * @Description: TODO 保存用户与角色对应
     * @param userId
     * @param roleId
     * @author com.mhout.sx
     */
    @Insert("insert into sys_user_role(user_id,role_id) VALUES(#{0},#{1})")
    public void insertRoleUser(String userId, String roleId);

    /**
     * @Title: getByEnname2
     * @Description: TODO 通过英文名获取角色
     * @param enname
     * @return
     * @author com.mhout.sx
     */
    @Select("select id from sys_role where enname=#{0} and del_flag='0'")
    public Role getByEnname2(String enname);

    /**
     * @Title: findRoleByUser
     * @Description: TODO 查询用户拥有的角色,除开默认角色
     * @param userId
     * @param roleType
     * @return
     * @author com.mhout.sx
     */
    @Select("select id,name from sys_role r,sys_user_role ur where ur.user_id=#{0} and r.id=ur.role_id and r.del_flag='0' and r.enname<>#{1} ")
    public List<Role> findRoleByUser(String userId, String roleType);

    /**
     * @Title: findRoleByUserId
     * @Description: TODO 查询用户拥有的角色
     * @param userId
     * @return
     * @author com.mhout.sx
     */
    @Select("select id,name from sys_role r,sys_user_role ur where ur.user_id=#{0} and r.id=ur.role_id and r.del_flag='0' ")
    public List<Role> findRoleByUserId(String userId);
    
    @Select("select id from sys_role where enname = #{0} and del_flag='0'")
    public List<Role> findRoleByEnname(String enname);

}
