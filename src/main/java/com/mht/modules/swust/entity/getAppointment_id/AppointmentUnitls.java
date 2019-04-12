/**
 * 
 */
package com.mht.modules.swust.entity.getAppointment_id;

import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import com.mht.common.utils.DateUtils;
import com.mht.modules.swust.entity.Appointment;
import com.mht.modules.swust.service.AppointmentService;

/**
 * @ClassName: Appointment_idUnitl
 * @Description: 
 * @author com.mhout.dhn
 * @date 2017年7月26日 下午5:31:02 
 * @version 1.0.0
 */
public class AppointmentUnitls {
		
	 @Autowired
	 private AppointmentService  appointmentService;
	 @Autowired 
	 private Appointment appointment;
	 
	 private static final String YEAR="yyyy";
	 
     public  Integer  getgetAppointmentId(){
    	 
    	 Integer appointment_id= null;
    	 
    	 int app=appointmentService.saveApp(appointment);
    	 Appointment appoint = appointmentService.get(""+app);
    	 //int sub =Integer.parseInt((appoint.getAppointmentId().toString(0, 1)));   	     	 
    	 int year=Integer.parseInt(DateUtils.formatDate(new Date(), YEAR).substring(2, 3)); 
    	 
    	 //if(sub==year){//17001
    		 app++; 		
    	 //}else{
    		// appointment_id=Integer.parseInt(year+"001");
    	 //}
    	 
    	 return appointment_id ;
    	 
     } 
     /**
     public int getId(){
    	 
    		 
    	 int app=appointmentService.saveApp(appointment);
    	 
    	 Appointment appoint = appointmentService.get(""+app);
    	 
    	 String year = DateUtils.formatDate(new Date(), YEAR).substring(2, 3); 
    	 
    	 String sub = appoint.getAppointment_id().substring(3, 4); 
    	 
    	 if(year==sub){
    		 
    		 return app++;
    		 
    	 }else {
    		 return  app=0;
    	 }
    	 
       	 
     }
     */
     
    
}
