package com.mht.modules.ident.entity;

import com.mht.common.persistence.DataEntity;
import com.mht.modules.sys.entity.User;

/**
 * @ClassName: Apply
 * @Description: 应用管理
 * @author com.mhout.xyb
 * @date 2017年3月23日 上午11:14:12 
 * @version 1.0.0
 */
public class Apply extends DataEntity<Apply>{
	
	private static final long serialVersionUID = 1L;
	private User user; //应用账号
	private String name; //应用名称
	private String url; //应用地址
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
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
	
	
}
