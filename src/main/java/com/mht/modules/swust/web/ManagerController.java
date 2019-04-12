package com.mht.modules.swust.web;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
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

import com.google.common.collect.Lists;
import com.mht.common.beanvalidator.BeanValidators;
import com.mht.common.config.Global;
import com.mht.common.json.AjaxJson;
import com.mht.common.persistence.Page;
import com.mht.common.utils.DateUtils;
import com.mht.common.utils.MD5Utils;
import com.mht.common.utils.StringUtils;
import com.mht.common.utils.excel.ExportExcel;
import com.mht.common.utils.excel.ImportExcel;
import com.mht.common.web.BaseController;
import com.mht.modules.swust.dao.SysUserDao;
import com.mht.modules.swust.entity.SysCar;
import com.mht.modules.swust.service.ManagerService;
import com.mht.modules.sys.dao.UserDao;
import com.mht.modules.sys.entity.Office;
import com.mht.modules.sys.entity.User;
import com.mht.modules.sys.utils.UserUtils;

/**
 * @ClassName: ManagerController
 * @Description:
 * @author com.mhout.dhn
 * @date 2017年7月26日 下午4:18:07
 * @version 1.0.0
 */
@Controller
@RequestMapping(value = "a/swust/manager")
public class ManagerController extends BaseController {

	@Autowired
	private ManagerService managerService;
	
	@Autowired
	private SysUserDao userDao;

	@ModelAttribute("office")
	public Office get(@RequestParam(required = false) String id) {
		if (StringUtils.isNotBlank(id)) {
			return managerService.get(id);
		} else {
			return new Office();
		}
	}

	/**
	 * @Title: index
	 * @Description: 跳转页面
	 * @return
	 * @author com.mhout.xyb
	 */
	@RequestMapping(value = { "" }, method = RequestMethod.POST)
	public String index() {
		return "/modules/swust/manager";

	}

	/**
	 * @Title: index
	 * @Description: 单位列表
	 * @param manager
	 * @param username
	 * @param password
	 * @return
	 * @author com.mhout.dhn
	 */
	@RequestMapping(value = { "list" }, method = RequestMethod.POST)
	public String list(HttpServletRequest request, HttpServletResponse response, Model model, Office office) {
		office.setGrade("2");
		Page<Office> page = managerService.findPage(new Page<Office>(request, response), office);
		model.addAttribute("page", page);
		return "modules/swust/managerList";
	}

	/**
	 * 
	 * @Title: resetpassword
	 * @Description: TODO
	 * @param request
	 * @param response
	 * @param id
	 * @author com.mhout.wzw
	 */
	@ResponseBody
	@RequestMapping("resetpassword")
	public AjaxJson resetpassword(HttpServletRequest request, HttpServletResponse response, String loginName) {
		AjaxJson json = new AjaxJson();
		User user = new User();
		user.setLoginName(loginName);
		user = userDao.getByLoginName(user);
		if (user != null) {
			user.setPassword(MD5Utils.make("123456"));
			userDao.update(user);
			UserUtils.clearCache(user);
		}
		return json;	
	}

	/**
	 * @Title: updateOffice 
	 * @Description: 保存单位信息
	 * @param office
	 * @return
	 * @author com.mhout.xyb
	 */
	@ResponseBody
	@RequestMapping("save")
	public AjaxJson save(Office office) {
		AjaxJson ajaxJson = new AjaxJson();
		boolean istrue = true;
		String oldname = "", oldloginName = ""; 
		if (StringUtils.isNotBlank(office.getId())) {
			Office oldoffice  = managerService.get(office.getId());
			oldname = oldoffice.getName();
			oldloginName = oldoffice.getPrimaryPerson().getLoginName();
		}
		//单位名称验证
		String name = office.getName();
		String loginname = office.getPrimaryPerson().getLoginName();
		if ("false".equals(checkName(name, null, oldname))){
			istrue = false;
		} else if ("false".equals(checkName(null, loginname, oldloginName))) {
			istrue = false;
		}
		if (istrue) {
			managerService.saveOffice(office, oldloginName);
		} else {
			ajaxJson.setSuccess(istrue);
			ajaxJson.setMsg("保存失败:单位名称或者单位用户名已存在");
		}
		return ajaxJson;
	}

	/**
	 * @Title: update
	 * @Description: 获取全部的数据
	 * @param manager
	 * @param username
	 * @param password
	 * @return
	 * @author com.mhout.dhn
	 */
	@ResponseBody
	@RequestMapping("update")
	public String update(Office office) {
		managerService.save(office);
		return "/modules/swust/mobile/managerList";
	}

	/**
	 * @Title: findByOrganization
	 * @Description: 获取全部的数据
	 * @param manager
	 * @param username
	 * @param password
	 * @author com.mhout.dhn
	 */
	@RequestMapping("findByOrganization")
	public String findByOrganization(Office office, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		List<Office> findList = managerService.findList(office);
		Page<Office> page = managerService.findPage(new Page<Office>(request, response), office);
		model.addAttribute("page", findList);
		return "/modules/swust/managerList";
	}
	
	/**
	 * @Title: disable usable
	 * @Description: 禁用
	 * @param office
	 * @return
	 * @author com.mhout.xyb
	 */
	@ResponseBody
	@RequestMapping(value = { "disable" }, method = RequestMethod.POST)
	public AjaxJson disable(Office office) {
		managerService.updateState(office, Global.DISABLE);
		return new AjaxJson();
	} 
	
	/**
	 * @Title: enable 
	 * @Description: 启用
	 * @param office
	 * @return
	 * @author com.mhout.xyb
	 */
	@ResponseBody
	@RequestMapping(value = { "enable" }, method = RequestMethod.POST)
	public AjaxJson enable(Office office) {
		managerService.updateState(office, Global.ENABLE);
		return new AjaxJson();
	} 
	
	/**
	 * @Title: getOffice 
	 * @Description: TODO
	 * @param office
	 * @return
	 * @author com.mhout.xyb
	 */
	@ResponseBody
	@RequestMapping("getOffice")
	public Office getOffice(Office office) {
		return office;
	}
	
	/**
	 * @Title: checkName 
	 * @Description: TODO
	 * @return
	 * @author com.mhout.xyb
	 */
	@ResponseBody
	@RequestMapping(value = "checkName", method = RequestMethod.POST)
	public String checkName(@RequestParam(required = false) String name, 
			@RequestParam(required = false)String loginname, 
			@RequestParam(required = false)String oldname) {
		return managerService.checkName(name, loginname, oldname);
	}

	
	/**
     * @Title: importFileTemplate
     * @Description: TODO 导入模板下载
     * @param response
     * @param redirectAttributes
     * @return
     * @author com.mhout.wzw
     */
    @RequestMapping(value = "importExample")
    public AjaxJson importFileTemplate(HttpServletResponse response ) {
    	AjaxJson json = new AjaxJson();
        try {
            String fileName = "单位信息导入模板.xlsx";
            List<Office> list = Lists.newArrayList();
            list.add(ManagerService.getOfficeExample());
            new ExportExcel("单位信息", Office.class, 2).setDataList(list).write(response, fileName).dispose();
            json.setMsg("完成");
            return null;
        } catch (Exception e) {
            json.setMsg("导入模板下载失败！失败信息：" + e.getMessage());
        }
        return null;
    }
    /**
     * @Title: importFile
     * @Description: TODO excel导入
     * @param file
     * @param redirectAttributes
     * @return
     * @author com.mhout.wzw
     */
    @ResponseBody
    @RequestMapping(value = "import", method = RequestMethod.POST)
    public AjaxJson importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
    	AjaxJson ajax = new AjaxJson();
        if (Global.isDemoMode()) {
            addMessage(redirectAttributes, "演示模式，不允许操作！");
            ajax.setMsg("演示模式，不允许操作！");
            return ajax;
        }
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 1, 0);
			String val = (String)ei.getCellValue(ei.getRow(0), 0);
			if(!"单位信息".equals(val)){
				addMessage(redirectAttributes, "导入预约信息失败！导入信息有误");
				ajax.setMsg("抱歉：请下载正确的模板后再次导入数据！");
				return ajax;
			}
            List<Office> list = ei.getDataList(Office.class);
            if(list.size()==0){
            	ajax.setMsg("Excel无数据");
            	return ajax;
            }
            for (Office user : list) {
            	if("".equals(user.getName())&&"".equals(user.getPersonPhone())&&"".equals(user.getPersonName())){
                	continue;
                }
                try {
                    if ("true".equals(checkUser(user))) {
                    	if(checkName(user.getPersonName())){
                   		 failureMsg.append("<br/>导入单位 " + user.getName() + " 的联系人输入不合规则; ");
                            failureNum++;
                   	}else
                    	if(checkInput(user)){
                    		save(user);
                    		 successNum++;
                    	}else{
                    failureMsg.append("<br/>导入单位 " + user.getName() + " 的信息输入不合规则; ");
                    failureNum++;
                    	}
                    } else {
                        failureMsg.append(checkResult(user));
                        failureNum++;
                    }
                } catch (ConstraintViolationException ex) {
                    failureMsg.append("<br/>单位 " + user.getName() + " 导入失败：");
                    List<String> messageList = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
                    for (String message : messageList) {
                        failureMsg.append(message + "; ");
                        failureNum++;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    failureMsg.append("<br/>单位" + user.getName() + " 导入失败：" + ex.getMessage());
                }
            }
            if (failureNum > 0) {
                failureMsg.insert(0, "，失败 " + failureNum + " 条单位信息，导入信息如下：");
            }
            addMessage(redirectAttributes, "已成功导入 " + successNum + " 条单位信息" + failureMsg);
            ajax.setMsg("已成功导入 " + successNum + " 条单位信息" + failureMsg);
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入单位信息失败！失败信息：" + e.getMessage());
            ajax.setMsg("导入单位信息失败！失败信息：" + e.getMessage());
        }
        return ajax;
    }
    private boolean checkName(String personName) {
		// TODO Auto-generated method stub
    	Pattern patternName = Pattern.compile("[\u4e00-\u9fa5A-Za-z]+");
    	if(!patternName.matcher(personName).matches()){
    		return true;
    	}
    	
		return false;
	}

	/**
     * 检查输入EXCEL输入是否有误
     * @param user
     * @return
     */
    private boolean checkInput(Office office) {
    	Pattern patternName = Pattern.compile("[\u4e00-\u9fa5A-Za-z]+");
    	Pattern patternLoginName = Pattern.compile("[\u4e00-\u9fa5A-Za-z0-9]+");
//    	Pattern patternPersonphone = Pattern.compile("^1[3|4|5|8][0-9]\\d{8}$");
    	boolean check = false;
    	boolean a = patternName.matcher(office.getName()).matches();
    	boolean b = patternLoginName.matcher(office.getPrimaryPerson().getLoginName()).matches();
//    	boolean d = patternPersonphone.matcher(office.getPrimaryPerson().getPhone()).matches();
    	if(a&&b&&office.getName()!=null&&!"".equals(office.getName())&&office.getPrimaryPerson().getLoginName()!=null&&!"".equals(office.getPrimaryPerson().getLoginName())){
    		 check = true;
    	}
    	return check;
	}

	/**
     * @Title: checkParamUser 
     * @Description: 参数校验
     * @param student
     * @return
     * @author com.mhout.xyb
     */
    private String checkParamUser(Office student) {
    	User user = new User();
    	BeanUtils.copyProperties(student, user);
    	return UserUtils.checkParamUser(user);
    }
    
    private String checkUser(Office office) {
    	if(office.getName()==null||"".equals(office.getName())||office.getName().length()<2||office.getName().length()>15||
    			office.getPrimaryPerson().getLoginName()==null||office.getPrimaryPerson().getLoginName().length()<2||office.getPrimaryPerson().getLoginName().length()>10||
    			"".equals(office.getPrimaryPerson().getLoginName())){
    		return "false";
    	}
    	String regex="^[a-zA-Z0-9]+$";
		Pattern pattern = Pattern.compile(regex);
		Matcher match=pattern.matcher(office.getPrimaryPerson().getLoginName());
    	if(!match.matches()){
    		return "false";
    	}
		boolean istrue = true;
		String oldname = "", oldloginName = ""; 
		if (StringUtils.isNotBlank(office.getId())) {
			Office oldoffice  = managerService.get(office.getId());
			oldname = oldoffice.getName();
			oldloginName = oldoffice.getPrimaryPerson().getLoginName();
		}
		//单位名称验证
		String name = office.getName();
		String loginname = office.getPrimaryPerson().getLoginName();
		if ("false".equals(checkName(name, null, oldname))){
			istrue = false;
		} else if ("false".equals(checkName(null, loginname, oldloginName))) {
			istrue = false;
		}
		if (istrue) {
			
		} else {
			return "false";
		}
    	
    	return managerService.checkName(office.getName(), office.getPrimaryPerson().getLoginName(), "");
    }
    
    private String checkResult(Office office) {
    	if(office.getName()==null||"".equals(office.getName())||
    			office.getPrimaryPerson().getLoginName()==null||
    			"".equals(office.getPrimaryPerson().getLoginName())){
    		return  "<br/>导入单位和单位用户名不能为空; ";
    	}
		boolean istrue = true;
		String oldname = "", oldloginName = ""; 
		if (StringUtils.isNotBlank(office.getId())) {
			Office oldoffice  = managerService.get(office.getId());
			oldname = oldoffice.getName();
			oldloginName = oldoffice.getPrimaryPerson().getLoginName();
		}
		//单位名称验证
		String name = office.getName();
		String loginname = office.getPrimaryPerson().getLoginName();
		String synx = "^[a-zA-Z\u4E00-\u9FA5]+$";
		String regex="^[a-zA-Z0-9]+$";
		Pattern pattern = Pattern.compile(synx);
		Matcher match=pattern.matcher(name);
		boolean b=match.matches();
		boolean isMatch = Pattern.matches(regex, loginname);
		if(!b){
			return  "<br/>导入单位 " + office.getName() + " 名只能为汉字、字母";
		}else if(!isMatch){
			return  "<br/>导入单位 " + office.getName() + " 用户名只能为字母和数字";
		}else
		if(name.length()<2||name.length()>15){
			return  "<br/>导入单位 " + office.getName() + " 的名称长度请在2~15之间; ";
		}else if(loginname.length()<2||loginname.length()>10){
			return  "<br/>导入单位 " + office.getName() + " 的单位用户名长度请在2~10之间; ";
		}else if ("false".equals(checkName(name, null, oldname))){
			return  "<br/>导入单位 " + office.getName() + " 的相关信息已存在; ";
		} else if ("false".equals(checkName(null, loginname, oldloginName))) {
			return  "<br/>导入单位的用户名 " + loginname + " 的相关信息已存在; ";
		}
		if (istrue) {
			
		} else {
			return "<br/>导入单位 " + office.getName() + " 的相关信息已存在; ";
		}
    	
    	return "<br/>导入单位 " + office.getName() + " 的相关信息已存在; ";
    }
    
    
    
    
	/**
	 * 
	 * @Title: importFile
	 * @Description: 把符合条件的预约信息以excel文件格式导出
	 * @param file
	 * @param redirectAttributes
	 * @return
	 * @author com.mhout.wzw
	 */
	@ResponseBody
	@RequestMapping(value = "export", method = RequestMethod.GET)
	public AjaxJson exportFile(Office office, HttpServletRequest request, HttpServletResponse response,String name,
			RedirectAttributes redirectAttributes) {
		office.setGrade("2");
		if(name!=null&&!"".equals(name)){
			office.setName(name);
		}
		AjaxJson ajaxJson = new AjaxJson();
		boolean issuccess = true;
		String msg = "操作成功!";
		try {
			String fileName = "单位信息" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
			Page<Office> page = managerService.findPage(new Page<Office>(request, response, -1), office);
			new ExportExcel("单位信息", Office.class).setDataList(page.getList()).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			issuccess = false;
			msg = "导出数据失败!";
		}
		ajaxJson.setSuccess(issuccess);
		ajaxJson.setMsg(msg);
		return ajaxJson;
	}
    
	@ResponseBody
	@RequestMapping(value = {"batchDelete"}, method = RequestMethod.POST)
	public AjaxJson batchPass(@RequestParam(value = "ids")String[] ids) {
		AjaxJson ajaxJson = new AjaxJson();
		managerService.batchDelete(ids);
		ajaxJson.setSuccess(true);
		ajaxJson.setMsg("操作成功");
		return ajaxJson;
	}
	
	
	
	
	
	
	
	
}
