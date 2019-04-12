package com.mht.modules.ident.entity;

import java.util.Date;

import com.mht.common.persistence.DataEntity;
import com.mht.modules.sys.entity.Office;
import com.mht.modules.sys.entity.User;

/**
 * @ClassName: AppUserRecord
 * @Description: 应用访问记录
 * @author com.mhout.xyb
 * @date 2017年4月25日 下午1:34:14
 * @version 1.0.0
 */
public class AppUserRecord extends DataEntity<AppUserRecord> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private User user; // 用户信息
	private Application application; // 应用信息
	private Office office; //院系
	

	private Date beginDate; // 开始日期
	private Date endDate; // 结束日期

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
	
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
}
