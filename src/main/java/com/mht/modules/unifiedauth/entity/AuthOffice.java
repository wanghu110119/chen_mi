/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.mht.modules.unifiedauth.entity;

import com.mht.common.persistence.DataEntity;
import com.mht.modules.ident.entity.Application;
import com.mht.modules.sys.entity.Office;

/**
 * @ClassName: AuthOffice
 * @Description: 部门授权
 * @author com.mhout.sx
 * @date 2017年3月29日 下午3:00:26
 * @version 1.0.0
 */
public class AuthOffice extends DataEntity<AuthOffice> {

    private static final long serialVersionUID = 1L;
    private Office office;
    private Application apply;
    private String accessAuth;

    private String officeId;
    private String applyId;
    private String isEdit; //是否编辑1.是 2.不是
    private String closeType; //是否屏蔽 1.是 2.不是

    public Office getOffice() {
        return office;
    }

    public void setOffice(Office office) {
        this.office = office;
    }

    public String getAccessAuth() {
        return accessAuth;
    }

    public void setAccessAuth(String accessAuth) {
        this.accessAuth = accessAuth;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
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