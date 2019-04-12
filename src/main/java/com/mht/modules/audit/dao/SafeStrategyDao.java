package com.mht.modules.audit.dao;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.mht.common.persistence.CrudDao;
import com.mht.common.persistence.annotation.MyBatisDao;
import com.mht.modules.audit.entity.SafeStrategy;

/**
 * @ClassName: SafeStrategyDao
 * @Description: 安全策略持久层
 * @author com.mhout.xyb
 * @date 2017年4月20日 下午1:41:01 
 * @version 1.0.0
 */
@MyBatisDao
public interface SafeStrategyDao extends CrudDao<SafeStrategy>{
	
	public List<SafeStrategy> findTypeList(SafeStrategy safeStrategy);
	
	@Select("SELECT value from audit_safe_strategy where `code` = #{0}")
	public SafeStrategy findByCode(String code);

}
