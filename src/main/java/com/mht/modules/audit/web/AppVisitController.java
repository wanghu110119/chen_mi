package com.mht.modules.audit.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mht.common.persistence.Page;
import com.mht.common.utils.StringUtils;
import com.mht.common.web.BaseController;
import com.mht.modules.audit.entity.AppUserRecordData;
import com.mht.modules.audit.entity.AppUserRecordList;
import com.mht.modules.audit.service.AppVisitService;
import com.mht.modules.ident.entity.AppUserRecord;
import com.mht.modules.sys.entity.Office;

/**
 * @ClassName: AppVisitController
 * @Description: 应用访问统计
 * @author com.mhout.xyb
 * @date 2017年4月25日 上午10:08:46
 * @version 1.0.0
 */
@Controller
@RequestMapping(value = "${adminPath}/audit/appvisit")
public class AppVisitController  extends BaseController {

	@Autowired
	AppVisitService appVisitService;
	
	@ModelAttribute("appUserRecord")
	public AppUserRecord get(@RequestParam(required = false) String id ){
		if (StringUtils.isNotBlank(id)){
			return appVisitService.get(id);
		} else {
			return new AppUserRecord();
		}
	}

	/**
	 * @Title: index
	 * @Description: 跳转应用访问主页
	 * @return
	 * @author com.mhout.xyb
	 */
	@RequiresPermissions("audit:appvisit:list")
	@RequestMapping(value = { "list", "" })
	public String list(AppUserRecord appUserRecord, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		Page<AppUserRecord> page = appVisitService.findPage(new Page<AppUserRecord>(request, response), appUserRecord);
		model.addAttribute("page", page);
		return "modules/audit/appvisit";
	}
	
	
	/**
	 * @Title: getVisitStatistics 
	 * @Description: 访问统计图
	 * @param appUserRecord
	 * @return
	 * @author com.mhout.xyb
	 */
	@ResponseBody
	@RequiresPermissions("audit:appvisit:list")
	@RequestMapping(value = { "getVisitStatistics"})
	public AppUserRecordList getVisitStatistics(String type, String grade) {
		Office office = new Office();
		if (StringUtils.isNotBlank(grade)) {
			office.setGrade(grade);
		} else {
			office = null;
		}
		return this.appVisitService.findVisitStatistics(type, office);
	}
	
	/**
	 * @Title: getVisitAmount 
	 * @Description: 访问量TOP10
	 * @return
	 * @author com.mhout.xyb
	 */
	@ResponseBody
	@RequiresPermissions("audit:appvisit:list")
	@RequestMapping(value = { "getVisitAmount"})
	public List<AppUserRecordData> getVisitAmount(String grade) {
		return this.appVisitService.findVisitAmount(grade);
	}
	
	/**
	 * @Title: getVisitUser 
	 * @Description: 用户占比TOP10
	 * @return
	 * @author com.mhout.xyb
	 */
	@ResponseBody
	@RequiresPermissions("audit:appvisit:list")
	@RequestMapping(value = { "getVisitUser"})
	public List<AppUserRecordData> getVisitUser(String grade) {
		return this.appVisitService.findVisitUser(grade);
	}
	
	/**
	 * @Title: getVisitTrend 
	 * @Description: 应用访问趋势TOP10
	 * @return
	 * @author com.mhout.xyb
	 */
	@ResponseBody
	@RequiresPermissions("audit:appvisit:list")
	@RequestMapping(value = { "getVisitTrend"})
	public List<AppUserRecordData> getVisitTrend(String grade) {
		return this.appVisitService.findVisitTrend(grade);
	}
	
	/**
	 * @Title: getVisitHistory 
	 * @Description: 访问历史总览
	 * @return
	 * @author com.mhout.xyb
	 */
	@ResponseBody
	@RequiresPermissions("audit:appvisit:list")
	@RequestMapping(value = { "getVisitHistory"})
	public List<AppUserRecordData> getVisitHistory() {
		return this.appVisitService.findVisitHistory();
	}

}
