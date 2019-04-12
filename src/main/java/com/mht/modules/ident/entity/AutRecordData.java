package com.mht.modules.ident.entity;

import com.mht.common.persistence.DataEntity;

public class AutRecordData extends DataEntity<AutRecordData>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String time;
	private int autCount;
	
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public int getAutCount() {
		return autCount;
	}
	public void setAutCount(int autCount) {
		this.autCount = autCount;
	}
	
	
}
