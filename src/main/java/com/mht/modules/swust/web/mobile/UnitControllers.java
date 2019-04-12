/**
 * 
 */
package com.mht.modules.swust.web.mobile;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mht.common.persistence.Page;
import com.mht.common.utils.StringUtils;
import com.mht.modules.swust.entity.Manager;
import com.mht.modules.swust.entity.Unit;
import com.mht.modules.swust.service.UnitService;

/**
 * @ClassName: UnitController
 * @Description: 
 * @author com.mhout.dhn
 * @date 2017年7月26日 下午4:57:20 
 * @version 1.0.0
 */
@Controller
@RequestMapping(value = "mobile/oeg/unit")
public class UnitControllers {	
	  @Autowired
	  private UnitService unitService;	 
       /**
        * 
        * @Title: getAll 
        * @Description: 查询全部
        * @return
        * @author com.mhout.dhn
        */
	   @ResponseBody	
	   @RequestMapping("unit")
	   public  String   getAll(Unit unit, HttpServletRequest request, 
		   HttpServletResponse response, Model model) {					
		   Page<Unit> page = unitService.findPage(new Page<Unit>(request, response), unit);
		   model.addAttribute("page", page);			
		   return "modules/swust/mobile/unitList";
	  }	  
	  /**
	   * 
	   * @Title: update 
	   * @Description: 修改数据数据
	   * @param id
	   * @return
	   * @author com.mhout.dhn
	   */
	  @ModelAttribute("update")		
	  public String  update(Unit unit) {
			unitService.save(unit);
			return "modules/swust/mobile/unitList";        
	  }	  
	  /**
       * 
       * @Title: updateStatus 
       * @Description: 修改是否能够使用的状态
       * @return
       * @author com.mhout.dhn
       */
	  @ModelAttribute("updateStatus")
	  public String  updateStatus(Unit unit){		  	    
	    unitService.save(unit);	    
		return "modules/swust/mobile/unitList";	  
	  }	  
	  /**
       * 
       * @Title: insert 
       * @Description: 添加元素
       * @return
       * @author com.mhout.dhn
       */
	  @ModelAttribute("inset")
	  public String  inset(Unit unit){
		unitService.save(unit); 
		return  "modules/swust/mobile/unitList";
	  }
	  /**
       * 
       * @Title: ListByOrganization 
       * @Description: 单位名查询
       * @return
       * @author com.mhout.dhn
       */
	  @ResponseBody
	  @ModelAttribute("ListByOrganization")
	  public String listByOrganization(Unit unit,HttpServletRequest request,HttpServletResponse response,Model model){		  
		  List<Unit> findList = unitService.findList(unit);	
		  Page<Unit> page = unitService.findPage(new Page<Unit>(request, response), unit);
		  model.addAttribute("page", page);			
		 return "modules/swust/mobile/unitList";
	  }

   
}
