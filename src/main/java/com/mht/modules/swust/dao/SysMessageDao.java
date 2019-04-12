package com.mht.modules.swust.dao;

import java.util.List;

import com.mht.common.persistence.CrudDao;
import com.mht.common.persistence.annotation.MyBatisDao;
import com.mht.modules.swust.entity.SysMessage;
@MyBatisDao
public interface SysMessageDao extends CrudDao<SysMessage>{
	
	List<SysMessage> selectAllMessage();
	
}
