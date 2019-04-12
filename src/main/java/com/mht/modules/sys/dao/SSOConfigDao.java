package com.mht.modules.sys.dao;

import com.mht.common.persistence.CrudDao;
import com.mht.common.persistence.annotation.MyBatisDao;
import com.mht.modules.sys.entity.SSOConfig;

/**
 * @ClassName: SSOConfigDao
 * @Description: 单点登录持久层
 * @author com.mhout.xyb
 * @date 2017年6月1日 下午2:21:05 
 * @version 1.0.0
 */
@MyBatisDao
public interface SSOConfigDao extends CrudDao<SSOConfig> {
	
}
