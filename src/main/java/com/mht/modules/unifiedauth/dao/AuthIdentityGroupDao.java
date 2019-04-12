/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.mht.modules.unifiedauth.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.mht.common.persistence.CrudDao;
import com.mht.common.persistence.annotation.MyBatisDao;
import com.mht.modules.ident.entity.IdentityGroup;
import com.mht.modules.unifiedauth.entity.AuthIdentityGroup;

/**
 * 
 * @ClassName: AuthIdentityGroupDao
 * @Description: 用户组授权
 * @author com.mhout.zjh
 * @date 2017年3月30日 下午5:33:21 
 * @version 1.0.0
 */
@MyBatisDao
public interface AuthIdentityGroupDao extends CrudDao<AuthIdentityGroup> {
	/**
	 * 
	 * @Title: findListById 
	 * @Description: TODO
	 * @param id
	 * @return
	 * @author com.mhout.zjh
	 */
	List<AuthIdentityGroup> findListByIdentityGroupId(@Param("identityGroupId")String id, @Param("type") String type,
			@Param("status") String status, @Param("accessway") String accessway);
	/**
	 * 
	 * @Title: deleteByIdentityGroupId 
	 * @Description: TODO
	 * @return
	 * @author com.mhout.zjh
	 */
	void deleteByIdentityGroupId(String id);
	/**
	 * 
	 * @Title: findIdentityGroupsByName 
	 * @Description: 通过用户组名称查询
	 * @param groupName
	 * @return
	 * @author com.mhout.zjh
	 */
	List<IdentityGroup> findIdentityGroupsByName(@Param("identityGroupName") String groupName);
    /**
     * @Title: findByAppId
     * @Description: TODO 通过AppId获取认证用户组信息
     * @param string
     * @return
     * @author com.mhout.wj
     */
    @Select("select ag.*,ig.group_name as 'identityGroup.groupName' from auth_identity_group ag "
            + "left join t_identity_group ig on ag.identity_group_id = ig.id where ag.app_id = #{id}")
    public List<AuthIdentityGroup> findByAppId(String id);
}
