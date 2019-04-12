package com.mht.modules.swust.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.mht.common.utils.SpringContextHolder;
import com.mht.modules.swust.service.impl.OrderUserServiceImpl;


public class PictureEngine implements ServletContextListener{

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
        Calendar calendar = Calendar.getInstance();  
        calendar.set(Calendar.HOUR_OF_DAY, 0); //凌晨0点  
        calendar.set(Calendar.MINUTE, 0);  
        calendar.set(Calendar.SECOND, 0);  
        Date date=calendar.getTime(); //第一次执行定时任务的时间  
        //如果第一次执行定时任务的时间 小于当前的时间  
//        此时要在 第一次执行定时任务的时间加一天，以便此任务在下个时间点执行。如果不加一天，任务会立即执行。  
//        if (date.before(new Date())) {  
//            date = this.addDay(date, 1);  
//        }  
        Timer timer = new Timer();  
        Task task = new Task(); 
        Task2 task2 = new Task2();
        //安排指定的任务在指定的时间开始进行重复的固定延迟执行。
        timer.schedule(task2,date,12*60*60*1000); 
        timer.schedule(task,date,12*60*60*1000); 
	}
	// 增加或减少天数  
    public Date addDay(Date date, int num) {  
        Calendar startDT = Calendar.getInstance();  
        startDT.setTime(date);  
        startDT.add(Calendar.DAY_OF_MONTH, num);  
        return startDT.getTime();  
    }  
}
