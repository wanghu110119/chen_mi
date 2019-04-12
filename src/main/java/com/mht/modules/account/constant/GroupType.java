/**
 * 
 */
package com.mht.modules.account.constant;

import java.util.ArrayList;
import java.util.List;

import com.mht.modules.sys.entity.Dict;

/**
 * @ClassName: GroupType
 * @Description: 分组类型枚举 one 表示一个人只有一条数据， many表示可填写多条数据
 * @author 华强
 * @date 2017年5月25日 下午4:54:21 
 * @version 1.0.0
 */
public enum GroupType {
	ONE("单条数据"), MANY("多条数据");
	
	private String chineseName;
	
	GroupType(String chineseName){
		this.chineseName = chineseName;
	}
	
	/**
	 * 
	 * @Title: getGroupTypeDicts 
	 * @Description: 将枚举类型转换为Dict
	 * @return List<Dict>
	 * @author huaqiang
	 */
	public static List<Dict> getGroupTypeDicts(){
		List<Dict> groupTypeDicts = new ArrayList<Dict>();
		for(GroupType groupType : GroupType.values()){
			groupTypeDicts.add(new Dict(groupType.name(), groupType.getChineseName()));
		}
		return groupTypeDicts;
	}

	public String getChineseName() {
		return chineseName;
	}

	public void setChineseName(String chineseName) {
		this.chineseName = chineseName;
	}
}
