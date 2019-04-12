/**
 * 
 */
package com.mht.modules.swust.entity;

import com.mht.common.persistence.DataEntity;

/**
 * @ClassName: Unit
 * @Description: 
 * @author com.mhout.dhn
 * @date 2017年7月26日 下午4:50:19 
 * @version 1.0.0
 */
public class Unit extends DataEntity<Unit>{
	//单位名称
	private String organization;
	//单位用户名
	private String organizationYIN;
	//联系人
	private String contacter;
	//联系电话
	private String telnumber;
	//是否可用
	private Integer status=0;
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getOrganization() {
		return organization;
	}
	public void setOrganization(String organization) {
		this.organization = organization;
	}
	public String getOrganizationYIN() {
		return organizationYIN;
	}
	public void setOrganizationYIN(String organizationYIN) {
		this.organizationYIN = organizationYIN;
	}
	public String getContacter() {
		return contacter;
	}
	public void setContacter(String contacter) {
		this.contacter = contacter;
	}
	public String getTelnumber() {
		return telnumber;
	}
	public void setTelnumber(String telnumber) {
		this.telnumber = telnumber;
	}
    
}
