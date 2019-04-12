package com.mht.modules.swust.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mht.common.service.CrudService;
import com.mht.modules.swust.dao.SysCarMoneyDao;
import com.mht.modules.swust.entity.SysCarMoney;
import com.mht.modules.swust.entity.SysPhotolist;
@Service
public class CarMOneyService  extends CrudService<SysCarMoneyDao, SysCarMoney>{
	@Autowired
	SysCarMoneyDao moneyDao;
	public List<SysCarMoney> selectAll() {
		return moneyDao.selectAll();
		
	}

}
