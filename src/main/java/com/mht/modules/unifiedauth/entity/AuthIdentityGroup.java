/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.mht.modules.unifiedauth.entity;

import com.mht.common.persistence.DataEntity;
import com.mht.common.utils.IdGen;
import com.mht.modules.ident.entity.Apply;
import com.mht.modules.ident.entity.IdentityGroup;

/**
 * 
 * @ClassName: AuthIdentityGroup
 * @Description: 用户组授权
 * @author com.mhout.zjh
 * @date 2017年3月31日 上午8:57:24 
 * @version 1.0.0
 */
public class AuthIdentityGroup extends DataEntity<AuthIdentityGroup> {

    private static final long serialVersionUID = 1L;
    /**
     * 被授权的用户组
     */
    private IdentityGroup identityGroup;
    /**
     * 访问权限（0:无  1：有）
     */
    private String accessAuth;
    /**
     * 应用
     */
    private Apply apply;
    
    private String identityGroupId;
    private String applyId;
    
    public AuthIdentityGroup(){
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

	public String getApplyId() {
		return applyId;
	}

	public void setApplyId(String applyId) {
		this.applyId = applyId;
	}

	public IdentityGroup getIdentityGroup() {
		return identityGroup;
	}

	public void setIdentityGroup(IdentityGroup identityGroup) {
		this.identityGroup = identityGroup;
	}

	public String getIdentityGroupId() {
		return identityGroupId;
	}

	public void setIdentityGroupId(String identityGroupId) {
		this.identityGroupId = identityGroupId;
	}
}