/**
 * Copyright &copy; 2015-2020 <a href="http://www.mht.org/">mht</a> All rights reserved.
 */
package com.mht.modules.fieldconfig.service;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mht.common.persistence.Page;
import com.mht.common.service.CrudService;
import com.mht.common.utils.IdGen;
import com.mht.modules.account.constant.GroupRole;
import com.mht.modules.fieldconfig.dao.FieldGroupDao;
import com.mht.modules.fieldconfig.entity.FieldConfig;
import com.mht.modules.fieldconfig.entity.FieldGroup;
import com.mht.modules.fieldconfig.entity.FieldGroupConfig;


/**
 * 代码生成模块功能测试Service
 * @author 张继平
 * @version 2017-03-31
 */
@Service
@Transactional(readOnly = true)
public class FieldGroupService extends CrudService<FieldGroupDao, FieldGroup> {

	@Autowired
	private FieldGroupDao fieldGroupDao;
	@Autowired
	private FieldConfigService fieldConfigService;
	
	public FieldGroup get(String id) {
		return super.get(id);
	}
	
	public List<FieldGroup> findList(FieldGroup fieldGroup) {
		return super.findList(fieldGroup);
	}
	
	public Page<FieldGroup> findPage(Page<FieldGroup> page, FieldGroup fieldGroup) {
		return super.findPage(page, fieldGroup);
	}
	
	
	//保存分组数据
	@Transactional(readOnly = false)
	public void save(FieldGroup fieldGroup) {
		List<FieldConfig> configs = fieldGroup.getConfigs();
		super.save(fieldGroup);
		fieldGroupDao.deleteGroupConfigByGroup(fieldGroup);
		if(configs != null){
			for(FieldConfig config : configs){
				FieldGroupConfig groupConfig = new FieldGroupConfig(IdGen.uuid(),fieldGroup.getId(),config.getId());
				fieldGroupDao.insertGroupConfig(groupConfig);
			}
		}
		
	}
	
	@Transactional(readOnly = false)
	public void delete(FieldGroup fieldGroup) {
		super.delete(fieldGroup);
	}

	
	/**
	 * 根据分组角色获取所有该角色下的字段分组
	 * @param groupRole
	 * @return
	 */
	public List<FieldGroup> findFieldGroupByGroupRole(GroupRole groupRole) {
		List<FieldGroup> groups = fieldGroupDao.findFieldGroupByGroupRole(groupRole.name());
		if(CollectionUtils.isNotEmpty(groups)){
			for(FieldGroup group : groups){
				List<FieldConfig> configs = fieldConfigService.getFieldConfigByGroup(group);
				group.setConfigs(configs);
			}
		}
		return groups;
	}

	/**
	 * 根据分组名称获取对应得分组
	 * @param groupName
	 * @return
	 */
	public Object getFieldGroupByGroupName(String groupName) {
		FieldGroup group = new FieldGroup();
		group.setGroupName(groupName);
		return fieldGroupDao.getFieldGroupByGroupName(group);
	}
	
	
	
	
}