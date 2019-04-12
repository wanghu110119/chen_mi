package com.mht.modules.audit.entity;

import com.mht.common.persistence.DataEntity;

/**
 * @ClassName: AppUserRecordData
 * @Description: 数据组装
 * @author com.mhout.xyb
 * @date 2017年4月26日 上午10:37:25
 * @version 1.0.0
 */
public class AppUserRecordData extends DataEntity<AppUserRecordData> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private Integer value;
	private String percent;
	private Integer yvalue;
	private Integer wvalue; //本周
	private Integer mvalue;	//本月
	private Integer yeavalue; //本年
	private Integer yallvalue; //全部
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public String getPercent() {
		return percent;
	}

	public void setPercent(String percent) {
		this.percent = percent;
	}

	public Integer getYvalue() {
		return yvalue;
	}

	public void setYvalue(Integer yvalue) {
		this.yvalue = yvalue;
	}

	public Integer getWvalue() {
		return wvalue;
	}

	public void setWvalue(Integer wvalue) {
		this.wvalue = wvalue;
	}

	public Integer getMvalue() {
		return mvalue;
	}

	public void setMvalue(Integer mvalue) {
		this.mvalue = mvalue;
	}

	public Integer getYeavalue() {
		return yeavalue;
	}

	public void setYeavalue(Integer yeavalue) {
		this.yeavalue = yeavalue;
	}

	public Integer getYallvalue() {
		return yallvalue;
	}

	public void setYallvalue(Integer yallvalue) {
		this.yallvalue = yallvalue;
	}
	
}
