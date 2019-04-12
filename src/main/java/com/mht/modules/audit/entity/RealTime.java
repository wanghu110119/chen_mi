package com.mht.modules.audit.entity;

import java.util.List;

public class RealTime {

	private String name;
	private String type="line";
	private Boolean smooth=true;
	private List<String> data;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		type = "line";
		this.type = type;
	}

	public Boolean getSmooth() {
		return smooth;
	}

	public void setSmooth(Boolean smooth) {
		smooth = true;
		this.smooth = smooth;
	}

	public List<String> getData() {
		return data;
	}

	public void setData(List<String> data) {
		this.data = data;
	}
	
}
