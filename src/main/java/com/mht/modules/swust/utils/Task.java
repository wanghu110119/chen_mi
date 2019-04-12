package com.mht.modules.swust.utils;

import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mht.common.enumutils.SwustReplyEnum;
import com.mht.common.utils.SpringContextHolder;
import com.mht.modules.swust.entity.SysCar;
import com.mht.modules.swust.service.SysCarService;
import com.mht.modules.sys.utils.UserUtils;


@Service
public class Task extends TimerTask {
	private static SysCarService carService = SpringContextHolder.getBean(SysCarService.class);
	
	@Transactional(readOnly = false)
    public void run() { 
    	 
		System.out.println("===========DAYILY AUDIT START--"+new Date()+"==========="); 
        Date nowDate = new Date();
        SysCar syscar = new SysCar();
        syscar.setCreateBy(UserUtils.getUser());
//        List<SysCar> list = carService.findList(syscar);
//        for (SysCar sysCar : list) {
//        	if((long)nowDate.getTime()>=(long)sysCar.getEndTime().getTime()){
//        		if(SwustReplyEnum.TIMEOUT.getParam().equals(sysCar.getDisable())){
//        			continue;
//        		}
//        		sysCar.setDisable(SwustReplyEnum.TIMEOUT.getParam());
//        		carService.save(sysCar);
//            }else if((long)nowDate.getTime()<(long)sysCar.getEndTime().getTime()&&(long)nowDate.getTime()>(long)sysCar.getBeginTime().getTime()){
//            	if(SwustReplyEnum.PREMIT.getParam().equals(sysCar.getDisable())){
//        			continue;
//        		}
//            	sysCar.setDisable(SwustReplyEnum.PREMIT.getParam());
//        		carService.save(sysCar);
//            }else{
//            	if(SwustReplyEnum.PAY.getParam().equals(sysCar.getDisable())){
//        			continue;
//        		}
//            	sysCar.setDisable(SwustReplyEnum.PAY.getParam());
//        		carService.save(sysCar);
//            }
//		}
        System.out.println("===========DAYILY AUDIT FINISH--"+new Date()+"=========="); 
    } 
}
