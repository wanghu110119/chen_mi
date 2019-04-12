/**
 * Copyright &copy; 2015-2020 <a href="http://www.mht.org/">mht</a> All rights reserved.
 */
package com.mht.modules.echarts.dao;

import com.mht.common.persistence.CrudDao;
import com.mht.common.persistence.annotation.MyBatisDao;
import com.mht.modules.echarts.entity.PieClass;

/**
 * 班级DAO接口
 * @author lgf
 * @version 2016-05-26
 */
@MyBatisDao
public interface PieClassDao extends CrudDao<PieClass> {

	
}