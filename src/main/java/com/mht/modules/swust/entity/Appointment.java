package com.mht.modules.swust.entity;

import java.util.Date;

import com.mht.common.persistence.DataEntity;

/**
 * 
 * @ClassName: Appointment
 * @Description:
 * @author com.mhout.dhn
 * @date 2017年7月26日 下午2:42:47
 * @version 1.0.0
 */

public class Appointment extends DataEntity<Appointment> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
     * @ClassName: Appointment
     * @Description:预约号
     * @author com.mhout.dhn
     * @version 1.0.0
     */	
	// 车牌号
	private String numberPlates;
	// 预约人
	private String contactsName;
	// 联系电话
	private String telephoneNumber;
	// 车牌号码
	private String timelicensePlate;
	// 车辆类型
	private String vehicleType;
	// 开始时间
	private Date startTime;
    // 結束时间
    private Date endTime;
	// 授权时间
    private Integer privilegedTime;
    // 对接单位
    private String abutmentCompany;
    // 预约事由
    private String contactsThing;
	// 审核状态
	private Integer auditStatus;
	
	private String state;
		
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getNumberPlates() {
		return numberPlates;
	}
	@Override
	public String toString() {
		return "Appointment [id=" + id + ", numberPlates=" + numberPlates + ", contactsName=" + contactsName
				+ ", telephoneNumber=" + telephoneNumber + ", timelicensePlate=" + timelicensePlate + ", vehicleType="
				+ vehicleType + ", startTime=" + startTime + ", endTime=" + endTime + ", privilegedTime="
				+ privilegedTime + ", abutmentCompany=" + abutmentCompany + ", contactsThing=" + contactsThing
				+ ", auditStatus=" + auditStatus + "]";
	}
	public void setNumberPlates(String numberPlates) {
		this.numberPlates = numberPlates;
	}
	public String getContactsName() {
		return contactsName;
	}
	public void setContactsName(String contactsName) {
		this.contactsName = contactsName;
	}
	public String getTelephoneNumber() {
		return telephoneNumber;
	}
	public void setTelephoneNumber(String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}
	public String getTimelicensePlate() {
		return timelicensePlate;
	}
	public void setTimelicensePlate(String timelicensePlate) {
		this.timelicensePlate = timelicensePlate;
	}
	public String getVehicleType() {
		return vehicleType;
	}
	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Integer getPrivilegedTime() {
		return privilegedTime;
	}
	public void setPrivilegedTime(Integer privilegedTime) {
		this.privilegedTime = privilegedTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getAbutmentCompany() {
		return abutmentCompany;
	}
	public void setAbutmentCompany(String abutmentCompany) {
		this.abutmentCompany = abutmentCompany;
	}
	public String getContactsThing() {
		return contactsThing;
	}
	public void setContactsThing(String contactsThing) {
		this.contactsThing = contactsThing;
	}
	public Integer getAuditStatus() {
		return auditStatus;
	}
	public void setAuditStatus(Integer auditStatus) {
		this.auditStatus = auditStatus;
	}	
}
