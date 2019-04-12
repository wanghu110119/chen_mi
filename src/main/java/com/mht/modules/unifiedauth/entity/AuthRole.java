/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.mht.modules.unifiedauth.entity;

import com.mht.common.persistence.DataEntity;
import com.mht.common.utils.IdGen;
import com.mht.modules.ident.entity.Apply;
import com.mht.modules.sys.entity.Role;

/**
 * 
 * @ClassName: AuthRole
 * @Description: 角色授权
 * @author com.mhout.zjh
 * @date 2017年3月29日 下午3:21:40 
 * @version 1.0.0
 */
public class AuthRole extends DataEntity<AuthRole> {

    private static final long serialVersionUID = 1L;
    /**
     * 被授权的角色
     */
    private Role role;
    /**
     * 访问权限（0:无  1：有）
     */
    private String accessAuth;
    /**
     * 应用
     */
    private Apply apply;
    
    private String roleId;
    private String applyId;
    private String isEdit; //是否编辑1.是 2.不是
    private String closeType; //是否屏蔽 1.是 2.不是
    
    public AuthRole(){
    	super();
    	this.id = IdGen.uuid();
    }

	public Apply getApply() {
        return apply;
    }

    public void setApply(Apply apply) {
        this.apply = apply;
    }

    public String getAccessAuth() {
        return accessAuth;
    }

    public void setAccessAuth(String accessAuth) {
        this.accessAuth = accessAuth;
    }

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getApplyId() {
		return applyId;
	}

	public void setApplyId(String applyId) {
		this.applyId = applyId;
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