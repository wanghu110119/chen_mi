package com.mht.modules.sys.entity;

import com.mht.common.persistence.DataEntity;

/**
 * @ClassName: SSOConfig
 * @Description: 单点登录配置
 * @author com.mhout.xyb
 * @date 2017年6月1日 下午2:12:25
 * @version 1.0.0
 */
public class SSOConfig extends DataEntity<SSOConfig> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name; //名称
	private String field; //字段名
	private String status; //状态 （1.可用 2.禁用）

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
