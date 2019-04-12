package com.mht.modules.ident.entity;

import java.util.Date;

import com.mht.common.persistence.DataEntity;
import com.mht.modules.sys.entity.User;

/**
 * @ClassName: AutRecord
 * @Description: 认证记录
 * @author com.mhout.xyb
 * @date 2017年3月24日 上午9:37:05 
 * @version 1.0.0
 */
public class AutRecord extends DataEntity<AutRecord>{

	private static final long serialVersionUID = 1L;
	private User user;	//认证账号
	private String ip;	//认证Ip
	private String isSuccess;	//认证是否成功 （1. 成功 2. 失败）
	
	public static final String SUCCESS="1";
	public static final String FAILURE="2";
			
	
	/**查询参数*/
	private String type;
	private Date successDate;
	private Date faultDate;
	private String cnSuccessDate;
	private String cnFaultDate;
	private Date dayDate;
	private Date cnDate;
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getIsSuccess() {
		return isSuccess;
	}
	public void setIsSuccess(String isSuccess) {
		this.isSuccess = isSuccess;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Date getSuccessDate() {
		return successDate;
	}
	public void setSuccessDate(Date successDate) {
		this.successDate = successDate;
	}
	public Date getFaultDate() {
		return faultDate;
	}
	public void setFaultDate(Date faultDate) {
		this.faultDate = faultDate;
	}
	public String getCnSuccessDate() {
		return cnSuccessDate;
	}
	public void setCnSuccessDate(String cnSuccessDate) {
		this.cnSuccessDate = cnSuccessDate;
	}
	public String getCnFaultDate() {
		return cnFaultDate;
	}
	public void setCnFaultDate(String cnFaultDate) {
		this.cnFaultDate = cnFaultDate;
	}
	public Date getDayDate() {
		return dayDate;
	}
	public void setDayDate(Date dayDate) {
		this.dayDate = dayDate;
	}
	public Date getCnDate() {
		return cnDate;
	}
	public void setCnDate(Date cnDate) {
		this.cnDate = cnDate;
	}
	
}
