package com.mht.modules.audit.entity;

import java.util.List;

/**
 * @ClassName: AppUserRecordList
 * @Description: 统计显示数据
 * @author com.mhout.xyb
 * @date 2017年4月26日 上午10:49:53
 * @version 1.0.0
 */
public class AppUserRecordList {

	private List<Integer> count;
	private List<String> value;
	private List<AppUserRecordData> list;

	public List<Integer> getCount() {
		return count;
	}

	public void setCount(List<Integer> count) {
		this.count = count;
	}

	public List<String> getValue() {
		return value;
	}

	public void setValue(List<String> value) {
		this.value = value;
	}

	public List<AppUserRecordData> getList() {
		return list;
	}

	public void setList(List<AppUserRecordData> list) {
		this.list = list;
	}
	
	

}
