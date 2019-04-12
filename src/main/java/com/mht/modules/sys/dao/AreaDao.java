/**
 * Copyright &copy; 2015-2020 <a href="http://www.mht.org/">mht</a> All rights reserved.
 */
package com.mht.modules.sys.dao;

import com.mht.common.persistence.TreeDao;
import com.mht.common.persistence.annotation.MyBatisDao;
import com.mht.modules.sys.entity.Area;

/**
 * 区域DAO接口
 * @author mht
 * @version 2014-05-16
 */
@MyBatisDao
public interface AreaDao extends TreeDao<Area> {
	
}
