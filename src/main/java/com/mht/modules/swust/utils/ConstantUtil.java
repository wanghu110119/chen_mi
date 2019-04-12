package com.mht.modules.swust.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mht.common.enumutils.CarTypeEnum;
import com.mht.common.service.CrudService;
import com.mht.common.utils.SpringContextHolder;
import com.mht.modules.swust.dao.SysCarMoneyDao;
import com.mht.modules.swust.dao.SysMessageDao;
import com.mht.modules.swust.entity.SysCarMoney;
import com.mht.modules.swust.entity.SysMessage;
import com.mht.modules.swust.service.impl.OrderUserServiceImpl;
import com.mht.modules.sys.dao.DictDao;

/**
 * 
 * @ClassName: ConstantUtil
 * @Description: 
 * @author com.mhout.wzw
 * @date 2017年8月1日 下午12:03:10 
 * @version 1.0.0
 */
public class ConstantUtil {
	
	 public static final int pageSize = 10;
	 public static final int showSize = 7;
	 public static final String default_pageCode = "1";
	 private static SysCarMoneyDao moneyDao = SpringContextHolder.getBean(SysCarMoneyDao.class);
	 
	/**
	 *  
	 * @Title: getmoney 
	 * @Description: TODO 显示金额工具方法
	 * 
	 * @param start
	 * @param carType
	 * @return
	 * @author com.mhout.wzw
	 */
	 public static double getmoney(String carType, long start){
		SysCarMoney money = moneyDao.selectByCarTypeId(carType);
		Date now = new Date();
		long datetime =(now.getTime() - start);
		if(datetime<=0){
			return 0;
		}
		double hour = (double)datetime;
		hour = hour/(60*60*1000);
		hour = Math.ceil(hour);
		int defaulttime = Integer.parseInt(money.getDefaultTime());
		int addmoney= Integer.parseInt(money.getAddMoney());
		int maxmoney = Integer.parseInt(money.getMaxMoney());
		double times =0.0;
		//小型车辆
		if (CarTypeEnum.SMALL.getParam().equals(carType)) {
			if(3<hour&&(defaulttime+(hour-3)*addmoney)<=maxmoney){
				return defaulttime+(hour-3)*addmoney;	
			}else if(hour<=3){
				return defaulttime;
			}else{
				return maxmoney;
			}
		//大型或超大型
		} else if (CarTypeEnum.LARGE.getParam().equals(carType)
				||CarTypeEnum.VERYLARGE.getParam().equals(carType)) {
			times = Math.ceil(hour/8);
			if(times*addmoney<=maxmoney){
				return times*addmoney;
			}else{
				return maxmoney;
			}
		} else {
			return 0;
		}
	 }
	 public static int accreditTime(Date last, Date start){
		 long accredittime = (last.getTime()-start.getTime());
		 double hour = Math.ceil(((double)accredittime)/(1000*3600));
		 return (int)hour;
	 }
	 /**
	  * 
	  * @Title: sendMessage 
	  * @Description: TODO发送短信计数
	  * @param messageDao
	  * @return
	  * @author com.mhout.wzw
	  */
	 public static boolean sendMessage(SysMessageDao messageDao){
			OrderUserServiceImpl service = new OrderUserServiceImpl();
			List<SysMessage> list = service.selectAllMessage();
			if (CollectionUtils.isNotEmpty(list)) {
				SysMessage message = list.get(0);
				if(Integer.parseInt(message.getSurplus())<1){
					return false;
				}
				message.setUsed(new StringBuilder().append((Integer.parseInt(message.getUsed())+1)).toString());
				message.setSurplus(new StringBuilder().append((Integer.parseInt(message.getSurplus())-1)).toString());
				messageDao.update(message);
			}
			return true;
	 }
}









