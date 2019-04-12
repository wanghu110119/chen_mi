/**
 * Copyright &copy; 2015-2020 <a href="http://www.mht.org/">mht</a> All rights reserved.
 */
package com.mht.modules.fieldconfig.entity;

import org.hibernate.validator.constraints.Length;

import com.mht.common.persistence.DataEntity;
import com.mht.common.utils.excel.annotation.ExcelField;

/**
 * 代码生成模块功能测试Entity
 * @author 张继平
 * @version 2017-03-29
 */
public class FieldConfig extends DataEntity<FieldConfig> {
	
	private static final long serialVersionUID = 1L;
	private String fieldName;		// 字段名称
	private String fieldCName;		// 字段中文名
	private String dataType;		// 数据类型
	private Integer isNecessary;		// 是否必填
	private Integer isModify;		// 是否可修改
	private Integer isUsable;       // 是否可用
	private String listValue;		// 列表值：当字段类型选择单选或者多选，该字段存放可选择的内容，各选项间用英文的;隔开，如：苹果;西瓜;香蕉;
	private Integer expression;		// 表达式 0表示= 1表示<=
	private Integer length;		// 长度
	private String  fieldValue = "";      //用于存储用户在该字段填写的值（该字段没有在对应得表中）
	private String colName;   //在存储数据表中的对应字段，以extend开头
	
	//查询参数
	private String fieldConfigIds;
	private String groupId;
	private String groupRole;
	
	public FieldConfig() {
		super();
	}

	public FieldConfig(String id){
		super(id);
	}

	@Length(min=0, max=64, message="字段名称长度必须介于 0 和 64 之间")
	@ExcelField(title="字段名称", align=2, sort=7)
	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	
	@Length(min=0, max=64, message="字段别名长度必须介于 0 和 64 之间")
	@ExcelField(title="字段中文名", align=2, sort=8)
	public String getFieldCName() {
		return fieldCName;
	}

	public void setFieldCName(String fieldCName) {
		this.fieldCName = fieldCName;
	}
	
	@ExcelField(title="数据类型", align=2, sort=10)
	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	
	@ExcelField(title="是否必填", align=2, sort=12)
	public Integer getIsNecessary() {
		return isNecessary;
	}

	public void setIsNecessary(Integer isNecessary) {
		this.isNecessary = isNecessary;
	}
	
	@ExcelField(title="是否可修改", align=2, sort=13)
	public Integer getIsModify() {
		return isModify;
	}

	public void setIsModify(Integer isModify) {
		this.isModify = isModify;
	}
	
	@ExcelField(title="是否可用", align=2, sort=14)
	public Integer getIsUsable() {
		return isUsable;
	}

	public void setIsUsable(Integer isUsable) {
		this.isUsable = isUsable;
	}

	@Length(min=0, max=200, message="列表值")
	@ExcelField(title="列表值", align=2, sort=15)
	public String getListValue() {
		return listValue;
	}

	public void setListValue(String listValue) {
		this.listValue = listValue;
	}
	
	@ExcelField(title="表达式", align=2, sort=16)
	public Integer getExpression() {
		return expression;
	}

	public void setExpression(Integer expression) {
		this.expression = expression;
	}
	
	@ExcelField(title="长度", align=2, sort=17)
	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public String getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}

	public String getColName() {
		return colName;
	}

	public void setColName(String colName) {
		this.colName = colName;
	}

	public String getFieldConfigIds() {
		return fieldConfigIds;
	}

	public void setFieldConfigIds(String fieldConfigIds) {
		this.fieldConfigIds = fieldConfigIds;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getGroupRole() {
		return groupRole;
	}

	public void setGroupRole(String groupRole) {
		this.groupRole = groupRole;
	}

	
}