package com.mht.modules.swust.service;

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mht.common.service.CrudService;
import com.mht.modules.swust.dao.BackstageTimeDao;
import com.mht.modules.swust.entity.SysBackstageTime;
import com.mht.modules.sys.entity.User;
import com.mht.modules.sys.utils.UserUtils;
@Service
public class BackstageTimeService  extends CrudService<BackstageTimeDao, SysBackstageTime>{

	@Autowired
	BackstageTimeDao timeDao;
	
	public void setPrimeryentity() {
		User user = UserUtils.getUser();
		SysBackstageTime sysBackstageTime = new SysBackstageTime();
		sysBackstageTime.setId("1");
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DAY_OF_YEAR, 1);
		cal.set(Calendar.HOUR_OF_DAY, 7);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		sysBackstageTime.setBeginTime(cal.getTime());
		cal = Calendar.getInstance();
		cal.setTime(sysBackstageTime.getBeginTime());
		cal.add(Calendar.DAY_OF_YEAR, 1);
		cal.set(Calendar.HOUR_OF_DAY, 10);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		sysBackstageTime.setEndTime(cal.getTime());
		sysBackstageTime.setUserId(user.getId());
		timeDao.insert(sysBackstageTime);
	}
	@Transactional(readOnly=false)
	public void notEqualsId(String id) {
		// TODO Auto-generated method stub
		timeDao.notEqualsId(id);
	}

}









