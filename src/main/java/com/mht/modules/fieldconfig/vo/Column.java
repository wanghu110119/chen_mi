package com.mht.modules.fieldconfig.vo;

/**
 * 
* @ClassName: Column 
* @Description: 数据列vo，用于保存列名和对应得值
* @author 华强 
* @date 2017年4月6日 下午4:30:34 
*
 */
public class Column {
	/**
	 * 列名
	 */
	private String colName;
	/**
	 * 数据值
	 */
	private String value;
	
	public Column() {
		super();
	}
	
	public Column(String colName, String value) {
		super();
		this.colName = colName;
		this.value = value;
	}
	public String getColName() {
		return colName;
	}
	public void setColName(String colName) {
		this.colName = colName;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	} 
	
	
	
}
