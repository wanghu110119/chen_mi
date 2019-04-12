package com.mht.modules.audit.entity;

import java.util.List;

public class RealTimeData {
	
	private List<String> section;
	
	private List<String> value;
	
	private List<RealTime> list;
	
	private List<List<Object>> timer;


	public List<String> getValue() {
		return value;
	}

	public void setValue(List<String> value) {
		this.value = value;
	}

	public List<RealTime> getList() {
		return list;
	}

	public void setList(List<RealTime> list) {
		this.list = list;
	}

	public List<String> getSection() {
		return section;
	}

	public void setSection(List<String> section) {
		this.section = section;
	}

	public List<List<Object>> getTimer() {
		return timer;
	}

	public void setTimer(List<List<Object>> timer) {
		this.timer = timer;
	}

}
