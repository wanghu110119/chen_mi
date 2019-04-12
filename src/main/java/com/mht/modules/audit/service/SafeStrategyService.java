package com.mht.modules.audit.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mht.common.service.CrudService;
import com.mht.modules.audit.dao.SafeStrategyDao;
import com.mht.modules.audit.entity.SafeStrategy;
import com.mht.modules.sys.entity.Dict;
import com.mht.modules.sys.utils.UserUtils;

/**
 * @ClassName: SafeStrategyService
 * @Description: 安全策略业务层
 * @author com.mhout.xyb
 * @date 2017年4月20日 下午1:39:44 
 * @version 1.0.0
 */
@Service
@Transactional(readOnly = true)
public class SafeStrategyService extends CrudService<SafeStrategyDao, SafeStrategy>{
	
	@Autowired
	private SafeStrategyDao safeStrategyDao;

	/**
	 * @Title: findList 
	 * @Description: 安全策略配置列表
	 * @param safeStrategy
	 * @return
	 * @author com.mhout.xyb
	 */
	public List<SafeStrategy> findList(SafeStrategy safeStrategy) {
		return safeStrategyDao.findList(safeStrategy);
	}
	
	/**
	 * @Title: findTypeList 
	 * @Description: 安全策略配置类型
	 * @param safeStrategy
	 * @return
	 * @author com.mhout.xyb
	 */
	public List<SafeStrategy> findTypeList(SafeStrategy safeStrategy) {
		return safeStrategyDao.findTypeList(safeStrategy);
	}
	
	/**
	 * @Title: saveSafe 
	 * @Description: 保存配置
	 * @param safeStrategy
	 * @return
	 * @author com.mhout.xyb
	 */
	@Transactional(readOnly = false)
	public boolean saveSafe(List<SafeStrategy> list) {
		if (CollectionUtils.isNotEmpty(list)) {
			for (SafeStrategy safeStrategy : list) {
				SafeStrategy safe = safeStrategyDao.get(safeStrategy.getId());
				if (safe != null) {
					safe.setCreateBy(UserUtils.getUser());
					safe.setCreateDate(new Date());
					safe.setUpdateBy(UserUtils.getUser());
					safe.setUpdateDate(new Date());
					safe.setValue(safeStrategy.getValue());
					safeStrategyDao.update(safe);
				}
			}
			return false;
		}
		return true;
	}
	
	/**
	 * @Title: defaultValue 
	 * @Description: 恢复默认值
	 * @param id
	 * @return
	 * @author com.mhout.xyb
	 */
	@Transactional(readOnly = false)
	public boolean defaultValue(String id) {
		if (StringUtils.isNotBlank(id)) {
			SafeStrategy safeStrategy = new SafeStrategy();
			safeStrategy.setDict(new Dict(id));
			List<SafeStrategy> list = safeStrategyDao.findList(safeStrategy);
			if (CollectionUtils.isNotEmpty(list)) {
				for (SafeStrategy resafe : list) {
					resafe.setCreateBy(UserUtils.getUser());
					resafe.setCreateDate(new Date());
					resafe.setUpdateBy(UserUtils.getUser());
					resafe.setUpdateDate(new Date());
					resafe.setValue(resafe.getDefaultValue());
					safeStrategyDao.update(resafe);
				}
				return true;
			}
		}
		return false;
	}
}
