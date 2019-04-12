package com.mht.modules.ident.web;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
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
import com.mht.common.json.ApplicationAuthDto;
import com.mht.common.persistence.Page;
import com.mht.common.utils.FileUtils;
import com.mht.common.utils.StringUtils;
import com.mht.common.web.BaseController;
import com.mht.modules.ident.entity.Application;
import com.mht.modules.ident.service.ApplicationService;
import com.mht.modules.sys.entity.User;
import com.mht.modules.sys.utils.UserUtils;
import com.mht.modules.unifiedauth.entity.AuthIdentityGroup;
import com.mht.modules.unifiedauth.entity.AuthOffice;
import com.mht.modules.unifiedauth.entity.AuthRole;
import com.mht.modules.unifiedauth.entity.AuthUser;
import com.mht.modules.unifiedauth.service.AuthIdentityGroupService;
import com.mht.modules.unifiedauth.service.AuthOfficeService;
import com.mht.modules.unifiedauth.service.AuthRoleService;
import com.mht.modules.unifiedauth.service.AuthUserService;

/**
 * @ClassName: ApplicationController
 * @Description: 
 * @author com.mhout.xyb
 * @date 2017年3月29日 下午4:11:16 
 * @version 1.0.0
 */
@Controller
@RequestMapping(value = "${adminPath}/ident/application")
public class ApplicationController extends BaseController {

	@Autowired
	private ApplicationService applicationService;
	
	@Autowired
    private AuthOfficeService authOfficeService;

    @Autowired
    private AuthUserService authUserService;

    @Autowired
    private AuthIdentityGroupService authIdentityGroupService;

    @Autowired
    private AuthRoleService authRoleService;
	
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
	@RequiresPermissions("ident:application:list")
	@RequestMapping(value = {"list", ""})
	public String list(Application application, HttpServletRequest request, HttpServletResponse response, Model model) {
		application.setType(Global.SYSTEM);
//		Page<Application> page = applicationService.find(new Page<Application>(request, response), application);
		Page<Application> page = applicationService.findSys(new Page<Application>(request, response), application);
		model.addAttribute("page", page);
		return "modules/ident/identApplicationList";
	}
	
	/**
	 * @Title: form 
	 * @Description: 应用查看，增加，编辑报告表单页面
	 * @param application
	 * @param model
	 * @return
	 * @author com.mhout.xyb
	 */
	@RequiresPermissions(value={"ident:application:view","ident:application:add","ident:application:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Application application, String modifyType, Model model) {
		if (!Global.SYSTEM.equals(application.getType())) {
			application = new Application();
		}
		model.addAttribute("application", application);
		model.addAttribute("modifyType", StringUtils.isNotBlank(modifyType)?modifyType:"1");
		return "modules/ident/identApplicationForm";
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
	@RequiresPermissions(value={"ident:application:add","ident:application:edit"},logical=Logical.OR)
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public String save(Application application, String modifyType, Model model,
			MultipartFile file, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, application)){
			return form(application, "1", model);
		}
		try {
			application.setType(Global.SYSTEM);
			applicationService.saveAppl(application, file);
		} catch (IOException e) {
			addMessage(redirectAttributes, "保存应用失败！失败信息："+e.getMessage());
		}
		addMessage(redirectAttributes, "保存应用'" + application.getName() + "'成功");
		if (StringUtils.isNotBlank(modifyType) && "2".equals(modifyType)) {
			return "redirect:" + adminPath + "/ident/userapplication/card?repage";
		}
		return "redirect:" + adminPath + "/ident/application/?repage";
	}
	
	/**
	 * @Title: delete 
	 * @Description: 单个删除
	 * @param apply
	 * @param redirectAttributes
	 * @return
	 * @author com.mhout.xyb
	 */
	@RequiresPermissions("ident:application:del")
	@RequestMapping(value = "delete")
	public String delete(Application application, RedirectAttributes redirectAttributes) {
		if(!applicationService.checkCustomApp(Global.MANAGERAPP)){
			addMessage(redirectAttributes, "越权操作，只有系统管理员才能删除应用！");
			return "redirect:" + adminPath + "/ident/application/?repage";
		}
		if (Global.ENABLE.equals(application.getStatus())) {
			addMessage(redirectAttributes, "使用中的应用，禁止删除！");
			return "redirect:" + adminPath + "/ident/application/?repage";
		}
		if (Global.SYSTEM.equals(application.getType())) {
			applicationService.delete(application);
			addMessage(redirectAttributes, "删除应用成功");
		} else {
			addMessage(redirectAttributes, "删除应用失败");
		}
		return "redirect:" + adminPath + "/ident/application/?repage";
	}
	
	/**
	 * @Title: deleteAll 
	 * @Description: 批量删除
	 * @param ids
	 * @param redirectAttributes
	 * @return
	 * @author com.mhout.xyb
	 */
	@RequiresPermissions("ident:application:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		if(!applicationService.checkCustomApp(Global.MANAGERAPP)){
			addMessage(redirectAttributes, "越权操作，只有系统管理员才能删除应用！");
			return "redirect:" + adminPath + "/ident/application/?repage";
		}
		String idArray[] =ids.split("\\,");
		StringBuilder failure = new StringBuilder();
		for(String id : idArray){
			Application application = applicationService.get(id);
			if (Global.ENABLE.equals(application.getStatus())) {
				failure.append(application.getName()+"正在使用，禁止删除！</br>");
				continue;
			}
			if (Global.SYSTEM.equals(application.getType())) {
				applicationService.delete(application);
			}
		}
		addMessage(redirectAttributes, failure==null?"删除应用成功":failure.toString());
		return "redirect:" + adminPath + "/ident/application/?repage";
	}
	
	/**
	 * @Title: deleteByLogic 
	 * @Description: 逻辑删除
	 * @param ids
	 * @param redirectAttributes
	 * @return
	 * @author com.mhout.xyb
	 */
	@RequiresPermissions("ident:application:del")
	@RequestMapping(value = "deleteByLogic")
	public String deleteByLogic(String ids, String modifyType,
			RedirectAttributes redirectAttributes) {
		if(!applicationService.checkCustomApp(Global.MANAGERAPP)){
			addMessage(redirectAttributes, "越权操作，只有系统管理员才能删除应用！");
			if (StringUtils.isNotBlank(modifyType)) {
				return "redirect:" + adminPath + "/ident/userapplication/card?repage";
			}
			return "redirect:" + adminPath + "/ident/application/?repage";
		}
		String idArray[] =ids.split("\\,");
		StringBuilder failure = new StringBuilder();
		for(String id : idArray){
			Application application = applicationService.get(id);
			if (Global.ENABLE.equals(application.getStatus())) {
				failure.append(application.getName()+"正在使用，禁止删除！</br>");
				continue;
			}
			if (Global.DISABLE.equals(application.getStatus()) && 
					Global.SYSTEM.equals(application.getType())) {
				applicationService.deleteApplication(application);
			}
		}
		addMessage(redirectAttributes, failure==null?"删除应用成功":failure.toString());
		if (StringUtils.isNotBlank(modifyType)) {
			return "redirect:" + adminPath + "/ident/userapplication/card?repage";
		}
		return "redirect:" + adminPath + "/ident/application/?repage";
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
	@RequiresPermissions("ident:application:del")
	@RequestMapping(value = "deleteAllByLogic")
	public AjaxJson deleteAllByLogic(String ids, RedirectAttributes redirectAttributes) {
		if(!applicationService.checkCustomApp(Global.MANAGERAPP)){
			addMessage(redirectAttributes, "越权操作，只有系统管理员才能删除应用！");
			AjaxJson json = new AjaxJson();
	    	json.setSuccess(false);
	    	json.setCode("1");
	    	json.setMsg("操作失败");
			return json;
		}
		String idArray[] =ids.split("\\,");
		StringBuilder failure = new StringBuilder();
		for(String id : idArray){
			Application application = applicationService.get(id);
			if (Global.ENABLE.equals(application.getStatus())) {
				failure.append(application.getName()+"正在使用，禁止删除！</br>");
				continue;
			}
			if (Global.SYSTEM.equals(application.getType())) {
				applicationService.deleteApplication(application);
			}
		}
		AjaxJson json = new AjaxJson();
    	json.setSuccess(true);
    	json.setCode("0");
    	json.setMsg("操作成功" + failure);
		return json;
	}
	
	/**
	 * @Title: enable 
	 * @Description: 启用
	 * @param application
	 * @param redirectAttributes
	 * @return
	 * @author com.mhout.xyb
	 */
	@RequiresPermissions("ident:application:enable")
	@RequestMapping(value = "enable")
	public String enable(Application application, RedirectAttributes redirectAttributes) {
		if (Global.SYSTEM.equals(application.getType())) {
			application.setStatus(Global.ENABLE);
			applicationService.save(application);
			addMessage(redirectAttributes, "启用应用成功");
		} else {
			addMessage(redirectAttributes, "启用应用失败");
		}
		return "redirect:" + adminPath + "/ident/application/?repage";
	}
	
	/**
	 * @Title: disable 
	 * @Description: 禁用
	 * @param application
	 * @param redirectAttributes
	 * @return
	 * @author com.mhout.xyb
	 */
	@RequiresPermissions("ident:application:disable")
	@RequestMapping(value = "disable")
	public String disable(Application application, RedirectAttributes redirectAttributes) {
		if (Global.SYSTEM.equals(application.getType())) {
			application.setStatus(Global.DISABLE);
			applicationService.save(application);
			addMessage(redirectAttributes, "禁用应用成功");
		} else {
			addMessage(redirectAttributes, "禁用应用失败");
		}
		return "redirect:" + adminPath + "/ident/application/?repage";
	}
	
	/**
     * @Title: authority
     * @Description: 查看权限
     * @param application
     * @param model
     * @return
     * @author com.mhout.xyb
     */
    @RequiresPermissions(value = { "ident:application:authority" })
    @RequestMapping(value = "authority")
    public String authority(Application application, Model model) {
        List<AuthUser> userAuth = authUserService.findByAppId(application.getId());
        List<AuthOffice> officeAuth = authOfficeService.findByAppId(application.getId());
        List<AuthIdentityGroup> groupAuth = authIdentityGroupService.findByAppId(application.getId());
        List<AuthRole> roleAuth = authRoleService.findByAppId(application.getId());
        model.addAttribute("userAuth", userAuth);
        model.addAttribute("officeAuth", officeAuth);
        model.addAttribute("groupAuth", groupAuth);
        model.addAttribute("roleAuth", roleAuth);
        return "modules/ident/identAuthorityForm";
    }
	
	/**
	 * @Title: manager 
	 * @Description: 指定管理员
	 * @param application
	 * @param model
	 * @return
	 * @author com.mhout.xyb
	 */
	@RequiresPermissions(value={"ident:application:manager"})
	@RequestMapping(value = "manager")
	public String manager(Application application, Model model) {
		model.addAttribute("application", application);
		return "modules/ident/identManagerForm";
	}
	
	/**
	 * @Title: user
	 * @Description: 管理成员
	 * @param application
	 * @param model
	 * @return
	 * @author com.mhout.xyb
	 */
	@RequiresPermissions(value={"ident:application:user"})
	@RequestMapping(value = "user")
	public String user(Application application, Model model) {
		model.addAttribute("application", application);
		return "modules/ident/identUserForm";
	}
	
	/**
	 * @Title: saveAuthority 
	 * @Description: 查看权限
	 * @param application
	 * @param userids
	 * @param model
	 * @return
	 * @author com.mhout.xyb
	 */
	@RequiresPermissions(value={"ident:application:authority"})
	@RequestMapping(value = "saveAuthority")
	public String saveAuthority(Application application, String userids, Model model,
			RedirectAttributes redirectAttributes) {
		applicationService.saveApplicationManager(application, userids);
		addMessage(redirectAttributes, "添加管理员成功！");
		return "redirect:" + adminPath + "/ident/application/?repage";
	}
	
	/**
	 * @Title: tree 
	 * @Description: 获取用户树
	 * @param id
	 * @param request
	 * @return
	 * @author com.mhout.xyb
	 */
	@RequiresPermissions("ident:application:list")
	@RequestMapping(value = "/tree", method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody
    public Object tree(@RequestParam(value = "id", required = false) String id,
    		@RequestParam(value = "appId", required = false) String appId,
    		String userName, HttpServletRequest request) {
		Application application = this.applicationService.get(appId);
        return this.applicationService.getTree(id, application, userName);
    }
	
	/**
	 * @Title: getApplicationAuth 
	 * @Description: 获取授权信息
	 * @param type
	 * @return
	 * @author com.mhout.xyb
	 */
	@ResponseBody
	@RequiresPermissions(value={"ident:application:user"})
	@RequestMapping(value = "authlist")
	public List<ApplicationAuthDto> getApplicationAuth(String type, String name, HttpServletRequest request,
			HttpServletResponse response) {
		return applicationService.getAuthList(type, name, request, response);
	}
	
	/**
	 * @Title: saveUserAuthority 
	 * @Description: 用户授权
	 * @param application
	 * @param redirectAttributes
	 * @return
	 * @author com.mhout.xyb
	 */
	@RequiresPermissions("ident:application:user")
	@RequestMapping(value = "saveUserAuthority")
	public String saveUserAuthority(String type, Application application, String ids,
			RedirectAttributes redirectAttributes) {
		applicationService.saveUserAuthority(type, application, ids);
		addMessage(redirectAttributes, "指定成员成功！");
		return "redirect:" + adminPath + "/ident/application/?repage";
	}
	
	/**
	 * @Title: getUserApplication 
	 * @Description: 获取用户应用
	 * @return
	 * @author com.mhout.xyb
	 */
	@ResponseBody
	@RequiresPermissions(value={"ident:application:edit"})
	@RequestMapping(value = "getUserApplication", method = RequestMethod.POST)
	public List<Application> getUserApplication() {
		Application application = new Application();
		application.setType(Global.SYSTEM);
		return applicationService.findAllUserApplication(application);
	}
	
	/**
	 * @Title: findManagerUser 
	 * @Description: 获取管理应用
	 * @param application
	 * @return
	 * @author com.mhout.xyb
	 */
	@ResponseBody
	@RequiresPermissions(value={"ident:application:authority"})
	@RequestMapping(value = "getManagerUser", method = RequestMethod.POST)
	public List<User> findManagerUser(Application application) {
		return applicationService.findManagerUser(application);
	}
	
	 /**
	 * @Title: list 
	 * @Description: 卡片排序
	 * @param ids
	 * @param redirectAttributes
	 * @return
	 * @author com.mhout.wj
	 */
	@RequiresPermissions("ident:application:order")
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
     * 验证登录名是否有效
     * 
     * @param oldLoginName
     * @param loginName
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "checkName")
    public String checkLoginName(Application application, String oldname) {
    	application.setType(Global.SYSTEM);
    	if (application != null && StringUtils.isNotBlank(application.getName())) {
    		return applicationService.checkName(application, oldname);
    	}
    	return "false";
    }
	
	/**
	 * @Title: download 
	 * @Description: 文件下载
	 * @param request
	 * @param response
	 * @param redirectAttributes
	 * @author com.mhout.xyb
	 */
	@RequestMapping(value = "download")
	public void download(HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes redirectAttributes) {  
        OutputStream os = null;
        response.reset();  
        // 获取到用户下载文件路径  
        String realName = request.getParameter("pic");  
        try {  
            // 拆分文件下载路径  
            String name = realName.substring(realName.lastIndexOf("/"));
            String path = Global.getApplUploadPath();
            realName = path + realName;  
            response.setHeader("Content-Disposition", "attachment; filename="  
                    + name);  
            response.setContentType("application/octet-stream; charset=utf-8");  
            os = response.getOutputStream();  
            os.write(FileUtils.readFileToByteArray(new File(realName)));  
            os.flush();  
        } catch (Exception e) {  
        	addMessage(redirectAttributes, "文件下载中断："+e.getMessage());
        } finally {  
            if (os != null) {  
                try {  
                    os.close();  
                } catch (IOException e) {  
                	addMessage(redirectAttributes, "文件下载中断："+e.getMessage());
                }  
            }  
        }  
    }  
}
