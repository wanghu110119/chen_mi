package com.mht.modules.ident.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mht.common.service.CrudService;
import com.mht.common.utils.StringUtils;
import com.mht.modules.audit.dao.SafeStrategyDao;
import com.mht.modules.audit.entity.SafeStrategy;
import com.mht.modules.ident.dao.AutRecordDao;
import com.mht.modules.ident.entity.AppUserRecord;
import com.mht.modules.ident.entity.Application;
import com.mht.modules.ident.entity.AutRecord;
import com.mht.modules.ident.entity.AutRecordData;
import com.mht.modules.ident.entity.AutRecordListData;
import com.mht.modules.sys.dao.OfficeDao;
import com.mht.modules.sys.entity.Office;
import com.mht.modules.sys.entity.User;
import com.mht.modules.sys.utils.UserUtils;

/**
 * @ClassName: AutRecordService
 * @Description: 认证记录Service
 * @author com.mhout.xyb
 * @date 2017年3月24日 上午9:45:04
 * @version 1.0.0
 */
@Service
@Transactional(readOnly = true)
public class AutRecordService extends CrudService<AutRecordDao, AutRecord> {
	
	private static final String logapp = "log_app";
	private static final String record = "记录";

	@Autowired
	private AutRecordDao autRecordDao;
	
	@Autowired
	private OfficeDao officeDao;
	
	@Autowired
	private AppUserRecordService appUserRecordService;
	
	@Autowired
	private SafeStrategyDao safeStrategyDao;
	
	
	 /**
     * @Title: record 
     * @Description: 应用访问记录
     * @param id
     * @author com.mhout.xyb
     */
	@Transactional(readOnly = false)
    public void record(Application application) {
		SafeStrategy strategy = safeStrategyDao.findByCode(logapp);
		if (strategy != null && record.equals(strategy.getValue())) {
			User user = UserUtils.getUser();
	    	if (user != null && application != null) {
	    		AppUserRecord appUserRecord = new AppUserRecord();
	    		//获取用户学院
	            List<Office> offices = officeDao.findOfficeByUser(user.getId());
	            if (offices != null && !offices.isEmpty()) {
	            	Office office = offices.get(0);
	                String ids = office.getParentIds();
	        		if (StringUtils.isNotBlank(ids)) {
	        			String[] values = ids.split("\\,");
	        			for (String id : values) {
	        				Office reoffice = officeDao.get(id);
	        				if (reoffice != null && "2".equals(reoffice.getGrade())) {
	        					appUserRecord.setOffice(reoffice);
	        					break;
	        				} 
	        			}
	        		}
	        		if (appUserRecord.getOffice() == null 
	        				&& "2".equals(office.getGrade())) {
	        			appUserRecord.setOffice(office);
	        		}
	            }
	    		appUserRecord.setApplication(application);
	    		appUserRecord.setUser(user);
	    		appUserRecordService.save(appUserRecord);
	    	}
		}
    }

	/**
	 * @Title: findDateList
	 * @Description: 根据日期查询
	 * @param autRecord
	 * @return
	 * @author com.mhout.xyb
	 */
	public List<AutRecord> findDateList(AutRecord autRecord) {
		return super.findList(autRecord);
	}

	/**
	 * @Title: findAutRecordListData
	 * @Description: 认证统计
	 * @param autRecord
	 * @return
	 * @author com.mhout.xyb
	 */
	public AutRecordListData findAutRecordListData(AutRecord autRecord) {
		AutRecordListData data = new AutRecordListData();
		if (autRecord.getSuccessDate() != null) {
			processData(autRecord, data, "1", "1", autRecord.getSuccessDate());
		} else if (autRecord.getFaultDate() != null) {
			processData(autRecord, data, "2", "1", autRecord.getFaultDate());
		} else if (autRecord.getCnSuccessDate() != null) {
			processData(autRecord, data, "1", getSearchType(autRecord.getCnSuccessDate()), 
					new Date());
		} else if (autRecord.getCnFaultDate() != null) {
			processData(autRecord, data, "2", getSearchType(autRecord.getCnFaultDate()), 
					new Date());
		}
		return data;
	}

	/**
	 * @Title: processData 
	 * @Description: 处理数据
	 * @param autRecord
	 * @param data 返回数据
	 * @param isSuccess 认证是否成功
	 * @param type 类型
	 * @param date
	 * @author com.mhout.xyb
	 */
	private void processData(AutRecord autRecord, AutRecordListData data, String isSuccess, String type, Date date) {
		List<Integer> value = new ArrayList<Integer>();
		List<String> time = new ArrayList<String>();
		AutRecord newAut = new AutRecord();
		// 认证成功
		newAut.setIsSuccess(isSuccess);
		newAut.setType(type);
		newAut.setDayDate(date);
		List<AutRecordData> autRecords = this.findDateCount(newAut);
		for (AutRecordData autRecordData : autRecords) {
			time.add(autRecordData.getTime());
			value.add(autRecordData.getAutCount());
		}
		judgeData(value, time, data, newAut.getType());
	}

	public String getSearchType(String type) {
		String msg = "";
		// 周
		if ("1".equals(type)) {
			msg = "2";
		}
		// 月
		else if ("2".equals(type)) {
			msg = "3";
		}
		// 年
		else if ("3".equals(type)) {
			msg = "4";
		}
		return msg;
	}

	public void judgeData(List<Integer> value, List<String> time, AutRecordListData data, String type) {
		List<Integer> value2 = new ArrayList<Integer>();
		List<String> time2 = new ArrayList<String>();
		int length = 0;
		String addStr = "";
		int start = 0;
		// 今日
		if ("1".equals(type)) {
			length = 23;
		}
		// 本周
		else if ("2".equals(type)) {
			length = 6;
		}
		// 本月
		else if ("3".equals(type)) {
			length = getCurrentMonthDay();
			start = 1;
		}
		// 本年
		else if ("4".equals(type)) {
			length = 12;
			addStr = "0";
			start = 1;
		}
		for (int i = start; i <= length; i++) {
			if ("4".equals(type) && i > 9) {
				addStr = "";
			}
			if (time.contains(addStr + i)) {
				value2.add(value.get(time.indexOf(addStr + i)));
			} else {
				value2.add(0);
			}
			if ("2".equals(type)) {
				time2.add(week(addStr + i));
			} else {
				time2.add(addStr + i);
			}
		}
		data.setTime(time2);
		data.setValue(value2);
	}

	/**
	 * @Title: findDateCount
	 * @Description: TODO
	 * @param autRecord
	 * @return
	 * @author com.mhout.xyb
	 */
	public List<AutRecordData> findDateCount(AutRecord autRecord) {
		// 今日
		if ("1".equals(autRecord.getType())) {
			return autRecordDao.findDateList(autRecord);
		}
		// 本周
		else if ("2".equals(autRecord.getType())) {
			return autRecordDao.findWeekList(autRecord);
		}
		// 本月
		else if ("3".equals(autRecord.getType())) {
			return autRecordDao.findMothList(autRecord);
		}
		// 本年
		else if ("4".equals(autRecord.getType())) {
			return autRecordDao.findYearList(autRecord);
		}
		return null;
	}

	private String week(String num) {
		String mString = "星期";
		switch (num) {
		case "0":
			mString = "星期一";
			break;
		case "1":
			mString = "星期二";
			break;
		case "2":
			mString = "星期三";
			break;
		case "3":
			mString = "星期四";
			break;
		case "4":
			mString = "星期五";
			break;
		case "5":
			mString = "星期六";
			break;
		case "6":
			mString = "星期日";
			break;
		default:
			break;
		}
		return mString;
	}

	private int getCurrentMonthDay() {
		Calendar a = Calendar.getInstance();
		a.set(Calendar.DATE, 1);
		a.roll(Calendar.DATE, -1);
		int maxDate = a.get(Calendar.DATE);
		return maxDate;
	}

}
