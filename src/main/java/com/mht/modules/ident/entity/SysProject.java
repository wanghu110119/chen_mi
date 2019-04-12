package com.mht.modules.ident.entity;

import com.mht.common.persistence.DataEntity;

/**
 * @ClassName: SysProject
 * @Description: 项目管理
 * @author com.mhout.xyb
 * @date 2017年5月4日 上午10:27:56
 * @version 1.0.0
 */
public class SysProject extends DataEntity<SysProject> {

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;
	private String name; // 项目名称
	private String url; // url地址
	private String code; // 项目代码
	private String ip; // ip地址
	private String status; // 状态 (1、可用 2、禁用)

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
}
