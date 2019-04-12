package com.mht.modules.swust.dao;

import java.util.List;

import com.mht.common.persistence.CrudDao;
import com.mht.common.persistence.annotation.MyBatisDao;
import com.mht.modules.swust.entity.SysCarMoney;

@MyBatisDao
public interface SysCarMoneyDao extends CrudDao<SysCarMoney>{
	SysCarMoney selectByCarTypeId (String carTypeId);
	
    int insert(SysCarMoney record);

    SysCarMoney selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(SysCarMoney record);

	List<SysCarMoney> selectAll();
}