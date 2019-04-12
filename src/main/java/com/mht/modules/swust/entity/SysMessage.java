package com.mht.modules.swust.entity;

import com.mht.common.persistence.DataEntity;

public class SysMessage extends DataEntity<SysMessage>{
private String id;
private String userId;
private String artical;
private String total;
private String used;
private String surplus;
@Override
public String toString() {
	return "SysMessage [id=" + id + ", userId=" + userId + ", artical=" + artical + ", total=" + total + ", used="
			+ used + ", surplus=" + surplus + "]";
}

public String getArtical() {
	return artical;
}

public void setArtical(String artical) {
	this.artical = artical;
}

public String getId() {
	return id;
}
public void setId(String id) {
	this.id = id;
}
public String getUserId() {
	return userId;
}
public void setUserId(String userId) {
	this.userId = userId;
}
public String getTotal() {
	return total;
}
public void setTotal(String total) {
	this.total = total;
}
public String getUsed() {
	return used;
}
public void setUsed(String used) {
	this.used = used;
}
public String getSurplus() {
	return surplus;
}
public void setSurplus(String surplus) {
	this.surplus = surplus;
}



}
