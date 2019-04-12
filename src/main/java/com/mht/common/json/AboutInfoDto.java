package com.mht.common.json;

public class AboutInfoDto {
	
	private String version;//版本信息
	
	private String updateDescription;//更新说明
	
	private String disclaimer;//免责声明

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getUpdateDescription() {
		return updateDescription;
	}

	public void setUpdateDescription(String updateDescription) {
		this.updateDescription = updateDescription;
	}

	public String getDisclaimer() {
		return disclaimer;
	}

	public void setDisclaimer(String disclaimer) {
		this.disclaimer = disclaimer;
	}
	
}
