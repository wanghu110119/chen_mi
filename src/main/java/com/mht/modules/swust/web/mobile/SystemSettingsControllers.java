package com.mht.modules.swust.web.mobile;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.mht.common.json.AjaxJson;
import com.mht.common.utils.StringUtils;
import com.mht.common.web.BaseController;
import com.mht.modules.swust.entity.PageBean;
import com.mht.modules.swust.entity.SysBackstageTime;
import com.mht.modules.swust.entity.SysCarMoney;
import com.mht.modules.swust.entity.SysMessage;
import com.mht.modules.swust.entity.SysPhotolist;
import com.mht.modules.swust.service.BackstageTimeService;
import com.mht.modules.swust.service.CarMOneyService;
import com.mht.modules.swust.service.impl.OrderUserServiceImpl;
import com.mht.modules.swust.service.impl.SystemSettingsServiceImpl;
import com.mht.modules.swust.utils.ConstantUtil;
import com.mht.modules.swust.utils.PhotoUtils;
import com.mht.modules.sys.dao.UserDao;
import com.mht.modules.sys.entity.User;
import com.mht.modules.sys.utils.UserUtils;

/**
 * 
 * @ClassName: SystemSettingsController
 * @Description: 系统设置上传文件
 * @author com.mhout.wzw
 * @date 2017年7月26日 下午4:23:51 
 * @version 1.0.0
 */
@Controller
@RequestMapping(value = "mobile/swust/system")
public class SystemSettingsControllers extends BaseController{

	@Autowired
	SystemSettingsServiceImpl service;
	
	@Autowired
	CarMOneyService moneyService;
	
	@Autowired
	BackstageTimeService  timeService;
	
	@Autowired
	OrderUserServiceImpl orderService;
	
	@Autowired
	OrderUserServiceImpl userService;
	 @Autowired
	    private UserDao userDao;
	
	@ModelAttribute("sysPhotolist")
	public SysPhotolist get(@RequestParam(required = false) String id ){
		if (StringUtils.isNotBlank(id)){
			return service.get(id);
		} else {
			return new SysPhotolist();
		}
	}
	
	/**
	 * 
	 * @Title: insertUserPhoto 
	 * @Description: 添加图片上传
	 * @param password
	 * @param session
	 * @param photo
	 * @return
	 * @author com.mhout.xyb
	 */
	@ResponseBody
	@RequestMapping(value="insertUserPhoto",method = RequestMethod.POST)
public AjaxJson insertUserPhoto(SysPhotolist sysPhotolist,HttpServletRequest request,
		HttpServletResponse response,MultipartFile photo ){
		AjaxJson ajax = new AjaxJson();
	String last = PhotoUtils.getPhotoType(photo).toUpperCase();
	if(last.equals("JPG")||last.equals("PNG")||last.equals("JPEG")){
		sysPhotolist.setPhotoType("."+last);
		sysPhotolist.setState("0");
		String path = request.getServletContext().getRealPath("/")+"/static/upload";
		User user = UserUtils.getUser();
		service.updateByPhoto(sysPhotolist,user,path,photo);
		return ajax;
	}else{
			ajax.setSuccess(false);
		return ajax;
	}
	}
/**
 * 
 * @Title: SelectAllByPhoto 
 * @Description: 页面打开访问查询
 * @param password
 * @param session
 * @param photo
 * @return
 * @author com.mhout.wzw
 */

@RequestMapping(value={"","system"})
public String SelectAllByPhoto(Model model, String password){
	User user = UserUtils.getUser();
	List<SysPhotolist> list = service.SelectAllByPhoto(user);
	SysPhotolist sysPhotolist = new SysPhotolist();
	sysPhotolist.setState("1");
	SysPhotolist defaultphoto = service.selectPhotoByDefaultKey(sysPhotolist);
	List<SysCarMoney> moneylist = moneyService.selectAll();
	SysBackstageTime sysBackstageTime = orderService.selectBackstageTime();
	timeService.save(sysBackstageTime);
	SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
	   String dateString = formatter.format(sysBackstageTime.getBeginTime());
	   model.addAttribute("beginTime", dateString);
	   dateString = formatter.format(sysBackstageTime.getEndTime());
	   model.addAttribute("endTime", dateString);
	   model.addAttribute("orderDay", sysBackstageTime.getRemarks());
	List<SysMessage> messageList = orderService.selectAllMessage();
	SysBackstageTime ent = orderService.selectByRemark();
	SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd");
	model.addAttribute("redio", ent);
	model.addAttribute("beginDate", sdf1.format(ent.getBeginTime()));
	model.addAttribute("endDate", sdf1.format(ent.getEndTime()));
	model.addAttribute("user", UserUtils.getUser());
	model.addAttribute("message", messageList.get(0));
	model.addAttribute("carList",moneylist);
	model.addAttribute("photolist", list);
	model.addAttribute("photo", defaultphoto);
	return "modules/swust/mobile/backstage";
}
@ResponseBody
@RequestMapping(value="changepicture",method = RequestMethod.POST)
public AjaxJson changepicture( String id){
	AjaxJson ajax = new AjaxJson();
	try{
		service.changepicture(id);
	}catch (Exception e) {
		ajax.setMsg("不好意思出错了");

	}
	return ajax;
}
@ResponseBody
@RequestMapping(value="changeDate",method = RequestMethod.POST)
public void changeDate( String beginDate){
	SysBackstageTime ent = timeService.get("2");
	ent.setSum(beginDate);
	timeService.save(ent);
}

@ResponseBody
@RequestMapping(value="changeRedio",method = RequestMethod.POST)
public void changeRedio( SysBackstageTime sysBackstageTime){
	sysBackstageTime.setDisable("1");
	timeService.save(sysBackstageTime);
	timeService.notEqualsId(sysBackstageTime.getId());
}


/**
 * 
 * @Title: changeMoney 
 * @Description: TODO
 * @param money
 * @author com.mhout.wzw
 */
@ResponseBody
@RequestMapping(value="changeMoney")
public void changeMoney(SysCarMoney money){
	SysCarMoney carMoney = moneyService.get(money.getId());
	carMoney.setDefaultTime(money.getDefaultTime());
	carMoney.setAddMoney(money.getAddMoney());
	carMoney.setMaxMoney(money.getMaxMoney());
	carMoney.setMoneyMonth(money.getMoneyMonth());
	moneyService.save(carMoney);
}
@ResponseBody
@RequestMapping(value="changeTime")
public void changeTime(String beginTime,String endTime ,String day){
	if(day==null||"".equals(day)){
		day=ConstantUtil.default_pageCode;
	}
	double dayNum = Double.parseDouble(day);
	SysBackstageTime sysBackstageTime = timeService.get("1");
	Calendar calendar = Calendar.getInstance();
	 calendar.setTime( new Date());
	 calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 1);
	 Date tomorrow = calendar.getTime();
	 SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
	 String str=sdf.format(tomorrow); 
	 endTime = str.substring(0, 11)+endTime;
	 beginTime = str.substring(0, 11)+beginTime;
	 SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//小写的mm表示的是分钟  
	 Date beginDate = new Date();
	 Date endDate = new Date();
	try {
		beginDate = format.parse(beginTime);
		endDate = format.parse(endTime);
		double endIndex = (double)endDate.getTime()+(dayNum-1)*24*60*60*1000;
		endDate = format.parse(format.format(endIndex));
	} catch (ParseException e) {
		e.printStackTrace();
	} 
	sysBackstageTime.setBeginTime(beginDate);
	sysBackstageTime.setEndTime(endDate);
	sysBackstageTime.setRemarks(String.valueOf((int)dayNum));
	timeService.save(sysBackstageTime);
}
/**
 * 后台设置限定时间在几天之内
 * @param changeDay
 */
@ResponseBody
@RequestMapping(value="changeDay")
public void changeDay(double changeDay){
	SysBackstageTime sysBackstageTime = orderService.selectBackstageTime();
	   double dateString = (double)sysBackstageTime.getBeginTime().getTime()+(changeDay-1)*24*60*60*1000-1000;
	   SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	   String d = format.format(dateString);  
	    Date date = new Date();
		try {
			date = format.parse(d);
		} catch (ParseException e) {
			e.printStackTrace();
		}  
	   sysBackstageTime.setEndTime(date);
	   sysBackstageTime.setRemarks(String.valueOf((int)changeDay));
	timeService.save(sysBackstageTime);
}


@ResponseBody
@RequestMapping(value="changeMessage")
public void changeMessage(String message){
	orderService.changeMessage(message);
}
@ResponseBody
@RequestMapping(value="deletepicture")
public void deletepicture(String id ){
	service.deletepicture( id );
}

/**
 * 
 * @Title: changeHeadPhoto 
 * @Description: TODO 修改密码
 * @param sysPhotolist
 * @param request
 * @param response
 * @param photo
 * @author com.mhout.wzw
 */

@ResponseBody
@RequestMapping(value="changeheadphoto",method = RequestMethod.POST)
public AjaxJson changeHeadPhoto(SysPhotolist sysPhotolist,HttpServletRequest request,
	HttpServletResponse response,MultipartFile photo ){
	AjaxJson json = new AjaxJson();
String last = PhotoUtils.getPhotoType(photo).toUpperCase();
if(last.equals("JPG")||last.equals("PNG")||last.equals("JPEG")){
	sysPhotolist.setPhotoType("."+last);
	sysPhotolist.setState("0");
	String path = request.getServletContext().getRealPath("static/upload");
	User user = UserUtils.getUser();
	service.changeHeadPhoto(sysPhotolist,user,path,photo);
}else{
		json.setMsg("请上传正确的图片格式");
}
	return json;
}
@ResponseBody
@RequestMapping(value="changename")
public AjaxJson changeName(String name ){
	AjaxJson json = new AjaxJson();
	User user = UserUtils.getUser();
	user.setName(name);
	userDao.update(user);
	UserUtils.clearCache(user);
	json.setMsg(name);
	return json;
}


}





















