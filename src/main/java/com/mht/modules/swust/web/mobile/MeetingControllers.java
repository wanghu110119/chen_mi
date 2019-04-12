package com.mht.modules.swust.web.mobile;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mht.common.json.AjaxJson;
import com.mht.common.utils.StringUtils;
import com.mht.common.web.BaseController;
import com.mht.modules.swust.entity.SysBackstageTime;
import com.mht.modules.swust.entity.SysCarMoney;
import com.mht.modules.swust.entity.SysOrderlist;
import com.mht.modules.swust.service.BackstageTimeService;
import com.mht.modules.swust.service.impl.OrderUserServiceImpl;
import com.mht.modules.swust.utils.QRCodeCreate;
import com.mht.modules.sys.entity.Office;
import com.mht.modules.sys.utils.UserUtils;
@Controller
@RequestMapping(value = "mobile/swust/meeting")
public class MeetingControllers extends BaseController{
	
	
	
	@Autowired
	OrderUserServiceImpl service;
	
	@Autowired
	BackstageTimeService timeService;
	
	@ModelAttribute("sysOrderlist")
	public SysOrderlist get(@RequestParam(required = false) String id) {
		if (StringUtils.isNotBlank(id)) {
			return service.get(id);
		} else {
			return new SysOrderlist();
		}
	}
	
	@RequestMapping(value="index")
	public String index(Model model, SysOrderlist sysOrderList){
		return "modules/swust/index";
	}
	
	
	
	@RequestMapping(value="add")
	public String insertOrder(Model model, SysOrderlist sysOrderList){
		if(sysOrderList.getEndTime().getTime()<=new Date().getTime()){
			return "";
		}
		List<Office> officelist = service.findOfficeList();
		List<SysCarMoney> car = service.selectAllByCar();
		SimpleDateFormat data = new SimpleDateFormat("yyyy-MM-dd");
		   SysBackstageTime sysBackstageDate = new SysBackstageTime();
		   sysBackstageDate.setBeginTime(sysOrderList.getBeginTime());
			   model.addAttribute("endDate", data.format(sysOrderList.getEndTime()));
		   model.addAttribute("beginDate", data.format(sysBackstageDate.getBeginTime()));
		   model.addAttribute("sysOrderList", sysOrderList);
		   model.addAttribute("redio", sysBackstageDate);
		model.addAttribute("cartype", car);
		model.addAttribute("officelist",officelist);
		return "modules/swust/mobile/MeetingOrderAdd";
	}
	
	
	/**
	 * @Title: addOrder 
	 * @Description: 添加预约
	 * @param order
	 * @param result
	 * @return
	 * @author com.mhout.xyb
	 */
	@ResponseBody
	@RequestMapping(value="addOrder")
	public AjaxJson addOrder(@Valid SysOrderlist order,BindingResult result,HttpServletRequest req){
		AjaxJson json = new AjaxJson();
		if (!beanValidatorForBinding(result, json)) {
			json.setSuccess(false);
		}else{
			json.setSuccess(true);
		}
		order.setCreateBy(UserUtils.get(order.getUserId()));
		order.setUpdateBy(UserUtils.get(order.getUserId()));
		order.setColor("0");
		if("2".equals(req.getParameter("disable"))){
			Calendar cal = Calendar.getInstance();   
	        cal.setTime(order.getEndTime());   
	        cal.add(Calendar.HOUR, 24);// 24小时制   
	        cal.add(Calendar.SECOND, -1);
	        order.setEndTime(cal.getTime()); 
		}
		service.saveOrder(order);
		return json;
	}
	
	
	
	
}
