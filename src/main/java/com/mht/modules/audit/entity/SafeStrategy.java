package com.mht.modules.audit.entity;

import com.mht.common.persistence.DataEntity;
import com.mht.modules.sys.entity.Dict;

/**
 * @ClassName: SafeStrategy
 * @Description: 安全策略配置
 * @author com.mhout.xyb
 * @date 2017年4月20日 下午1:40:43
 * @version 1.0.0
 */
public class SafeStrategy extends DataEntity<SafeStrategy> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Dict dict; // 类型（1.账号策略 2.密码策略 3.日志策略 4.权限策略 5.SSO策略）
	private String label; // 标注
	private String code; // 标注代码
	private String value; // 显示值
	private String paramType; // 参数类型（如：select、radio）
	private String paramValue; // 参数值
	private String defaultValue; // 默认值
	private String dcr; // 描述

	public Dict getDict() {
		return dict;
	}

	public void setDict(Dict dict) {
		this.dict = dict;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getParamType() {
		return paramType;
	}

	public void setParamType(String paramType) {
		this.paramType = paramType;
	}

	public String getParamValue() {
		return paramValue;
	}

	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getDcr() {
		return dcr;
	}

	public void setDcr(String dcr) {
		this.dcr = dcr;
	}
	
}
