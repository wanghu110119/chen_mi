package com.mht.modules.swust.web;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mht.common.json.AjaxJson;
import com.mht.common.persistence.Page;
import com.mht.common.utils.IdGen;
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
@RequestMapping(value = "a/swust/meeting")
public class CmsMeetingController extends BaseController{
	
	
	
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
	
	@RequestMapping(value="init")
	public String init(Model model ,HttpServletRequest request, HttpServletResponse response,SysOrderlist sysOrderlist){
		List<Office> officelist = service.findOfficeList();
		sysOrderlist.setQrCodeImage("notNull");
		Page<SysOrderlist> page = service.findPage(new Page<SysOrderlist>(request, response), sysOrderlist);
		model.addAttribute("page",page);
		model.addAttribute("officelist",officelist);
		return "modules/swust/qrcode";
	}
	@RequestMapping(value="select")
	public String select(Model model ,HttpServletRequest request, HttpServletResponse response,SysOrderlist sysOrderlist){
		List<Office> officelist = service.findOfficeList();
		sysOrderlist.setQrCodeImage("notNull");
		Page<SysOrderlist> page = service.findPage(new Page<SysOrderlist>(request, response), sysOrderlist);
		model.addAttribute("page",page);
		model.addAttribute("officelist",officelist);
		return "modules/swust/qrcodeList";
	}
	
	
	
	@RequestMapping(value="add")
	public String insertOrder(Model model, SysOrderlist sysOrderList){
		List<Office> officelist = service.findOfficeList();
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
		   model.addAttribute("sysOrderList", sysOrderList);
		   model.addAttribute("redio", sysBackstageDate);
		model.addAttribute("cartype", car);
		model.addAttribute("officelist",officelist);
		return "modules/swust/sysOrderAdd";
	}
	
	@ResponseBody
	@RequestMapping(value = {"batchDelete"}, method = RequestMethod.POST)
	public AjaxJson batchPass(@RequestParam(value = "ids")String[] ids) {
		AjaxJson ajaxJson = new AjaxJson();
		service.batchDelete(ids);
		ajaxJson.setSuccess(true);
		ajaxJson.setMsg("操作成功");
		return ajaxJson;
	}
	@ResponseBody
	@RequestMapping(value = {"createQrCode"}, method = RequestMethod.POST)
	public AjaxJson createQrCode(SysOrderlist sysOrderList ,HttpServletRequest request) {
		Calendar nowTime = Calendar.getInstance();
		Date time = sysOrderList.getEndTime();
		nowTime.setTime(time);
		nowTime.add(Calendar.MINUTE, 59);
		nowTime.add(Calendar.HOUR, 23);
		sysOrderList.setEndTime(nowTime.getTime());
		AjaxJson ajaxJson = new AjaxJson();
		sysOrderList = QRCodeCreate.createQRCode(sysOrderList,request);
		ajaxJson.put("qrCode", sysOrderList.getQrCodeAddress());
		ajaxJson.setSuccess(true);
		ajaxJson.setMsg("操作成功");
		return ajaxJson;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
