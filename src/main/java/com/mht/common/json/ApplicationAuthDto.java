package com.mht.common.json;

/**
 * @ClassName: ApplicationAuthDto
 * @Description: 应用成员
 * @author com.mhout.xyb
 * @date 2017年3月31日 下午10:34:46 
 * @version 1.0.0
 */
public class ApplicationAuthDto {
	
	private String id;
	private String name;
	private String type; //类型 1.用户 2.用户组 3.角色 4.组织 
	private int count;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
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
		this.type = type;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	

}
