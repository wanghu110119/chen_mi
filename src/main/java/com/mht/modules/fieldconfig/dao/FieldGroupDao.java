/**
 * Copyright &copy; 2015-2020 <a href="http://www.mht.org/">mht</a> All rights reserved.
 */
package com.mht.modules.fieldconfig.dao;

import java.util.List;

import com.mht.common.persistence.CrudDao;
import com.mht.common.persistence.annotation.MyBatisDao;
import com.mht.modules.fieldconfig.entity.FieldGroup;
import com.mht.modules.fieldconfig.entity.FieldGroupConfig;

/**
 * 代码生成模块功能测试DAO接口
 * @author 张继平
 * @version 2017-03-31
 */
@MyBatisDao
public interface FieldGroupDao extends CrudDao<FieldGroup> {

	/**
	 * 删除和分组绑定的字段
	 * @param fieldGroup
	 */
	public void deleteGroupConfigByGroup(FieldGroup fieldGroup);

	/**
	 * 新增分组和字段的关系
	 * @param groupConfig
	 */
	public void insertGroupConfig(FieldGroupConfig groupConfig);

	/**
	 * 根据分组角色获取所有该角色下的字段分组
	 * @param groupRole
	 * @return
	 */
	public List<FieldGroup> findFieldGroupByGroupRole(String groupRole);

	/**
	 * 根据分组名称获取对应得分组
	 * @param group
	 * @return
	 */
	public Object getFieldGroupByGroupName(FieldGroup group);

}