/**
 * Copyright &copy; 2015-2020 <a href="http://www.mht.org/">mht</a> All rights reserved.
 */
package com.mht.modules.fieldconfig.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.mht.common.persistence.CrudDao;
import com.mht.common.persistence.annotation.MyBatisDao;
import com.mht.modules.fieldconfig.entity.UserExtendInfo;
import com.mht.modules.fieldconfig.vo.FieldRecord;

/**
 * 用户扩展信息DAO接口
* @ClassName: UserExtendInfoDao 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author 华强 
* @date 2017年3月30日 下午3:07:43 
*
 */
@MyBatisDao
public interface UserExtendInfoDao extends CrudDao<UserExtendInfo> {

	/**
	 * 获取用户扩展表中所有列
	 * @param tableName
	 */
	public List<String> getColName();

	/**
	 * 往表中插入数据列
	 * @param colName
	 */
	public void insertColumn(@Param("colName")String colName);

	/**
	 * 将扩展字段数据保存在数据表中
	 * @param infoMap
	 */
	public void insertExtendInfo(@Param("fieldRecord")FieldRecord fieldRecord);

	/**
	 * 根据分组id和用户id获取用户对应得扩展数据
	 * @param groupId
	 * @param userId
	 * @return
	 */
	public List<Map<String, String>> getExendInfoByGroupIdAndUserId(@Param("groupId")String groupId, @Param("userId")String userId);

	/**
	 * 删除用户的所有扩展信息
	 * @param userId
	 */
	public void deleteExtendInfoByUserId(@Param("userId")String userId);

}