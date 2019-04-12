/**
 * Copyright &copy; 2015-2020 <a href="http://www.mht.org/">mht</a> All rights reserved.
 */
package com.mht.modules.fieldconfig.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.mht.common.persistence.CrudDao;
import com.mht.common.persistence.annotation.MyBatisDao;
import com.mht.modules.fieldconfig.entity.FieldConfig;
import com.mht.modules.fieldconfig.entity.FieldGroup;

/**
 * 代码生成模块功能测试DAO接口
 * @author 张继平
 * @version 2017-03-29
 */
@MyBatisDao
public interface FieldConfigDao extends CrudDao<FieldConfig> {

	/**
	 * 根据字段类型获取该类型下所有可用字段
	 * @param fieldType
	 * @return
	 */
	public List<FieldConfig> findListByFieldType(@Param("fieldType")Integer fieldType);

	/**
	 * 通过字段名获取扩展字段
	 * @param fieldName
	 * @return
	 */
	public Object getFieldConfigByFieldName(FieldConfig config);

	/**
	 * 获取可用字段列表(除去不可用字段以及被其他分组用掉的字段)
	 * @param groupId分组id
	 * @param groupRole分组所属角色
	 * @return
	 */
	public List<FieldConfig> findUsableFieldConfigList(FieldConfig config);

	/**
	 * 根据分组获取分组中的字段列表
	 * @param fieldGroup
	 * @return
	 */
	public List<FieldConfig> getFieldConfigByGroup(FieldGroup fieldGroup);
	
	/**
	 * @Title: getGroupByFieldConfig 
	 * @Description: 根据组字段获取列表
	 * @param config
	 * @return
	 * @author com.mhout.xyb
	 */
	public List<FieldConfig> getGroupByFieldConfig(FieldConfig config);
}