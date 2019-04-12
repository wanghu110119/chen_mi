/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.mht.modules.unifiedauth.entity;

import com.mht.common.persistence.DataEntity;
import com.mht.modules.ident.entity.Application;
import com.mht.modules.sys.entity.User;

/**
 * @ClassName: AuthUser
 * @Description: 用户授权应用权限实体
 * @author com.mhout.sx
 * @date 2017年3月30日 下午2:24:21
 * @version 1.0.0
 */
public class AuthUser extends DataEntity<AuthUser> {

    private static final long serialVersionUID = 1L;
    private User user;
    private Application apply;
    private String accessAuth;

    private String userId;
    private String applyId;
    private String isEdit; //是否编辑1.是 2.不是
    private String closeType; //是否屏蔽 1.是 2.不是

    public String getAccessAuth() {
        return accessAuth;
    }

    public void setAccessAuth(String accessAuth) {
        this.accessAuth = accessAuth;
    }

    public String getApplyId() {
        return applyId;
    }

    public void setApplyId(String applyId) {
        this.applyId = applyId;
    }

    public Application getApply() {
        return apply;
    }

    public void setApply(Application apply) {
        this.apply = apply;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

	public String getIsEdit() {
		return isEdit;
	}

	public void setIsEdit(String isEdit) {
		this.isEdit = isEdit;
	}

	public String getCloseType() {
		return closeType;
	}

	public void setCloseType(String closeType) {
		this.closeType = closeType;
	}

}