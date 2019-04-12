package com.mht.modules.fieldconfig.entity;

import com.mht.common.persistence.DataEntity;

public class UserExtendInfo extends DataEntity<UserExtendInfo> {

	private static final long serialVersionUID = 1L;
	
	private String id;
	private String extendInfo;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getExtendInfo() {
		return extendInfo;
	}
	public void setExtendInfo(String extendInfo) {
		this.extendInfo = extendInfo;
	}
	
	public UserExtendInfo() {
		
	}
	public UserExtendInfo(String id, String extendInfo) {
		super();
		this.id = id;
		this.extendInfo = extendInfo;
	}
	
	
	
}
