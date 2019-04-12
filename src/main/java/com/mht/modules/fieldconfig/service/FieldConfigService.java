/**
 * Copyright &copy; 2015-2020 <a href="http://www.mht.org/">mht</a> All rights reserved.
 */
package com.mht.modules.fieldconfig.service;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mht.common.exception.BusinessException;
import com.mht.common.persistence.Page;
import com.mht.common.service.CrudService;
import com.mht.modules.fieldconfig.dao.FieldConfigDao;
import com.mht.modules.fieldconfig.dao.UserExtendInfoDao;
import com.mht.modules.fieldconfig.entity.FieldConfig;
import com.mht.modules.fieldconfig.entity.FieldGroup;

/**
 * 扩展字段配置Service
 * @author 华强
 * @version 2017-03-29
 */
@Service
@Transactional(readOnly = true)
public class FieldConfigService extends CrudService<FieldConfigDao, FieldConfig> {

	@Autowired
	private FieldConfigDao fieldConfigDao;
	
	@Autowired
	private UserExtendInfoDao userExtendInfoDao;
	
	public FieldConfig get(String id) {
		return super.get(id);
	}
	
	public List<FieldConfig> findList(FieldConfig fieldConfig) {
		return super.findList(fieldConfig);
	}
	
	public Page<FieldConfig> findPage(Page<FieldConfig> page, FieldConfig fieldConfig) {
		return super.findPage(page, fieldConfig);
	}
	
	
	public List<FieldConfig> findUsableFieldConfigList(FieldConfig fieldConfig) {
		return fieldConfigDao.findUsableFieldConfigList(fieldConfig);
	}
	
	
	@Transactional(readOnly = false)
	public void save(FieldConfig fieldConfig) {
		super.save(fieldConfig);
	}
	
	@Transactional(readOnly = false)
	public boolean deleteField(FieldConfig fieldConfig) {
		List<FieldConfig> list = fieldConfigDao.getGroupByFieldConfig(fieldConfig);
		if (CollectionUtils.isNotEmpty(list)) {
			return false;
		}
		fieldConfigDao.deleteByLogic(fieldConfig);
		return true;
		//super.delete(fieldConfig);
	}

	public List<FieldConfig> findListByFieldType(Integer fieldType) {
		return fieldConfigDao.findListByFieldType(fieldType);
	}

	public Object getFieldConfigByFieldName(String fieldName) {
		FieldConfig config = new FieldConfig();
		config.setFieldName(fieldName);
		return fieldConfigDao.getFieldConfigByFieldName(config);
	}

	public List<FieldConfig> getFieldConfigByGroup(FieldGroup fieldGroup) {
		return fieldConfigDao.getFieldConfigByGroup(fieldGroup);
	}
	
	/**
	 * 新增扩展字段
	 * @param fieldConfig
	 */
	@Transactional(readOnly = false)
	public void addConfig(FieldConfig fieldConfig) {
		//获取扩展信息表中所有的列名
		List<String> columns =  userExtendInfoDao.getColName();
		
		columns.remove("id");
		columns.remove("group_id");
		columns.remove("user_id");
		List<FieldConfig> configs = fieldConfigDao.findAllList(null);
		
		if (columns.size() > configs.size()){
	      String col = getFirstColumnOfUnUsed(columns, configs);
	      if (col != null){
	    	  fieldConfig.setColName(col);
	    	  super.save(fieldConfig);
	      }else{
	        throw new BusinessException("数据库数据异常，请联系管理员进行处理");
	      }
	    }else{
	      Integer maxSize = Integer.valueOf(columns.size() + 1);
	      String colName = "extend" + maxSize;
	      fieldConfig.setColName(colName);
	      try{
	    	  userExtendInfoDao.insertColumn(colName);
	    	  super.save(fieldConfig);
	      }catch (Exception e){
	    	e.printStackTrace();
	        throw new BusinessException("创建字段失败，请重试！");
	      }
	    }
	}

	private String getFirstColumnOfUnUsed(List<String> columns, List<FieldConfig> fieldConfigs)
	  {
	    for (String column : columns)
	    {
	      boolean isExist = false;
	      for (FieldConfig config : fieldConfigs) {
	        if (config.getColName().equalsIgnoreCase(column)) {
	          isExist = true;
	        }
	      }
	      if (!isExist) {
	        return column;
	      }
	    }
	    return null;
	  }
}