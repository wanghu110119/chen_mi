/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.mht.modules.unifiedauth.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.mht.common.persistence.CrudDao;
import com.mht.common.persistence.annotation.MyBatisDao;
import com.mht.modules.sys.entity.Role;
import com.mht.modules.unifiedauth.entity.AuthRole;

/**
 * 
 * @ClassName: AuthRoleDao
 * @Description: 角色授权
 * @author com.mhout.zjh
 * @date 2017年3月29日 下午5:22:59
 * @version 1.0.0
 */
@MyBatisDao
public interface AuthRoleDao extends CrudDao<AuthRole> {
	/**
	 * 
	 * @Title: findListById
	 * @Description: TODO
	 * @param roleId
	 * @return
	 * @author com.mhout.zjh
	 */
	List<AuthRole> findListByRoleId(@Param("roleId") String roleId, @Param("type") String type,
			@Param("status") String status, @Param("accessway") String accessway,
			@Param("parentIds") List<String> parentIds);

	/**
	 * 
	 * @Title: deleteByRoleId
	 * @Description: TODO
	 * @return
	 * @author com.mhout.zjh
	 */
	// @Delete("delete from auth_role ar where ar.role_id = #{roleId}")
	void deleteByRoleId(String roleId);

	/**
	 * 
	 * @Title: findRoleByName
	 * @Description: 通过角色名称查询
	 * @param roleName
	 * @return
	 * @author com.mhout.zjh
	 */
	List<Role> findRolesByName(@Param("roleName") String roleName);

	/**
	 * @Title: findByAppId
	 * @Description: TODO 通过AppId获取认证角色信息
	 * @param string
	 * @return
	 * @author com.mhout.wj
	 */
	@Select("select ar.*,sr.name as 'role.name' from auth_role ar "
			+ "left join sys_role sr on ar.role_id = sr.id where ar.app_id = #{id}")
	public List<AuthRole> findByAppId(String id);
}
