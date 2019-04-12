package com.mht.modules.audit.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mht.modules.audit.entity.RealTimeData;
import com.mht.modules.audit.service.AppVisitService;
import com.mht.modules.ident.entity.AppUserRecord;
import com.mht.modules.ident.entity.Application;
import com.mht.modules.ident.service.ApplicationService;
import com.mht.modules.sys.entity.Office;
import com.mht.modules.sys.service.OfficeService;
import com.mysql.fabric.xmlrpc.base.Array;

/**
 * @ClassName: RealTimeController
 * @Description: 实时访问统计
 * @author com.mhout.xyb
 * @date 2017年5月9日 上午10:39:03 
 * @version 1.0.0
 */
@Controller
@RequestMapping(value = "${adminPath}/audit/realtime")
public class RealTimeController {
	
	@Autowired
	private ApplicationService applicationService;
	
	@Autowired
	private OfficeService officeService;
	
	@Autowired
	private AppVisitService appVisitService;
	
	/**
	 * @Title: list 
	 * @Description: 跳转页面
	 * @param appUserRecord
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @author com.mhout.xyb
	 */
	@RequiresPermissions("audit:realtime:list")
	@RequestMapping(value = { "list", "" })
	public String list(AppUserRecord appUserRecord, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		List<Application> list = applicationService.findAllSystemApps();
		model.addAttribute("apps", list);
		return "modules/audit/realtimeApp";
	}
	
	/**
	 * @Title: getOffices 
	 * @Description: 获取院系信息
	 * @return
	 * @author com.mhout.xyb
	 */
	@ResponseBody
	@RequiresPermissions("audit:realtime:list")
	@RequestMapping(value = { "getOffices" })
	public List<Office> getOffices() {
		Office office = new Office();
		office.setGrade("2");
		return officeService.findAllOffices(office);
	}
	
	/**
	 * @Title: getRealTime 
	 * @Description: 初始化实时数据
	 * @param id
	 * @return
	 * @author com.mhout.xyb
	 */
	@ResponseBody
	@RequiresPermissions("audit:realtime:list")
	@RequestMapping(value = { "getRealTime" })
	public List<RealTimeData> getRealTime(String id, String type, String oid) {
		List<RealTimeData> list = null;
		if (StringUtils.isNotBlank(id)) {
			list = appVisitService.findRealTime(id, type);
		} else {
			list = appVisitService.findOfficeRealTime(oid, type);
		}
		return list;
	}
	
	
	@ResponseBody
	@RequiresPermissions("audit:realtime:list")
	@RequestMapping(value = { "getTimerData" })
	public List<RealTimeData> getTimerData(String id, String type, String oid) {
		List<RealTimeData> list = null;
		if (StringUtils.isNotBlank(id)) {
			list = appVisitService.findAppTimer(id, type);
		} else {
			list = appVisitService.findOfficeTimer(oid, type);
		}
		return list;
	}
}
