package com.mht.modules.sys.webservice;

import java.util.List;

/**
 * @ClassName: UserVo
 * @Description: 数据同步
 * @author com.mhout.xyb
 * @date 2017年6月5日 上午10:16:37
 * @version 1.0.0
 */
public class UserVo {

	private String appid; // 应用id
	private String secret; // 授权码
	private String type; // 操作类型（1.新增、2.修改、3.删除）
	private List<SynchUser> users; //用户信息

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<SynchUser> getUsers() {
		return users;
	}

	public void setUsers(List<SynchUser> users) {
		this.users = users;
	}

}
