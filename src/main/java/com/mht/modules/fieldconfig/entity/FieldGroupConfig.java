package com.mht.modules.fieldconfig.entity;

/**
 * 
* @ClassName: FieldGroupConfig 
* @Description: group与fieldConfg中间表对象 
* @author 华强 
* @date 2017年4月1日 上午12:14:46 
*
 */
public class FieldGroupConfig {
	private String id; //id
	private String groupId; //分组id
	private String fieldId; //扩展字段id
	
	public FieldGroupConfig() {
	
	}
	public FieldGroupConfig(String id, String groupId, String fieldId) {
		super();
		this.id = id;
		this.groupId = groupId;
		this.fieldId = fieldId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getFieldId() {
		return fieldId;
	}
	public void setFieldId(String fieldId) {
		this.fieldId = fieldId;
	}
	
	
}
