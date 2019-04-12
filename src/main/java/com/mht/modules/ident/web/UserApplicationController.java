package com.mht.modules.ident.web;

import java.io.IOException;
import java.util.List;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mht.common.config.Global;
import com.mht.common.json.AjaxJson;
import com.mht.common.persistence.Page;
import com.mht.common.utils.StringUtils;
import com.mht.common.web.BaseController;
import com.mht.modules.ident.entity.Application;
import com.mht.modules.ident.service.ApplicationService;
import com.mht.modules.ident.service.AutRecordService;
import com.mht.modules.sys.entity.User;
import com.mht.modules.sys.utils.UserUtils;

@Controller
@RequestMapping(value = "${adminPath}/ident/userapplication")
public class UserApplicationController extends BaseController {
	
	@Autowired
	private ApplicationService applicationService;
	
	@Autowired
	private AutRecordService autRecordService;
	
	@ModelAttribute("application")
	public Application get(@RequestParam(required = false) String id ){
		if (StringUtils.isNotBlank(id)){
			return applicationService.get(id);
		} else {
			return new Application();
		}
	}

	/**
	 * @Title: list 
	 * @Description: 应用管理列表
	 * @param application
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @author com.mhout.xyb
	 */
	@RequiresPermissions("ident:userapplication:list")
	@RequestMapping(value = {"list", ""})
	public String list(Application application, HttpServletRequest request, HttpServletResponse response, Model model) {
		application.setType(Global.USERDEFINED);
		User user = UserUtils.getUser();
		String isAble = "1";
		if (!user.isAdmin()) {
			application.setCreateBy(user);
			application.setStatus(Global.ENABLE);
			isAble = "2";
		}
		Page<Application> page = applicationService.find(new Page<Application>(request, response), application);
		model.addAttribute("page", page);
		model.addAttribute("isable", isAble);
		return "modules/ident/identUserApplicationList";
	}
	
	/**
	 * @Title: form 
	 * @Description: 应用查看，增加，编辑报告表单页面
	 * @param application
	 * @param model
	 * @return
	 * @author com.mhout.xyb
	 */
	@RequiresPermissions(value={"ident:userapplication:view","ident:userapplication:add","ident:userapplication:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Application application,String modifyType, Model model) {
		if (!Global.USERDEFINED.equals(application.getType())) {
			application = new Application();
		}
		model.addAttribute("application", application);
		model.addAttribute("modifyType", StringUtils.isNotBlank(modifyType)?modifyType:"1");
		return "modules/ident/identUserApplicationForm";
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
	@RequiresPermissions(value={"ident:userapplication:add","ident:userapplication:edit"},logical=Logical.OR)
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public String save(Application application, String modifyType, Model model,
			MultipartFile file, RedirectAttributes redirectAttributes) {
		if(!applicationService.checkCustomApp(Global.APP)){
			addMessage(redirectAttributes, "越权操作，只有系统管理员才能添加或修改数据！");
			return "redirect:" + adminPath + "/ident/userapplication/?repage";
		}
		if (!beanValidator(model, application)){
			return form(application, "1", model);
		}
		try {
			application.setType(Global.USERDEFINED);
			application.setStatus(Global.ENABLE);
			applicationService.saveAppl(application, file);
		} catch (IOException e) {
			addMessage(redirectAttributes, "保存应用失败！失败信息："+e.getMessage());
		}
		addMessage(redirectAttributes, "保存应用'" + application.getName() + "'成功");
		if (StringUtils.isNotBlank(modifyType) && "2".equals(modifyType)) {
			return "redirect:" + adminPath + "/ident/userapplication/card?repage";
		}
		return "redirect:" + adminPath + "/ident/userapplication/?repage";
	}
	
	/**
	 * @Title: delete 
	 * @Description: 单个删除
	 * @param apply
	 * @param redirectAttributes
	 * @return
	 * @author com.mhout.xyb
	 */
	@RequiresPermissions("ident:userapplication:del")
	@RequestMapping(value = "delete")
	public String delete(Application application, RedirectAttributes redirectAttributes) {
		if (Global.USERDEFINED.equals(application.getType())) {
			applicationService.delete(application);
			addMessage(redirectAttributes, "删除应用成功");
		} else {
			addMessage(redirectAttributes, "删除应用失败");
		}
		return "redirect:" + adminPath + "/ident/userapplication/?repage";
	}
	
	/**
	 * @Title: deleteAll 
	 * @Description: 批量删除
	 * @param ids
	 * @param redirectAttributes
	 * @return
	 * @author com.mhout.xyb
	 */
	@RequiresPermissions("ident:userapplication:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split("\\,");
		for(String id : idArray){
			Application application = applicationService.get(id);
			if (Global.USERDEFINED.equals(application.getType())) {
				applicationService.delete(application);
			}
			applicationService.delete(applicationService.get(id));
		}
		addMessage(redirectAttributes, "删除应用成功");
		return "redirect:" + adminPath + "/ident/userapplication/?repage";
	}
	
	/**
	 * @Title: deleteByLogic 
	 * @Description: 逻辑删除
	 * @param ids
	 * @param redirectAttributes
	 * @return
	 * @author com.mhout.xyb
	 */
	@RequiresPermissions("ident:userapplication:del")
	@RequestMapping(value = "deleteByLogic")
	public String deleteByLogic(String ids, String modifyType, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split("\\,");
		for(String id : idArray){
			Application application = applicationService.get(id);
			if (Global.USERDEFINED.equals(application.getType())) {
				applicationService.deleteApplication(application);
			}
		}
		addMessage(redirectAttributes, "删除应用成功");
		if (StringUtils.isNotBlank(modifyType)) {
			return "redirect:" + adminPath + "/ident/userapplication/card?repage";
		}
		return "redirect:" + adminPath + "/ident/userapplication/?repage";
	}
	
	/**
	 * @Title: deleteAllByLogic 
	 * @Description: 逻辑删除(ajax)
	 * @param ids
	 * @param redirectAttributes
	 * @return
	 * @author com.mhout.xyb
	 */
	@ResponseBody
	@RequiresPermissions("ident:userapplication:del")
	@RequestMapping(value = "deleteAllByLogic")
	public AjaxJson deleteAllByLogic(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split("\\,");
		for(String id : idArray){
			Application application = applicationService.get(id);
			if (Global.USERDEFINED.equals(application.getType())) {
				applicationService.deleteApplication(application);
			}
		}
		AjaxJson json = new AjaxJson();
    	json.setSuccess(true);
    	json.setCode("0");
    	json.setMsg("操作成功");
		return json;
	}
	
	/**
	 * @Title: list 
	 * @Description: 卡片列表
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @author com.mhout.wj
	 */
    @RequiresPermissions("ident:userapplication:card")
    @RequestMapping(value = {"card"})
    public String list(HttpServletRequest request, HttpServletResponse response, Model model) {
        User userId = UserUtils.getUser();
        List<Application> sysApps = applicationService.findAllSystemApps();
        List<Application> selfApps = applicationService.findByUserAndType(userId.getId(), Global.USERDEFINED);
        model.addAttribute("system", sysApps);
        model.addAttribute("self", selfApps);
        model.addAttribute("sysSize", sysApps.size());
        model.addAttribute("selfSize", selfApps.size());
        return "modules/ident/myApp";
    }
    
    
    /**
	 * @Title: system 
	 * @Description: 添加为通用
	 * @param ids
	 * @param redirectAttributes
	 * @return
	 * @author com.mhout.xyb
	 */
	@RequiresPermissions("ident:userapplication:system")
	@RequestMapping(value = "system")
	public String add2SystemApp(Application application, RedirectAttributes redirectAttributes) {
		User user = UserUtils.getUser();
		if (user.isAdmin()) {
			String value = applicationService.checkName(application, null);
			if ("false".equals(value)) {
				application.setType(Global.SYSTEM);
				applicationService.save(application);
				addMessage(redirectAttributes, "操作成功！");
			}
			addMessage(redirectAttributes, "操作失败,该应用名称已经在系统应用已经存在！");
		} else {
			addMessage(redirectAttributes, "操作失败！");
		}
		return "redirect:" + adminPath + "/ident/userapplication/?repage";
	}
    
	
    /**
	 * @Title: list 
	 * @Description: 卡片排序
	 * @param ids
	 * @param redirectAttributes
	 * @return
	 * @author com.mhout.wj
	 */
    @RequiresPermissions("ident:userapplication:order")
	@RequestMapping(value = "order")
    @ResponseBody
	public AjaxJson order(String ids, RedirectAttributes redirectAttributes) {
    	String[] id = StringUtils.split(ids, "\\,");
		for (int i=1; i<id.length+1; i++) {
			Application app = applicationService.get(id[i-1]);
			app.setSort(i);
			applicationService.save(app);
		}
    	AjaxJson json = new AjaxJson();
    	json.setSuccess(true);
    	json.setCode("0");
    	json.setMsg("操作成功");
		return json;
	}
    
    /**
     * @Title: record 
     * @Description: 访问记录
     * @param id
     * @author com.mhout.xyb
     */
    @ResponseBody
    @RequiresPermissions("ident:userapplication:card")
  	@RequestMapping(value = "record")
    public AjaxJson record(Application application) {
    	autRecordService.record(application);
    	AjaxJson json = new AjaxJson();
    	json.setSuccess(true);
    	json.setCode("0");
    	json.setMsg("操作成功");
		return json;
    }
}
