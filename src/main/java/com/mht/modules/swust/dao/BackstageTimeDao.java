package com.mht.modules.swust.dao;

import com.mht.common.persistence.CrudDao;
import com.mht.common.persistence.annotation.MyBatisDao;
import com.mht.modules.swust.entity.SysBackstageTime;
@MyBatisDao
public interface BackstageTimeDao  extends CrudDao<SysBackstageTime>{

	SysBackstageTime selectBackstageTime();

	SysBackstageTime selectByRemark(String i);

	void notEqualsId(String id);

	SysBackstageTime selectByDisable();

}
