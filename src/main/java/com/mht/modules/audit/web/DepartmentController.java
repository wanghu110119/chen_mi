package com.mht.modules.audit.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mht.common.persistence.Page;
import com.mht.common.utils.StringUtils;
import com.mht.common.web.BaseController;
import com.mht.modules.audit.service.AppVisitService;
import com.mht.modules.ident.entity.AppUserRecord;

/**
 * @ClassName: DepartmentController
 * @Description: 院级应用统计
 * @author com.mhout.xyb
 * @date 2017年4月25日 上午11:23:08 
 * @version 1.0.0
 */
@Controller
@RequestMapping(value = "${adminPath}/audit/department")
public class DepartmentController extends BaseController {
	
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
	 * @Description: 跳转统计页
	 * @return
	 * @author com.mhout.xyb
	 */
	@RequiresPermissions("audit:department:list")
	@RequestMapping(value = { "list", "" })
	public String index(AppUserRecord appUserRecord, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		Page<AppUserRecord> page = appVisitService.findPage(new Page<AppUserRecord>(request, response), appUserRecord);
		model.addAttribute("page", page);
		return "modules/audit/department";
	}
}
