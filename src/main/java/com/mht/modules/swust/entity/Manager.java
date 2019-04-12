/**
 * 
 */
package com.mht.modules.swust.entity;

import com.mht.common.persistence.DataEntity;

/**
 * @ClassName: Manager
 * @Description: 
 * @author com.mhout.dhn
 * @date 2017年7月26日 下午4:13:06 
 * @version 1.0.0
 */
public class Manager  extends DataEntity<Manager>{
	//用戶名	
	private String username;
	//密碼
	private String password;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
