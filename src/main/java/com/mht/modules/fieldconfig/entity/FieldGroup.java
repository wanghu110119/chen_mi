/**
 * Copyright &copy; 2015-2020 <a href="http://www.mht.org/">mht</a> All rights reserved.
 */
package com.mht.modules.fieldconfig.entity;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.mht.common.persistence.DataEntity;
import com.mht.common.utils.excel.annotation.ExcelField;

/**
 * 代码生成模块功能测试Entity
 * @author 张继平
 * @version 2017-03-31
 */
public class FieldGroup extends DataEntity<FieldGroup> {
	
	private static final long serialVersionUID = 1L;
	private String groupName;		// 分组英文名
	private String groupCName;		// 分组中文名
	private String groupType;		// 分组类型
	private String groupRole;		// 所属角色
	
	private List<FieldConfig> configs = new ArrayList<FieldConfig>(); //分组中包含的字段
	
	public FieldGroup() {
		super();
	}

	public FieldGroup(String id){
		super(id);
	}

	@Length(min=1, max=64, message="分组英文名长度必须介于 1 和 64 之间")
	@ExcelField(title="分组英文名", align=2, sort=7)
	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	@Length(min=1, max=64, message="分组中文名长度必须介于 1 和 64 之间")
	@ExcelField(title="分组中文名", align=2, sort=8)
	public String getGroupCName() {
		return groupCName;
	}

	public void setGroupCName(String groupCName) {
		this.groupCName = groupCName;
	}
	
	public String getGroupType() {
		return groupType;
	}

	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}

	public String getGroupRole() {
		return groupRole;
	}

	public void setGroupRole(String groupRole) {
		this.groupRole = groupRole;
	}

	/**
	 * 分组中包含的字段
	 * @return
	 */
	public List<FieldConfig> getConfigs() {
		return configs;
	}

	public void setConfigs(List<FieldConfig> configs) {
		this.configs = configs;
	}
	
	
}