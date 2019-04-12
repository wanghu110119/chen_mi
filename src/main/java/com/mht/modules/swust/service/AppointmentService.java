package com.mht.modules.swust.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mht.common.persistence.Page;
import com.mht.common.service.CrudService;
import com.mht.common.utils.StringUtils;
import com.mht.modules.account.web.CommonController;
import com.mht.modules.swust.dao.AppointmentDao;
import com.mht.modules.swust.entity.Appointment;
import com.mht.modules.sys.entity.Office;
import com.mht.modules.sys.entity.Post;
import com.mht.modules.sys.entity.Role;
import com.mht.modules.sys.utils.DictUtils;

/**
 * 
 * @ClassName: AppointmentService
 * @Description: 
 * @author com.mhout.dhn
 * @date 2017年7月26日 下午2:44:45 
 * @version 1.0.0
 */
 
@Service
@Transactional(readOnly = true)
public class AppointmentService extends CrudService<AppointmentDao, Appointment>{
	
	
	@Autowired
	private  AppointmentDao appointmentDao;
	
	
	
	
	/** 
	 * @Title: saveApp 
	 * @Description: TODO
	 * @param ment
	 * @return
	 * @author com.mhout.dhn
	 */
	public int saveApp(Appointment ment) {		
		int maxId = appointmentDao.findByMaxId();	
		return maxId;
	}
	@Override
	public Page<Appointment> findPage(Page<Appointment> page, Appointment entity) {
	// TODO Auto-generated method stub
		entity.setPage(page); 
		Integer status = entity.getAuditStatus();
		if(status==-1){
	        page.setList(dao.findCheckStatus(entity));
		}else if(status==0){
			page.setList(dao.findUnCheckStatus(entity));
		}
	    return page;
	}
	/** 
	 * @Title: findAppointment 
	 * @Description: TODO
	 * @param page
	 * @param appointment
	 * @return
	 * @author com.mhout.dhn
	 */
	public  Page<Appointment> findAppointment(Page<Appointment> page, Appointment appointment) {
		    // TODO Auto-generated method stub
		    appointment.setPage(page);
	        List<Appointment> appointments = appointmentDao.findList(appointment);	       
	        page.setList(appointments);
	        return page;
	}
	/** 
	 * @Title: findAll 
	 * @Description: TODO
	 * @param appointment
	 * @author com.mhout.dhn
	 */
	public void findAll(Appointment appointment) {
		// TODO Auto-generated method stub
		
	}
	/** 
	 * @Title: findAll 
	 * @Description: TODO
	 * @param abutmentCompany
	 * @author com.mhout.dhn
	 */
	public void findAll(String abutmentCompany) {
		// TODO Auto-generated method stub
		
	}		
	
}
