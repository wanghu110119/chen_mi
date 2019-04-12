package com.mht.modules.fieldconfig.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * 
* @ClassName: FieldRecord 
* @Description: 一条数据记录 
* @author 华强 
* @date 2017年4月6日 下午4:33:48 
*
 */
public class FieldRecord {
	
	/**
	 * 一条记录中包含的列数据
	 */
	private List<Column> columns = new ArrayList<Column>();

	public List<Column> getColumns() {
		return columns;
	}

	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}
	
	public void addColumns(Column column){
		columns.add(column);
	}
	
	
}
