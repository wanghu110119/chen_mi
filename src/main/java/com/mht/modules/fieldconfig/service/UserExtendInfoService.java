/**
 * Copyright &copy; 2015-2020 <a href="http://www.mht.org/">mht</a> All rights reserved.
 */
package com.mht.modules.fieldconfig.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mht.common.service.CrudService;
import com.mht.common.utils.IdGen;
import com.mht.common.utils.StringUtils;
import com.mht.modules.account.constant.GroupRole;
import com.mht.modules.fieldconfig.dao.UserExtendInfoDao;
import com.mht.modules.fieldconfig.entity.FieldConfig;
import com.mht.modules.fieldconfig.entity.FieldGroup;
import com.mht.modules.fieldconfig.entity.UserExtendInfo;
import com.mht.modules.fieldconfig.vo.Column;
import com.mht.modules.fieldconfig.vo.FieldRecord;

/**
 * 代码生成模块功能测试Service
 * @author 张继平
 * @version 2017-03-29
 */
@Service
@Transactional(readOnly = true)
public class UserExtendInfoService extends CrudService<UserExtendInfoDao, UserExtendInfo> {

	@Autowired
	private UserExtendInfoDao userExtendInfoDao;
	
	@Autowired
	private FieldConfigService fieldConfigService;
	
	@Autowired
	private FieldGroupService fieldGroupService;
	
	/**
	 * 保存用户的扩展信息
	 * @param parameters 需要的数据信息的key为 groupName.colName.index
	 * @param groupRole 用户角色类型
	 * @param userId 用户id
	 */
	@Transactional(readOnly =  false)
	public void saveUserExtendInfo(Map<String, String[]> parameters, GroupRole groupRole,String userId) {
	
		//解析用户保存的参数，并封装为记录对象集合
		List<FieldRecord> fieldRecords =  getFieldRecords(parameters, groupRole, userId);
		
		//删除用户之前保存的扩展数据
		userExtendInfoDao.deleteExtendInfoByUserId(userId);
		//保存用户数据
		if(CollectionUtils.isNotEmpty(fieldRecords)){
			for(FieldRecord fieldRecord : fieldRecords){
				userExtendInfoDao.insertExtendInfo(fieldRecord);
			}
		}
	}

	/**
	 * 解析用户保存的参数，并封装为记录对象集合
	 * @param parameters 数据信息
	 * @param groupRole 角色类型
	 * @param userId 用户id
	 * @return
	 */
	private List<FieldRecord> getFieldRecords(Map<String, String[]> parameters, GroupRole groupRole, String userId){
		
		List<FieldRecord> fieldRecords = new ArrayList<FieldRecord>();
		List<FieldGroup> groups = fieldGroupService.findFieldGroupByGroupRole(groupRole);
		
		if(CollectionUtils.isNotEmpty(groups)){
			for(FieldGroup group : groups){
				//解析一个分组中的记录数
				setOneFieldRecords(group, parameters, userId, fieldRecords );
			}
		}
		return fieldRecords;
	}
	
	/**
	 * 解析一个分组中的记录
	 * @param group 分组信息
	 * @param parameters 数据信息
	 * @param userId 用户id
	 * @param fieldRecords 一个分组中的字段
	 */
	private void setOneFieldRecords(FieldGroup group, Map<String, String[]> parameters, String userId, List<FieldRecord> fieldRecords){
		List<FieldConfig> configs = fieldConfigService.getFieldConfigByGroup(group);
		//数据记录map。使用map的原因是为了方便解析一对多型分组中的多条数据  key:记录下标（参数中下标），value：对应记录
		Map<Integer,FieldRecord> fieldRecordMap = new HashMap<Integer,FieldRecord>(); 
		if(CollectionUtils.isNotEmpty(configs)){
			for(FieldConfig config : configs){
				//解析数据，并组装为记录对象
				parseParameter(fieldRecordMap, group.getGroupName(), config, parameters);
			}
			//将map转化成list。并在每条记录中添加id,group_id和user_id
			setRequiredColumn(fieldRecords, fieldRecordMap, group.getId(), userId);
		}
	}
	/**
	 * 解析数据，并组装为记录对象
	 * @param fieldRecordMap 一个分组数据记录map
	 * @param groupName 分组名称
	 * @param config 字段属性
	 * @param parameters 数据信息
	 */
	private void parseParameter(Map<Integer,FieldRecord> fieldRecordMap,String groupName, FieldConfig config, Map<String, String[]> parameters){
		for(Entry<String,String[]> entry : parameters.entrySet()){
			String paramKey = entry.getKey();
			String[] paramKeys = paramKey.split("\\.");
			if(paramKeys.length == 3 && paramKeys[0].equals(groupName) && paramKeys[1].equals(config.getColName())){
				String[] values =  entry.getValue();
				if(values != null){
					Integer index = Integer.valueOf(paramKeys[2]);
					FieldRecord fieldRecord = fieldRecordMap.get(index);
					if(fieldRecord == null){
						fieldRecord = new FieldRecord();
					}
					//如果该字段对应了多个值，则使用";"隔开（主要针对checkBox）
					Column column = new Column(config.getColName(), StringUtils.join(values, ";"));
					fieldRecord.addColumns(column);
					fieldRecordMap.put(index, fieldRecord);
				}
			}
		}
	}
	
	/**
	 * 将map转化成list。并在每条记录中添加id,group_id和user_id
	 * @param fieldRecords 数据记录List，保存每条记录信息
	 * @param fieldRecordMap 一个分组的记录map
	 * @param groupId 分组id
	 * @param userId 用户id
	 */
	private void setRequiredColumn(List<FieldRecord> fieldRecords, Map<Integer, FieldRecord> fieldRecordMap,
			String groupId, String userId) {
		if(!fieldRecordMap.isEmpty()){
			for(Entry<Integer, FieldRecord> entry : fieldRecordMap.entrySet()){
				FieldRecord fieldRecord = entry.getValue();
				fieldRecord.addColumns(new Column("id", IdGen.uuid()));
				fieldRecord.addColumns(new Column("group_id", groupId));
				fieldRecord.addColumns(new Column("user_id", userId));
				fieldRecords.add(fieldRecord);
			}
		}
		
	}
	
	/**
	 * 根据分组id和用户id获取用户对应得扩展数据
	 * @param groupId
	 * @param userId
	 * @return
	 */
	public List<Map<String, String>> getExendInfoByGroupIdAndUserId(String groupId, String userId) {		
		return userExtendInfoDao.getExendInfoByGroupIdAndUserId(groupId,userId);
	}

	
}