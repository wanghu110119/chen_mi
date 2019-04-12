package com.mht.modules.ident.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mht.common.config.Global;
import com.mht.common.persistence.Page;
import com.mht.common.utils.StringUtils;
import com.mht.common.web.BaseController;
import com.mht.modules.ident.entity.SysProject;
import com.mht.modules.ident.service.SysProjectService;

/**
 * @ClassName: SysProjectController
 * @Description: 项目管理控制层
 * @author com.mhout.xyb
 * @date 2017年5月4日 上午10:37:53 
 * @version 1.0.0
 */
@Controller
@RequestMapping(value = "${adminPath}/ident/sysProject")
public class SysProjectController extends BaseController {
	
	@Autowired
	private SysProjectService sysProjectService;
	
	@ModelAttribute("sysProject")
	public SysProject get(@RequestParam(required = false) String id ){
		if (StringUtils.isNotBlank(id)){
			return sysProjectService.get(id);
		} else {
			return new SysProject();
		}
	}
	
	/**
	 * @Title: list 
	 * @Description: 项目管理列表
	 * @param sysProject
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @author com.mhout.xyb
	 */
	@RequiresPermissions("ident:sysProject:list")
	@RequestMapping(value = {"list", ""})
	public String list(SysProject sysProject, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SysProject> page = sysProjectService.find(new Page<SysProject>(request, response), sysProject);
		model.addAttribute("page", page);
		return "modules/ident/identSysProjectList";
	}
	
	
	/**
	 * @Title: form 
	 * @Description: 项目查看，增加，编辑报告表单页面
	 * @param application
	 * @param model
	 * @return
	 * @author com.mhout.xyb
	 */
	@RequiresPermissions(value={"ident:sysProject:view","ident:sysProject:add","ident:sysProject:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(SysProject sysProject, Model model) {
		model.addAttribute("sysProject", sysProject);
		return "modules/ident/identSysProjectForm";
	}
	
	/**
	 * @Title: save 
	 * @Description: 保存或者编辑应用
	 * @param application
	 * @param model
	 * @param redirectAttributes
	 * @return
	 * @author com.mhout.xyb
	 */
	@RequiresPermissions(value={"ident:sysProject:add","ident:sysProject:edit"},logical=Logical.OR)
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public String save(SysProject sysProject, Model model,
			MultipartFile file, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, sysProject)){
			return form(sysProject, model);
		}
		sysProjectService.save(sysProject);
		addMessage(redirectAttributes, "保存项目'" + sysProject.getName() + "'成功");
		return "redirect:" + adminPath + "/ident/sysProject/?repage";
	}
	
	/**
	 * @Title: deleteAll 
	 * @Description: 批量删除
	 * @param ids
	 * @param redirectAttributes
	 * @return
	 * @author com.mhout.xyb
	 */
	@RequiresPermissions("ident:sysProject:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split("\\,");
		for(String id : idArray){
			SysProject sysProject = sysProjectService.get(id);
			sysProjectService.deleteSysProject(sysProject);
		}
		addMessage(redirectAttributes, "删除项目成功");
		return "redirect:" + adminPath + "/ident/sysProject/?repage";
	}
	
	/**
	 * @Title: enable 
	 * @Description: 启用
	 * @param application
	 * @param redirectAttributes
	 * @return
	 * @author com.mhout.xyb
	 */
	@RequiresPermissions("ident:sysProject:enable")
	@RequestMapping(value = "enable")
	public String enable(SysProject sysProject, RedirectAttributes redirectAttributes) {
		sysProject.setStatus(Global.ENABLE);
		sysProjectService.save(sysProject);
		addMessage(redirectAttributes, "启用项目成功");
		return "redirect:" + adminPath + "/ident/sysProject/?repage";
	}
	
	/**
	 * @Title: disable 
	 * @Description: 禁用
	 * @param application
	 * @param redirectAttributes
	 * @return
	 * @author com.mhout.xyb
	 */
	@RequiresPermissions("ident:sysProject:disable")
	@RequestMapping(value = "disable")
	public String disable(SysProject sysProject, RedirectAttributes redirectAttributes) {
		sysProject.setStatus(Global.DISABLE);
		sysProjectService.save(sysProject);
		addMessage(redirectAttributes, "禁用项目成功");
		return "redirect:" + adminPath + "/ident/sysProject/?repage";
	}
	
}
