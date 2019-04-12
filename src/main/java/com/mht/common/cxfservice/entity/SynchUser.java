package com.mht.common.cxfservice.entity;

/**
 * @ClassName: SynchUser
 * @Description: 用户同步实体类
 * @author com.mhout.xyb
 * @date 2017年6月5日 上午10:15:03
 * @version 1.0.0
 */
public class SynchUser {

	private String username; // 用户名
	private String password; // 密码
	private String type; // 类型（1.用户名 2.手机号 3.身份证号 4.邮箱号 5.学生号 6.教工号 ）

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
