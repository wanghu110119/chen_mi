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
import com.mht.modules.unifiedauth.entity.AuthOffice;

/**
 * @ClassName: AuthOfficeDao
 * @Description: 部门授权dao
 * @author com.mhout.sx
 * @date 2017年3月29日 下午3:02:54
 * @version 1.0.0
 */
@MyBatisDao
public interface AuthOfficeDao extends CrudDao<AuthOffice> {

	/**
	 * @Title: findListByOffice
	 * @Description: TODO获取应用列表，附带部门是否拥有权限
	 * @param officeId
	 * @return
	 * @author com.mhout.sx
	 */
	// @Select("select ao.id id,ao.access_auth accessAuth,iaa.id
	// \"apply.id\",iaa.name \"apply.name\" from "
	// + "ident_application iaa left join auth_office ao on (iaa.id=ao.app_id
	// and ao.office_id=#{officeId})"
	// + " where iaa.del_flag='0' and iaa.type =#{type} and iaa.status =
	// #{status} and iaa.access_way = #{accessway}")
	public List<AuthOffice> findListByOffice(@Param("officeId") String officeId, @Param("type") String type,
			@Param("status") String status, @Param("accessway") String accessway, @Param("parentIds") List<String> parentIds);

	/**
	 * @Title: deleteByOffice
	 * @Description: TODO删除部门的应用权限
	 * @param officeId
	 * @author com.mhout.sx
	 */
	@Delete("delete from auth_office where office_id=#{1}")
	public void deleteByOffice(String officeId);

	/**
	 * @Title: getApplicationByOffice
	 * @Description: TODO获取部门拥有的应用
	 * @param officeId
	 * @return
	 * @author com.mhout.sx
	 */
	@Select("select ia.id id,ia.name name,ia.serial serial "
			+ "from ident_application ia,auth_office ao where ia.id=ao.app_id and ao.office_id=#{1} and ao.access_auth='1' and ia.del_flag='0'")
	public List<Application> getApplicationByOffice(String officeId);

	/**
	 * @Title: findOfficeByName
	 * @Description: TODO 搜索组织机构
	 * @param string
	 * @return
	 * @author com.mhout.sx
	 */
	@Select("select o1.id id,o1.name name,o1.code code,o2.name \"parent.name\" "
			+ "from sys_office o1 left join sys_office o2 on o1.parent_id=o2.id where o1.name like #{1} and o1.del_flag='0'")
	public List<Office> findOfficeByName(String string);

	/**
	 * @Title: findByAppId
	 * @Description: TODO 通过AppId获取认证部门信息
	 * @param string
	 * @return
	 * @author com.mhout.wj
	 */
	@Select("select ao.*,so.name as 'office.name' from auth_office ao "
			+ "left join sys_office so on ao.office_id = so.id where ao.app_id = #{id}")
	public List<AuthOffice> findByAppId(String id);

	public List<Office> findOfficeByParentAndType(@Param("parentId") String parentId, @Param("type") String type);
}
