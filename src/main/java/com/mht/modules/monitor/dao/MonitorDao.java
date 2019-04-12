/**
 * Copyright &copy; 2015-2020 <a href="http://www.mht.org/">mht</a> All rights reserved.
 */
package com.mht.modules.monitor.dao;

import com.mht.common.persistence.CrudDao;
import com.mht.common.persistence.annotation.MyBatisDao;
import com.mht.modules.monitor.entity.Monitor;

/**
 * 系统监控DAO接口
 * @author liugf
 * @version 2016-02-07
 */
@MyBatisDao
public interface MonitorDao extends CrudDao<Monitor> {
	
}