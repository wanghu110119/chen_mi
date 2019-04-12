package com.mht.modules.ident.entity;

import java.util.Date;

import com.mht.common.persistence.BaseEntity;
import com.mht.common.persistence.DataEntity;
import com.mht.common.utils.IdGen;
import com.mht.modules.sys.entity.User;
import com.mht.modules.sys.utils.UserUtils;
/**
 * 
 * @ClassName: IdentityGroupRecord
 * @Description: 
 * @author com.mhout.zjh
 * @date 2017年3月24日 下午4:39:38 
 * @version 1.0.0
 */
public class IdentityGroupUser extends DataEntity<IdentityGroupUser> {

	private static final long serialVersionUID = 1L;
	/**
	 * 身份组
	 */
	private IdentityGroup identityGroup;	
	/**
	 * 用户
	 */
	private User user;
	
	public IdentityGroupUser(){
		super();
	}
	
	public IdentityGroupUser(IdentityGroup identityGroup,User user,String action){
		this.id = IdGen.uuid();
		this.identityGroup = identityGroup;
		this.user = user;
		this.createBy = UserUtils.getUser();
		this.createDate = new Date();
		this.remarks = "";
		this.delFlag = BaseEntity.DEL_FLAG_NORMAL;
	}
	public IdentityGroupUser(IdentityGroup identityGroup,User user,String action,String remarks){
		this.id = IdGen.uuid();
		this.identityGroup = identityGroup;
		this.user = user;
		this.createBy = UserUtils.getUser();
		this.createDate = new Date();
		this.remarks = remarks;
		this.delFlag = BaseEntity.DEL_FLAG_NORMAL;
	}
	public IdentityGroup getIdentityGroup() {
		return identityGroup;
	}
	public void setIdentityGroup(IdentityGroup identityGroup) {
		this.identityGroup = identityGroup;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}

	public static final String ACTION_JOIN_GROUP = "0";
	public static final String ACTION_OUT_GROUP = "1";
}