package com.mht.modules.audit.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.mht.common.persistence.CrudDao;
import com.mht.common.persistence.annotation.MyBatisDao;
import com.mht.modules.audit.entity.AppUserRecordData;
import com.mht.modules.ident.entity.AppUserRecord;

/**
 * @ClassName: AppVisitDao
 * @Description: 应用访问历史持久层
 * @author com.mhout.xyb
 * @date 2017年4月25日 下午1:47:21 
 * @version 1.0.0
 */
@MyBatisDao
public interface AppVisitDao extends CrudDao<AppUserRecord>{
	
	/**
	 * @Title: findVisitStatistics 
	 * @Description: 查询应用统计
	 * @param appUserRecord
	 * @return
	 * @author com.mhout.xyb
	 */
	public List<AppUserRecordData> findVisitStatistics(AppUserRecord appUserRecord);
	
	/**
	 * @Title: findVisitAmount 
	 * @Description: 访问量TOP10
	 * @param appUserRecord
	 * @return
	 * @author com.mhout.xyb
	 */
	public List<AppUserRecordData> findVisitAmount(@Param("limit")Integer limit);
	
	/**
	 * @Title: findVisitAmount 
	 * @Description: 院系访问量TOP10
	 * @param limit
	 * @param grade
	 * @return
	 * @author com.mhout.xyb
	 */
	public List<AppUserRecordData> findOfficeAmount(@Param("limit")Integer limit,@Param("grade")String grade);
	
	/**
	 * @Title: findVisitUser 
	 * @Description: 用户占比TOP10
	 * @param limit
	 * @return
	 * @author com.mhout.xyb
	 */
	public List<AppUserRecordData> findVisitUser(@Param("limit")Integer limit);
	
	/**
	 * @Title: findOfficeUser 
	 * @Description: 院系用户占比TOP10
	 * @param limit
	 * @param grade
	 * @return
	 * @author com.mhout.xyb
	 */
	public List<AppUserRecordData> findOfficeUser(@Param("limit")Integer limit, @Param("grade")String grade);
	
	/**
	 * @Title: findVisitUser 
	 * @Description: 应用访问趋势TOP10
	 * @param limit
	 * @return
	 * @author com.mhout.xyb
	 */
	public List<AppUserRecordData> findVisitTrend(@Param("limit")Integer limit);
	
	/**
	 * @Title: findDepartTrend 
	 * @Description: 院系访问趋势TOP10
	 * @param limit
	 * @param grade
	 * @return
	 * @author com.mhout.xyb
	 */
	public List<AppUserRecordData> findDepartTrend(@Param("limit")Integer limit, @Param("grade")String grade);
	
	/**
	 * @Title: findVisitHistory 
	 * @Description: 访问历史总览
	 * @return
	 * @author com.mhout.xyb
	 */
	public List<AppUserRecordData> findVisitHistory();
	
	/**
	 * @Title: findAppRealTime 
	 * @Description: 应用访问实时数据
	 * @param oid
	 * @param aid
	 * @return
	 * @author com.mhout.xyb
	 */
	public List<AppUserRecordData> findAppRealTime(@Param("oid")String oid, @Param("aid")String aid,
			@Param("startDate")Date startDate, @Param("endDate")Date endDate, @Param("type")String type,
			@Param("searchType")String searchType);
}
