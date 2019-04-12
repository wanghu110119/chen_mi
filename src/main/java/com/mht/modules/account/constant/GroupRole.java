/**
 * 
 */
package com.mht.modules.account.constant;

import java.util.ArrayList;
import java.util.List;

import com.mht.modules.sys.entity.Dict;

/**
 * @ClassName: GroupRoleType
 * @Description: 扩展字段分组角色枚举
 * @author 华强
 * @date 2017年5月25日 下午4:47:05 
 * @version 1.0.0
 */
public enum GroupRole {
	STUDENT("学生"), TEACHER("老师") , PARENTS("家长");
	
	
	private String chineseName;
	
	GroupRole(String chineseName){
		this.chineseName = chineseName;
	}
	
	/**
	 * 
	 * @Title: getGroupRoleDicts 
	 * @Description: 将枚举类型转换为Dict
	 * @return List<Dict>
	 * @author huaqiang
	 */
	public static List<Dict> getGroupRoleDicts(){
		List<Dict> groupRoleDicts = new ArrayList<Dict>();
		for(GroupRole groupRole : GroupRole.values()){
			groupRoleDicts.add(new Dict(groupRole.name(), groupRole.getChineseName()));
		}
		return groupRoleDicts;
	}

	public String getChineseName() {
		return chineseName;
	}

	public void setChineseName(String chineseName) {
		this.chineseName = chineseName;
	}
	
	
	
}
