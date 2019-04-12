package com.mht.modules.swust.utils;

import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import org.junit.Test;

import com.mht.common.utils.SpringContextHolder;
import com.mht.modules.swust.dao.SysBackupListDao;
import com.mht.modules.swust.entity.SysOrderlist;
import com.mht.modules.swust.service.impl.OrderUserServiceImpl;

public class Task2 extends TimerTask {
	
	private static  OrderUserServiceImpl service  = SpringContextHolder.getBean(OrderUserServiceImpl.class);
	private static  SysBackupListDao backupDao  = SpringContextHolder.getBean(SysBackupListDao.class);
	@Override
	@Test
	public void run() {
		// TODO Auto-generated method stub
//		List<SysOrderlist> list = service.selectRemoveOrder();
//		if(list.size()>0){
//			backupDao.batchInsert(list);
//			service.deleteRemoveOrder(list);
//		}
		System.out.println("========THREAD2 : SAFE & SUCCESS  "+new Date()+"========");
	}
	public static void main(String[] args) {
		List<SysOrderlist> list = service.selectRemoveOrder();
		backupDao.batchInsert(list);
		service.deleteRemoveOrder(list);
		System.out.println("========THREAD2 : SAFE & SUCCESS  "+new Date()+"========");
	}
	
	
	
	
	
	
	
	
}
