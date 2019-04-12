/**
 * 
 */
package com.mht.modules.swust.web;

import java.io.IOException;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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

import com.mht.common.enumutils.ApiCodeEnum;
import com.mht.common.json.AjaxJson;
import com.mht.common.persistence.Page;
import com.mht.common.utils.MD5Utils;
import com.mht.common.utils.StringUtils;
import com.mht.common.web.BaseController;
import com.mht.modules.account.service.CommonService;
import com.mht.modules.swust.dao.SysOfficeDao;
import com.mht.modules.swust.entity.SysBackstageTime;
import com.mht.modules.swust.entity.SysCarMoney;
import com.mht.modules.swust.entity.SysOrderlist;
import com.mht.modules.swust.entity.SysPhotolist;
import com.mht.modules.swust.service.BackstageTimeService;
import com.mht.modules.swust.service.impl.OrderUserServiceImpl;
import com.mht.modules.sys.dao.OfficeDao;
import com.mht.modules.sys.entity.Office;
import com.mht.modules.sys.entity.User;
import com.mht.modules.sys.service.SystemService;
import com.mht.modules.sys.utils.UserUtils;



/**
 * @ClassName: UserController
 * @Description: 
 * @author 王梓玮
 * @date 2017年7月24日 上午11:32:26 
 * @version 1.0.0
 */
@Controller
@RequestMapping(value = "a/swust/order")
public class OrderUserController extends BaseController{
	
	@Autowired
	OrderUserServiceImpl service;
	
	@Autowired
	private SysOfficeDao officeDao;
	
	@Autowired
	BackstageTimeService timeService;
	
	@Autowired
	private SystemService systemService;
	
	@ModelAttribute("sysOrderlist")
	public SysOrderlist get(@RequestParam(required = false) String id ){
		if (StringUtils.isNotBlank(id)){
			return service.get(id);
		} else {
			return new SysOrderlist();
		}
	}
	
	/**
	 * @Title: index 
	 * @Description: 跳转页面
	 * @return
	 * @author com.mhout.wzw
	 */
	@RequestMapping(value={"","sysOrder"})
	public String index() {
		return "/modules/swust/sysOrder";
	}
	
	/**
	 * 
	 * @Title: UserSelectOrderList 
	 * @Description: 查询所有预约
	 * @param user
	 * @param req
	 * @param pageCode
	 * @return
	 * @author com.mhout.wzw
	 */
	@RequestMapping("list")
	public String UserSelectOrderList(String name,String state,String color,SysOrderlist sysOrderlist,HttpServletRequest request, 
			HttpServletResponse response, Model model){
		if(state==null||state.equals("")){
			sysOrderlist.setState("0");
		}else if(state.equals("3")){
			sysOrderlist.setState("1");
		}else{
			sysOrderlist.setState(state);
		}
		sysOrderlist.setUserId(UserUtils.getUser().getId());
		sysOrderlist.setOrderName(name);
		sysOrderlist.setColor(color);
		Page<SysOrderlist> page = service.findPage(new Page<SysOrderlist>(request, response), sysOrderlist);
		SysBackstageTime sysBackstageTime = service.selectBackstageTime();
		timeService.save(sysBackstageTime);
		if("".equals(color)||color==null){
			Map<String,List<SysOrderlist>> sortMap = new HashMap<>();
			for (SysOrderlist list : page.getList()) {
				String carNum = list.getCarNumber();
				sortMap.put(carNum, new ArrayList<SysOrderlist>());
			}
			for (SysOrderlist add : page.getList()) {
				sortMap.get(add.getCarNumber()).add(add);
			}//map生成完成
			
			Iterator iter = sortMap.keySet().iterator();
			while (iter.hasNext()) {
			String key = (String) iter.next();
			List<SysOrderlist> val = sortMap.get(key);
			for (int i = 0; i < val.size(); i++) {
				for (int j = i+1; j < val.size(); j++) {
					if(Math.abs(val.get(i).getBeginTime().getTime()-val.get(j).getBeginTime().getTime())<36*60*60*1000){
						val.get(i).setColor("red");
						val.get(j).setColor("red");
						service.updateOrderList(val.get(i));
						service.updateOrderList(val.get(j));
					}
				}
			}
			}
		}else{
			page = service.findPage(new Page<SysOrderlist>(request, response), sysOrderlist);
		}
		page.setBeginAndEnd(5);
		if(page.getPageNo()==0){page.setPageNo(1);}
		model.addAttribute("defaultTime", sysBackstageTime.getEndTime());
		model.addAttribute("user", UserUtils.getUser());
		model.addAttribute("sysOrderlist",sysOrderlist);
        model.addAttribute("page", page);
		return "/modules/swust/sysOrderlist";
	}
	/**
	 * 
	 * @Description: 查询警告预约
	 * @param model
	 * @param order
	 * @param request
	 * @param response	
	 * @return
	 */
	@RequestMapping(value="selectOrderListByColor")
	public String selectOrderListByColor(Model model,String state,SysOrderlist order,HttpServletRequest request, String color,
		HttpServletResponse response){
		if(state==null||state.equals("")){
			order.setState("0");
		}else if(state.equals("3")){
			order.setState("1");
		}else{
			order.setState(state);
		}
		order.setColor(color);
		Page<SysOrderlist> page = service.findPage(new Page<SysOrderlist>(request, response), order); 
		model.addAttribute("page", page);
		return "/modules/swust/sysOrderlist";
	}
	
	
	
	/**
	 * 
	 * @Title: insertOrder 
	 * @Description: 添加预约
	 * @param user
	 * @param req
	 * @param pageCode
	 * @param order
	 * @return
	 * @author com.mhout.wzw
	 */
	@RequestMapping(value="add")
	public String insertOrder(Model model){
		Office office = new Office();
		office.setGrade("2");
		office.setUseable("1");
		List<Office> officelist = officeDao.findList(office);
		List<SysCarMoney> car = service.selectAllByCar();
		SysBackstageTime sysBackstageTime = service.selectBackstageTime();
		timeService.save(sysBackstageTime);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat data = new SimpleDateFormat("yyyy-MM-dd");
		   String dateString = formatter.format(sysBackstageTime.getBeginTime());
		   model.addAttribute("beginTimeShow", sysBackstageTime.getBeginTime());
		   model.addAttribute("beginTime", dateString);
		   dateString = formatter.format(sysBackstageTime.getEndTime());
		   model.addAttribute("endTimeShow", sysBackstageTime.getEndTime());
		   SysBackstageTime sysBackstageDate = service.selectByDisable();
		   sysBackstageDate.setBeginTime(new Date());
		   model.addAttribute("endTime", dateString);
		   if(sysBackstageDate.getSum()!=null){
			   Calendar cal = Calendar.getInstance(); 
			   cal.setTime(sysBackstageDate.getBeginTime());
			   cal.add(Calendar.HOUR, 24*Integer.parseInt(sysBackstageDate.getSum()));// 24小时制
			   model.addAttribute("endDate", data.format(cal.getTime()));
		   }
		   model.addAttribute("beginDate", data.format(sysBackstageDate.getBeginTime()));
		   model.addAttribute("redio", sysBackstageDate);
		model.addAttribute("cartype", car);
		model.addAttribute("officelist",officelist);
		return "/modules/swust/sysOrderAdd";
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
			json.setSuccess(true);
		order.setUserId(UserUtils.getUser().getId());
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
	
	@ResponseBody
	@RequestMapping(value="UserOrderOpen")
	public AjaxJson UserOrderOpen(SysOrderlist sysOrderlist,HttpServletResponse response,HttpServletRequest request) throws IOException {
		AjaxJson ajax = new AjaxJson();
		sysOrderlist.setUserId(UserUtils.getUser().getId());
		sysOrderlist.setState("0");
		Page<SysOrderlist> page = service.findPage(new Page<SysOrderlist>(request, response), sysOrderlist);
		if(page.getPageNo()==0){
			page.setPageNo(1);
		}
		ajax.setMsg(page.toString());
		return ajax;
	}
	
	/**
	 * 
	 * @Title: UserSelectOrderReason 
	 * @Description: Ajax查询事由
	 * @param id
	 * @param resp
	 * @throws IOException
	 * @author com.mhout.wzw
	 */
	@ResponseBody
	@RequestMapping(value="UserSelectOrderReason")
	public AjaxJson UserSelectOrderReason(Model model,SysOrderlist sysOrderlist, HttpServletResponse resp,HttpServletRequest request) throws IOException {
		AjaxJson ajax = new AjaxJson();
		StringBuilder sb = new StringBuilder();
		sb.append(sysOrderlist.getOrderReason());
		if(sysOrderlist.getRemarks()==null||sysOrderlist.getRemarks()==""){
			sysOrderlist.setRemarks("未填写备注");
		}
		sb.append(","+sysOrderlist.getRemarks());
		System.out.println(sb.toString());
		ajax.setMsg(sb.toString());
		return ajax;
	}
	/**
	 * 
	 * @Title: userSelectByState 
	 * @Description: 审核判定
	 * @param state
	 * @param req
	 * @param pageCode
	 * @param session
	 * @return
	 * @author com.mhout.wzw
	 */
	@RequestMapping(value="userSelectByState")
	public String userSelectByState(String state,HttpServletRequest request,HttpServletResponse response,String pageCode,Model model){
		SysOrderlist sysOrderlist = new SysOrderlist();
		sysOrderlist.setState(state);
		Page<SysOrderlist> page = service.findPage(new Page<SysOrderlist>(request, response), sysOrderlist); 
        model.addAttribute("page", page);
		return "/modules/swust/sysOrderlist";
	}
	/**
	 * 
	 * @Title: UserSelectPassword 
	 * @Description: 修改密码Ajax验证密码是否正确
	 * @param password
	 * @param resp
	 * @param req
	 * @param session
	 * @throws IOException
	 * @author com.mhout.wzw
	 */
		@ResponseBody
		@RequestMapping(value="UserSelectPassword")
		public AjaxJson UserSelectPassword(String oldPassword)
				throws IOException {
			AjaxJson ajax = new AjaxJson();
			User user = UserUtils.getUser();
			/*user = service.UserSelectPassword(user);*/
			if(user.getPassword().equals(MD5Utils.make(oldPassword))){
				ajax.setMsg("");
				ajax.setSuccess(true);
			}else{
				ajax.setMsg("密码有错误，请重新输入");
				ajax.setSuccess(false);
				
			}
			return ajax;
		}
		/**
		 * @Title: checkName 
		 * @Description: TODO
		 * @return
		 * @author com.mhout.xyb
		 */
		@ResponseBody
		@RequestMapping(value = "checkPwd", method = RequestMethod.POST)
		public String checkName( String oldPassword) {
			User user = UserUtils.getUser();
			if(user.getPassword().equals(MD5Utils.make(oldPassword))){
				return "true";
			}else{
				return "false";
			}
		}
		
		
		/**
		 * 
		 * @Title: updateUserByPassword 
		 * @Description: 修改密码提交
		 * @param password
		 * @param session
		 * @return
		 * @author com.mhout.wzw
		 */
		@ResponseBody
		@RequestMapping(value="updateUserByPassword")
		public AjaxJson updateUserByPassword(Model model,String oldPassword,String newPassword){
			AjaxJson ajax = new AjaxJson();
	        User user = UserUtils.getUser();
	        if (StringUtils.isNotBlank(oldPassword) && StringUtils.isNotBlank(newPassword)) {
	            String old = CommonService.encryption(oldPassword);
	            if (old.equals(user.getPassword())) {
	                service.updatePasswordById(user.getId(), user.getLoginName(), newPassword);
	                ajax.setMsg("修改密码成功");
	                ajax.setSuccess(true);
	            } else {
	            	ajax.setSuccess(false);
	            	ajax.setMsg("修改密码失败，原密码错误");
	            }
	        }
	        return ajax;
//	        return "modules/sys/accountSafe";
		}
		
		/**
		 * 
		 * @Title: updateUserMobile 
		 * @Description: 修改管理员手机号
		 * @param mobile
		 * @param session
		 * @return
		 * @author com.mhout.wzw
		 */
		@ResponseBody
		@RequestMapping(value="updateUserMobile")
		public AjaxJson updateAdminMobile(String mobile){
			AjaxJson ajax = new AjaxJson();
	        service.updateAdminMobile(mobile);
	        ajax.setMsg("号码修改成功");
            ajax.setSuccess(true);
	        return ajax;
//	        return "modules/sys/accountSafe";
		}
		
		
		
		
		
		@ResponseBody
		@RequestMapping(value="updateNewUserByPassword")
		public AjaxJson updateNewUserByPassword(Model model,String oldPassword,String newPassword){
			AjaxJson ajax = new AjaxJson();
	        User user = UserUtils.getUser();
	        if (StringUtils.isNotBlank(oldPassword) && StringUtils.isNotBlank(newPassword)) {
	                service.updatePasswordById(user.getId(), user.getLoginName(), newPassword);
	                ajax.setMsg("修改密码成功");
	                ajax.setSuccess(true);
	        }
	        return ajax;
//	        return "modules/sys/accountSafe";
		}
		@RequestMapping(value="Initialization")
		public String initialization(String state,HttpServletRequest request,HttpServletResponse response,String pageCode,Model model){
			SysPhotolist photo = service.initializationLogin();
			model.addAttribute("photo", photo);
			return "/modules/swust/initializationLogin";
		}
		
		@ResponseBody
		@RequestMapping(value="timeValidate")
		public AjaxJson timeValidate(Model model,Date time ,HttpServletRequest request){
			AjaxJson ajax = new AjaxJson();
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String start = request.getParameter("startTime");
			String finish = request.getParameter("finishTime");
			double startNum=0;
			double finishNum=0;
			try {
				startNum = sdf.parse(start).getTime()+18*60*60*1000;
				 finishNum= sdf.parse(finish).getTime();
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if(finishNum>startNum){
				ajax.setMsg("预约时长请在一天之内");
                ajax.setSuccess(true);
                return ajax;
			}
	        if (time!=null) {
	        	 String yearAndDay = sdf.format(new Date()).substring(0, 11);
	        	 double timeValidate = 0;
	        	 double beginTime = 0;
	        	 double endTime = 0;
	        	try {
					 timeValidate = sdf.parse(yearAndDay+sdf.format(time).substring(11)).getTime();
					beginTime = sdf.parse(yearAndDay+sdf.format(service.selectBackstageTime().getBeginTime()).substring(11)).getTime();
					endTime = sdf.parse(yearAndDay+sdf.format(service.selectBackstageTime().getEndTime()).substring(11)).getTime();
	        	} catch (ParseException e) {
					e.printStackTrace();
				}
	            if (timeValidate>endTime||timeValidate<beginTime) {
	                ajax.setMsg("请将预约时间设置在规定时间之内");
	                ajax.setSuccess(true);
	            }else{
	            	ajax.setSuccess(false);
	            	ajax.setMsg("");
	            }
	        }
	        return ajax;
		}
		
		
		@ResponseBody
		@RequestMapping(value="dateValidate")
		public AjaxJson DateValidate(Model model,Date time ,HttpServletRequest request){
			AjaxJson ajax = new AjaxJson();
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			String start = request.getParameter("beginDate");
			String finish = request.getParameter("endDate");
			double startNum=0;
			double finishNum=0;
			try {
				startNum = sdf.parse(start).getTime();
				 finishNum= sdf.parse(finish).getTime();
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        if (time!=null) {
	        		double chooseTime = time.getTime();
	        		if(chooseTime>finishNum||chooseTime<startNum){
	        			 ajax.setMsg("请将预约时间设置在规定时间之内");
	 	                ajax.setSuccess(true);
	        		}else{
	                ajax.setMsg("");
	                ajax.setSuccess(false);
	        		}
	        }
	        return ajax;
		}
		
		
		@ResponseBody
		@RequestMapping(value = {"batchDelete"}, method = RequestMethod.POST)
		public AjaxJson batchPass(@RequestParam(value = "ids[]")String[] ids) {
			AjaxJson ajaxJson = new AjaxJson();
			service.batchDelete(ids);
			ajaxJson.setSuccess(true);
			ajaxJson.setMsg("操作成功");
			return ajaxJson;
		}
		
		public String checkOffice(String name, String loginname, String oldname) {
			String exist = "true";
			//true表示不存在：可以添加
			//校验单位名称
			if (StringUtils.isNotBlank(name)) {
				if (!name.equals(oldname)) {
					Office office = officeDao.findByName(name);
					exist = office != null ? "false":"true";
				}
			} 
			return exist;
		}
}










