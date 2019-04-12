package com.mht.modules.ident.entity;

import java.util.Date;

import com.mht.common.persistence.BaseEntity;
import com.mht.common.persistence.DataEntity;
import com.mht.common.utils.IdGen;
import com.mht.modules.sys.utils.UserUtils;

/**
 * 
 * @ClassName: IdentityGroupRecord
 * @Description:
 * @author com.mhout.zjh
 * @date 2017年3月24日 下午4:39:38
 * @version 1.0.0
 */
public class IdentityGroupRecord extends DataEntity<IdentityGroupRecord> {

    private static final long serialVersionUID = 1L;
    public static final String GROUP_IN = "0";
    public static final String GROUP_OUT = "1";
    /**
     * 身份组
     */
    private IdentityGroup identityGroup;
    /**
     * 用户
     */
    private DataEntity user;
    /**
     * 动作 0：进组 1：出组
     */
    private String action;
    /**
     * 操作IP
     */
    private String operationIp;

    public IdentityGroupRecord() {
        super();
    }

    public IdentityGroupRecord(IdentityGroup identityGroup, DataEntity user, String action) {
        this.id = IdGen.uuid();
        this.identityGroup = identityGroup;
        this.user = user;
        this.action = action;
        this.createBy = UserUtils.getUser();
        this.createDate = new Date();
        this.remarks = "";
        this.operationIp = "";
        this.delFlag = BaseEntity.DEL_FLAG_NORMAL;
    }

    public IdentityGroupRecord(IdentityGroup identityGroup, DataEntity user, String action, String remarks) {
        this.id = IdGen.uuid();
        this.identityGroup = identityGroup;
        this.user = user;
        this.action = action;
        this.createBy = UserUtils.getUser();
        this.createDate = new Date();
        this.remarks = remarks;
        this.operationIp = "";
        this.delFlag = BaseEntity.DEL_FLAG_NORMAL;
    }

    public IdentityGroup getIdentityGroup() {
        return identityGroup;
    }

    public void setIdentityGroup(IdentityGroup identityGroup) {
        this.identityGroup = identityGroup;
    }

    public DataEntity getUser() {
        return user;
    }

    public void setUser(DataEntity user) {
        this.user = user;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getOperationIp() {
        return operationIp;
    }

    public void setOperationIp(String operationIp) {
        this.operationIp = operationIp;
    }

    public static final String ACTION_JOIN_GROUP = "0";
    public static final String ACTION_OUT_GROUP = "1";
}