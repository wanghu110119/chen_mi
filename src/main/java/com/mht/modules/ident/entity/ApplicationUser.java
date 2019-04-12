package com.mht.modules.ident.entity;

import com.mht.common.persistence.DataEntity;
import com.mht.modules.sys.entity.User;

/**
 * @ClassName: ApplicationUser
 * @Description: 应用管理
 * @author com.mhout.xyb
 * @date 2017年3月31日 下午3:34:59 
 * @version 1.0.0
 */
public class ApplicationUser extends DataEntity<ApplicationUser> {
	
	/**
     * 
     */
    private static final long serialVersionUID = 1L;
    private User user;	//用户信息
    private Application application; //应用信息
    private String type; //类型 （1.管理员 2.成员）
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Application getApplication() {
		return application;
	}
	public void setApplication(Application application) {
		this.application = application;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
