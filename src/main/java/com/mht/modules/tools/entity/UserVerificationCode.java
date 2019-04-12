package com.mht.modules.tools.entity;

import java.util.Date;

import com.mht.common.persistence.DataEntity;
import com.mht.modules.sys.entity.User;

public class UserVerificationCode extends DataEntity<UserVerificationCode> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String verificationCode;

	private Date expireDate;// 过期时间

	private User user;

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
