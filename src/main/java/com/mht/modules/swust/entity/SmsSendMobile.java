package com.mht.modules.swust.entity;

import com.mht.common.persistence.DataEntity;

public class SmsSendMobile extends DataEntity<SmsSendMobile>{
		
    private String carType;

    private String mobile;

    private String id;

	public String getCarType() {
		return carType;
	}

	public void setCarType(String carType) {
		this.carType = carType;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


    

}