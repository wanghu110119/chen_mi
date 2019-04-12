package com.mht.modules.audit.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mht.common.config.Global;
import com.mht.common.persistence.Page;
import com.mht.common.service.CrudService;
import com.mht.common.utils.DateUtils;
import com.mht.modules.audit.dao.AppVisitDao;
import com.mht.modules.audit.entity.AppUserRecordData;
import com.mht.modules.audit.entity.AppUserRecordList;
import com.mht.modules.audit.entity.RealTime;
import com.mht.modules.audit.entity.RealTimeData;
import com.mht.modules.ident.dao.AppUserRecordDao;
import com.mht.modules.ident.dao.ApplicationDao;
import com.mht.modules.ident.entity.AppUserRecord;
import com.mht.modules.ident.entity.Application;
import com.mht.modules.sys.dao.OfficeDao;
import com.mht.modules.sys.entity.Office;

/**
 * @ClassName: AppVisitService
 * @Description: 应用访问历史业务层
 * @author com.mhout.xyb
 * @date 2017年4月25日 下午2:04:15 
 * @version 1.0.0
 */
@Service
@Transactional(readOnly = true)
public class AppVisitService extends CrudService<AppUserRecordDao, AppUserRecord>{
	
	@Autowired
	private AppVisitDao appVisitDao;
	
	@Autowired
	private OfficeDao officeDao;
	
	@Autowired
	private ApplicationDao applicationDao;
	

	public Page<AppUserRecord> findPage(Page<AppUserRecord> page, AppUserRecord appUserRecord) {
		// 设置默认时间范围，默认当前月
		if (appUserRecord.getBeginDate() == null){
			appUserRecord.setBeginDate(DateUtils.setDays(DateUtils.parseDate(DateUtils.getDate()), 1));
		}
		if (appUserRecord.getEndDate() == null){
			appUserRecord.setEndDate(DateUtils.addMonths(appUserRecord.getBeginDate(), 1));
		}
		return super.findPage(page, appUserRecord);
	}
	
	/**
	 * @Title: findVisitStatistics 
	 * @Description: 访问统计图
	 * @param appUserRecord
	 * @return
	 * @author com.mhout.xyb
	 */
	public AppUserRecordList findVisitStatistics(String type, Office office) {
		AppUserRecord appUserRecord = new AppUserRecord();
		appUserRecord.setOffice(office);
		if (StringUtils.isNotBlank(type)) {
			this.setDate(type, appUserRecord);
		} else {
			Date date = DateUtils.parseDate(DateUtils.getDate());
			appUserRecord.setBeginDate(date);
			appUserRecord.setEndDate(DateUtils.addDays(date, 1));
		}
		List<AppUserRecordData> list = appVisitDao.findVisitStatistics(appUserRecord);
		AppUserRecordList appRecord = new AppUserRecordList();
		if (CollectionUtils.isNotEmpty(list)) {
			List<Integer> count = new ArrayList<Integer>();
			List<String> value = new ArrayList<String>();
			for (AppUserRecordData data : list) {
				count.add(data.getValue());
				value.add(data.getName());
			}
			appRecord.setCount(count);
			appRecord.setValue(value);
			appRecord.setList(list);
		}
		return appRecord;
	}
	
	/**
	 * @Title: setDate 
	 * @Description: 设置日期范围
	 * @param type
	 * @param appUserRecord
	 * @author com.mhout.xyb
	 */
	private void setDate(String type, AppUserRecord appUserRecord) {
		Date date = DateUtils.parseDate(DateUtils.getDate());
		switch (type) {
		case "1":
			appUserRecord.setBeginDate(date);
			appUserRecord.setEndDate(DateUtils.addDays(date, 1));
			break;
		case "2":
			appUserRecord.setBeginDate(DateUtils.addDays(date, -1));
			appUserRecord.setEndDate(date);		
			break;
		case "3":
			appUserRecord.setBeginDate(DateUtils.getBeginDayOfWeek());
			appUserRecord.setEndDate(DateUtils.getEndDayOfWeek());
			break;
		case "4":
			appUserRecord.setBeginDate(DateUtils.getBeginDayOfMonth());
			appUserRecord.setEndDate(DateUtils.getEndDayOfMonth());
			break;
		case "5":
			appUserRecord.setBeginDate(DateUtils.getBeginDayOfYear());
			appUserRecord.setEndDate(DateUtils.getEndDayOfYear());
			break;
		case "6":
			appUserRecord.setBeginDate(null);
			appUserRecord.setEndDate(null);
			break;
		default:
			appUserRecord.setBeginDate(null);
			appUserRecord.setEndDate(null);
			break;
		}
	}
	
	/**
	 * @Title: findVisitAmount 
	 * @Description: 访问量TOP10统计
	 * @return
	 * @author com.mhout.xyb
	 */
	public List<AppUserRecordData> findVisitAmount(String grade) {
		if (StringUtils.isNotBlank(grade)) {
			return appVisitDao.findOfficeAmount(10, grade);
		}
		return appVisitDao.findVisitAmount(10);
	}
	
	/**
	 * @Title: findVisitUser 
	 * @Description: 用户占比TOP10
	 * @return
	 * @author com.mhout.xyb
	 */
	public List<AppUserRecordData> findVisitUser(String grade) {
		if (StringUtils.isNotBlank(grade)) {
			return appVisitDao.findOfficeUser(10, grade);
		}
		return appVisitDao.findVisitUser(10);
	}
	
	/**
	 * @Title: findVisitUser 
	 * @Description: 应用访问趋势TOP10
	 * @return
	 * @author com.mhout.xyb
	 */
	public List<AppUserRecordData> findVisitTrend(String grade) {
		if (StringUtils.isNotBlank(grade)) {
			return appVisitDao.findDepartTrend(10, grade);
		}
		return appVisitDao.findVisitTrend(10);
	}
	
	/**
	 * @Title: findVisitHistory 
	 * @Description: 访问历史总览
	 * @return
	 * @author com.mhout.xyb
	 */
	public List<AppUserRecordData> findVisitHistory() {
		return appVisitDao.findVisitHistory();
	}
	
	/**
	 * @Title: findRealTime 
	 * @Description: 应用实时数据
	 * @param id
	 * @return
	 * @author com.mhout.xyb
	 */
	public List<RealTimeData> findRealTime(String id, String dateType) {
		//返回数据
		List<RealTimeData> list = new ArrayList<RealTimeData>();
		RealTimeData data = new RealTimeData();
		//院系信息
		List<String> officevalues = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		List<RealTime> realtimes = new ArrayList<RealTime>();
		Office office = new Office();
		office.setGrade("2");
		List<Office> offices = officeDao.findAllList(office);
		if (CollectionUtils.isNotEmpty(offices)) {
			for (Office reoffice : offices) {
				officevalues.add(reoffice.getName());
				//坐标数据
				List<String> counts = new ArrayList<String>();
				Date endDate = new Date();
				//设置时间
				Date startDate = setRealTimeDate(endDate, dateType);
				List<AppUserRecordData> countlist = appVisitDao.findAppRealTime(reoffice.getId(), id,
						startDate, endDate, dateType, Global.YES);
				if (CollectionUtils.isNotEmpty(countlist)) {
					for (AppUserRecordData redata : countlist) {
						//各院系访问总和
						counts.add(redata.getValue()+"");
						if (!values.contains(redata.getName())) {
							//添加时间
							values.add(redata.getName());
						}
					}
				} else {
					counts.add("0");
				}
				RealTime realtime = new RealTime();
				realtime.setName(reoffice.getName());
				realtime.setData(counts);
				realtimes.add(realtime);
			}
		}
		data.setSection(officevalues);
		if (CollectionUtils.isEmpty(values)) {
			values.add(DateUtils.getDate("HH:mm:ss"));
		}
		data.setValue(values);
		data.setList(realtimes);
		list.add(data);
		return list;
	}
	
	/**
	 * @Title: findOfficeRealTime 
	 * @Description: 应用实时统计
	 * @param id
	 * @param dateType
	 * @return
	 * @author com.mhout.xyb
	 */
	public List<RealTimeData> findOfficeRealTime(String id, String dateType){
		//返回数据
		List<RealTimeData> list = new ArrayList<RealTimeData>();
		RealTimeData data = new RealTimeData();
		//应用信息
		List<String> applicationValues = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		List<RealTime> realtimes = new ArrayList<RealTime>();
		List<Application> applications = applicationDao.findAllSystemApps();
		if (CollectionUtils.isNotEmpty(applications)) {
			for (Application application : applications) {
				applicationValues.add(application.getName());
				//坐标数据
				List<String> counts = new ArrayList<String>();
				Date endDate = new Date();
				//设置时间
				Date startDate = setRealTimeDate(endDate, dateType);
				List<AppUserRecordData> countlist = appVisitDao.findAppRealTime(id, application.getId(),
						startDate, endDate, dateType, Global.YES);
				if (CollectionUtils.isNotEmpty(countlist)) {
					for (AppUserRecordData redata : countlist) {
						//应用访问总和
						counts.add(redata.getValue()+"");
						if (!values.contains(redata.getName())) {
							//添加时间
							values.add(redata.getName());
						}
					}
				} else {
					counts.add("0");
				}
				RealTime realtime = new RealTime();
				realtime.setName(application.getName());
				realtime.setData(counts);
				realtimes.add(realtime);
			}
		}
		data.setSection(applicationValues);
		if (CollectionUtils.isEmpty(values)) {
			values.add(DateUtils.getDate("HH:mm:ss"));
		}
		data.setValue(values);
		data.setList(realtimes);
		list.add(data);
		return list;
	}
	
	
	public List<RealTimeData> findAppTimer(String id, String dateType) {
		List<List<Object>> datalist = new ArrayList<List<Object>>();
		List<RealTimeData> data = new ArrayList<RealTimeData>();
		RealTimeData realtime = new RealTimeData();
		Office office = new Office();
		office.setGrade("2");
		List<Office> offices = officeDao.findAllList(office);
		if (CollectionUtils.isNotEmpty(offices)) {
			for (int i=0;i<offices.size();i++) {
				//设置时间
				Date endDate = new Date();
				Date startDate = setRealTimeDate(endDate, dateType);
				List<Object> list = new ArrayList<Object>();
				list.add(i);
				List<AppUserRecordData> countlist = appVisitDao.findAppRealTime(offices.get(i).getId(), id,
						startDate, endDate, dateType, Global.NO);
				list.add(countlist.size()>0?countlist.get(0).getValue():0);
				list.add(true);
				list.add(true);
				if (i==0) {
					list.add(DateUtils.getDate("HH:mm:ss"));
				} 
				datalist.add(list);
			}
		}
		realtime.setTimer(datalist);
		data.add(realtime);
		return data;
	}
	
	public List<RealTimeData> findOfficeTimer(String id, String dateType) {
		List<List<Object>> datalist = new ArrayList<List<Object>>();
		List<RealTimeData> data = new ArrayList<RealTimeData>();
		RealTimeData realtime = new RealTimeData();
		List<Application> applications = applicationDao.findAllSystemApps();
		if (CollectionUtils.isNotEmpty(applications)) {
			for (int i=0;i<applications.size();i++) {
				//设置时间
				Date endDate = new Date();
				Date startDate = setRealTimeDate(endDate, dateType);
				List<Object> list = new ArrayList<Object>();
				list.add(i);
				List<AppUserRecordData> countlist = appVisitDao.findAppRealTime(id, applications.get(i).getId(),
						startDate, endDate, dateType, Global.NO);
				list.add(countlist.size()>0?countlist.get(0).getValue():0);
				list.add(true);
				list.add(true);
				if (i==0) {
					list.add(DateUtils.getDate("HH:mm:ss"));
				} 
				datalist.add(list);
			}
		}
		realtime.setTimer(datalist);
		data.add(realtime);
		return data;
	}
	
	
	
	/**
	 * @Title: setRealTimeDate 
	 * @Description: 设置实时数据时间
	 * @param startDate
	 * @param endDate
	 * @author com.mhout.xyb
	 */
	private Date setRealTimeDate(Date endDate, String dateType) {
		Date date = new Date();
		switch (dateType) {
		case "1":
			date = DateUtils.addMinutes(endDate, -1);
			break;
		case "2":
			date = DateUtils.addMinutes(endDate, -10);
			break;
		case "3":
			date = DateUtils.addMinutes(endDate, -30);
			break;
		case "4":
			date = DateUtils.addHours(endDate, -3);
			break;
		case "5":
			date = DateUtils.addDays(endDate, -1);
			break;
		case "6":
			date = DateUtils.addDays(endDate,-3);
			break;
		default:
			break;
		}
		return date;
	}
	
}
