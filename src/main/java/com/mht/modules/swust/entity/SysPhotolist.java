package com.mht.modules.swust.entity;

import java.util.Date;

import com.mht.common.persistence.DataEntity;

public class SysPhotolist extends DataEntity<SysPhotolist>{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;


    private String userId;

    private String photoPath;

    private String name;

    private String photoType;

    private Date createTime;

    private String state;

    private String remarks;

    private Date beginTime;

    private Date endTime;

    private Integer accreditTime;

    private Integer messageTotal;

    private Integer messageCode;
    
    

    @Override
	public String toString() {
		return "SysPhotolist [id=" + id + ", userId=" + userId + ", photoPath=" + photoPath + ", name=" + name
				+ ", photoType=" + photoType + ", createTime=" + createTime + ", state=" + state + ", remarks="
				+ remarks + ", beginTime=" + beginTime + ", endTime=" + endTime + ", accreditTime=" + accreditTime
				+ ", messageTotal=" + messageTotal + ", messageCode=" + messageCode + "]";
	}


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath == null ? null : photoPath.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getPhotoType() {
        return photoType;
    }

    public void setPhotoType(String photoType) {
        this.photoType = photoType == null ? null : photoType.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state == null ? null : state.trim();
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks == null ? null : remarks.trim();
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

    public Integer getAccreditTime() {
        return accreditTime;
    }

    public void setAccreditTime(Integer accreditTime) {
        this.accreditTime = accreditTime;
    }

    public Integer getMessageTotal() {
        return messageTotal;
    }

    public void setMessageTotal(Integer messageTotal) {
        this.messageTotal = messageTotal;
    }

    public Integer getMessageCode() {
        return messageCode;
    }

    public void setMessageCode(Integer messageCode) {
        this.messageCode = messageCode;
    }
}