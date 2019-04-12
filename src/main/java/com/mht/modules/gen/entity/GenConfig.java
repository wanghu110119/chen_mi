/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mht.modules.gen.entity;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.mht.modules.sys.entity.Dict;

/**
 * 生成方案Entity
 * @author ThinkGem
 * @version 2013-10-15
 */
@XmlRootElement(name="config")
public class GenConfig implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private List<GenCategory> categoryList;	// 代码模板分类
	private List<Dict> javaTypeList;		// Java类型
	private List<Dict> queryTypeList;		// 查询类型
	private List<Dict> showTypeList;		// 显示类型
	private List<Dict> jdbcTypeList;//数据字段类型
	private List<Dict> validateTypeList;//数据验证类型

	public GenConfig() {
		super();
	}

	@XmlElementWrapper(name = "category")
	@XmlElement(name = "category")
	public List<GenCategory> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<GenCategory> categoryList) {
		this.categoryList = categoryList;
	}

	@XmlElementWrapper(name = "javaType")
	@XmlElement(name = "dict")
	public List<Dict> getJavaTypeList() {
		return javaTypeList;
	}

	public void setJavaTypeList(List<Dict> javaTypeList) {
		this.javaTypeList = javaTypeList;
	}

	@XmlElementWrapper(name = "queryType")
	@XmlElement(name = "dict")
	public List<Dict> getQueryTypeList() {
		return queryTypeList;
	}

	public void setQueryTypeList(List<Dict> queryTypeList) {
		this.queryTypeList = queryTypeList;
	}

	@XmlElementWrapper(name = "showType")
	@XmlElement(name = "dict")
	public List<Dict> getShowTypeList() {
		return showTypeList;
	}

	public void setShowTypeList(List<Dict> showTypeList) {
		this.showTypeList = showTypeList;
	}

	@XmlElementWrapper(name="jdbcType")
	@XmlElement(name="dict")
	public List<Dict> getJdbcTypeList() {
		return jdbcTypeList;
	}

	public void setJdbcTypeList(List<Dict> jdbcTypeList) {
		this.jdbcTypeList = jdbcTypeList;
	}
	@XmlElementWrapper(name="validateType")
	@XmlElement(name="dict")
	public List<Dict> getValidateTypeList() {
		return validateTypeList;
	}

	public void setValidateTypeList(List<Dict> validateTypeList) {
		this.validateTypeList = validateTypeList;
	}
	
}