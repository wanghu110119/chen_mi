package com.mht.modules.swust.dao;

import java.util.List;

import com.mht.common.persistence.CrudDao;
import com.mht.common.persistence.annotation.MyBatisDao;
import com.mht.modules.swust.entity.Appointment;
/**
 * @ClassName: AppointmentDao
 * @Description: 
 * @author com.mhout.xyb
 * @date 2017年7月26日 下午2:44:00 
 * @version 1.0.0
 */
@MyBatisDao
public interface AppointmentDao extends CrudDao<Appointment>{
	
	/**
	 * @Title: findByMaxId 
	 * @Description: 获取最大ID
	 * @return
	 * @author com.mhout.dhn
     */
	public int findByMaxId();		
	/** 
	 * @Title: findlistExceptStatus 
	 * @Description: TODO
	 * @param str
	 * @return
	 * @author com.mhout.dhn
	 */
	public List<Appointment> findlistExceptStatus(Integer str);
	/** 
	 * @Title: findCheckStatus 
	 * @Description: TODO
	 * @param entity
	 * @return
	 * @author com.mhout.dhn
	 */
	public List<Appointment> findCheckStatus(Appointment entity);
	/** 
	 * @Title: findUnCheckStatus 
	 * @Description: TODO
	 * @param entity
	 * @return
	 * @author com.mhout.dhn
	 */
	public List<Appointment> findUnCheckStatus(Appointment entity);	
}
