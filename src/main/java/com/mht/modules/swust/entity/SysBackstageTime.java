package com.mht.modules.swust.entity;

import java.util.Date;

import com.mht.common.persistence.DataEntity;

public class SysBackstageTime extends DataEntity<SysBackstageTime>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private String userId;//关联用户

private Date beginTime;//起始时间

private Date endTime;//结束时间

private String disable;

private String sum;


public String getSum() {
	return sum;
}

public void setSum(String sum) {
	this.sum = sum;
}

public String getDisable() {
	return disable;
}

public void setDisable(String disable) {
	this.disable = disable;
}

@Override
public String toString() {
	return "SysBackstageTime [userId=" + userId + ", beginTime=" + beginTime + ", endTime=" + endTime + "]";
}

public String getUserId() {
	return userId;
}

public void setUserId(String userId) {
	this.userId = userId;
}

public Date getBeginTime() {
	return beginTime;
}

public void setBeginTime(Date beginTime) {
	this.beginTime = beginTime;
}

public Date getEndTime() {
	return endTime;
}

public void setEndTime(Date endTime) {
	this.endTime = endTime;
}


}
