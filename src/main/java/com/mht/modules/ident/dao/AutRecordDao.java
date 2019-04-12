package com.mht.modules.ident.dao;

import java.util.List;

import com.mht.common.persistence.CrudDao;
import com.mht.common.persistence.annotation.MyBatisDao;
import com.mht.modules.ident.entity.AutRecord;
import com.mht.modules.ident.entity.AutRecordData;

/**
 * @ClassName: AutRecordDao
 * @Description: 认证记录Dao
 * @author com.mhout.xyb
 * @date 2017年3月24日 上午9:44:01 
 * @version 1.0.0
 */
@MyBatisDao
public interface AutRecordDao extends CrudDao<AutRecord>{


	
	public List<AutRecordData> findDateList(AutRecord autRecord);
	
	public List<AutRecordData> findWeekList(AutRecord autRecord);
	
	public List<AutRecordData> findMothList(AutRecord autRecord);
	
	public List<AutRecordData> findYearList(AutRecord autRecord);
	
}
