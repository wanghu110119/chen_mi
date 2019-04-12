/**
 * Copyright &copy; 2015-2020 <a href="http://www.mht.org/">mht</a> All rights reserved.
 */
package com.mht.modules.fieldconfig.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

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

import com.google.common.collect.Lists;
import com.mht.common.config.Global;
import com.mht.common.exception.BusinessException;
import com.mht.common.persistence.Page;
import com.mht.common.utils.DateUtils;
import com.mht.common.utils.MyBeanUtils;
import com.mht.common.utils.StringUtils;
import com.mht.common.utils.excel.ExportExcel;
import com.mht.common.utils.excel.ImportExcel;
import com.mht.common.web.BaseController;
import com.mht.modules.fieldconfig.entity.FieldConfig;
import com.mht.modules.fieldconfig.service.FieldConfigService;

/**
 * 
* @ClassName: FieldConfigController 
* @Description: 扩展字段配置Controller
* @author 华强 
* @date 2017年3月30日 上午11:04:37 
*
 */
@Controller
@RequestMapping(value = "${adminPath}/fieldconfig/fieldConfig")
public class FieldConfigController extends BaseController {

	@Autowired
	private FieldConfigService fieldConfigService;
	
	@ModelAttribute
	public FieldConfig get(@RequestParam(required=false) String id) {
		FieldConfig entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = fieldConfigService.get(id);
		}
		if (entity == null){
			entity = new FieldConfig();
		}
		return entity;
	}
	
	/**
	 * 成功列表页面
	 */
	@RequiresPermissions("fieldconfig:fieldConfig:list")
	@RequestMapping(value = {"list", ""})
	public String list(FieldConfig fieldConfig, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<FieldConfig> page = fieldConfigService.findPage(new Page<FieldConfig>(request, response), fieldConfig); 
		model.addAttribute("page", page);
		return "modules/fieldconfig/fieldConfigList";
	}
	
	/**
	 * 获取可用的配置字段页面
	 */
	@RequestMapping(value = {"usableList"})
	public String usableList(FieldConfig fieldConfig, Model model) {
		List<FieldConfig> fieldConfigs = fieldConfigService.findUsableFieldConfigList(fieldConfig); 
		model.addAttribute("fieldConfigs", fieldConfigs);
		model.addAttribute("groupId", fieldConfig.getGroupId());
		model.addAttribute("groupRole", fieldConfig.getGroupRole());
		model.addAttribute("fieldConfigIds", fieldConfig.getFieldConfigIds());
		return "modules/fieldconfig/fieldConfigListForGroup";
	}
	

	/**
	 * 查看，增加，编辑成功表单页面
	 */
	@RequiresPermissions(value={"fieldconfig:fieldConfig:view","fieldconfig:fieldConfig:add","fieldconfig:fieldConfig:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(FieldConfig fieldConfig, Model model) {
		model.addAttribute("fieldConfig", fieldConfig);
		return "modules/fieldconfig/fieldConfigForm";
	}

	/**
	 * 保存扩展字段
	 */
	@RequiresPermissions(value={"fieldconfig:fieldConfig:add","fieldconfig:fieldConfig:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(FieldConfig fieldConfig, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, fieldConfig)){
			return form(fieldConfig, model);
		}
		try{
			if(!fieldConfig.getIsNewRecord()){//编辑表单保存
				FieldConfig t = fieldConfigService.get(fieldConfig.getId());//从数据库取出记录的值
				MyBeanUtils.copyBeanNotNull2Bean(fieldConfig, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
				fieldConfigService.save(t);//保存
			}else{//新增表单保存
				fieldConfigService.addConfig(fieldConfig);//保存
			}
		}catch (BusinessException e) {
			addMessage(model, e.getMessage());
			return form(fieldConfig, model);
		}catch (Exception e) {
			e.printStackTrace();
			addMessage(model, "保存扩展字段失败！");
			return form(fieldConfig, model);
		}		
		addMessage(redirectAttributes, "保存扩展字段成功");
		return "redirect:"+Global.getAdminPath()+"/fieldconfig/fieldConfig/?repage";
	}
	
	/**
	 * 删除成功
	 */
	@RequiresPermissions("fieldconfig:fieldConfig:del")
	@RequestMapping(value = "delete")
	public String delete(FieldConfig fieldConfig, RedirectAttributes redirectAttributes) {
		if (fieldConfigService.deleteField(fieldConfig)) {
			addMessage(redirectAttributes, "删除成功");
		} else {
			addMessage(redirectAttributes, "该字段已经被使用，禁止删除！");
		}
		return "redirect:"+Global.getAdminPath()+"/fieldconfig/fieldConfig/?repage";
	}
	
	/**
	 * 批量删除成功
	 */
	@RequiresPermissions("fieldconfig:fieldConfig:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			fieldConfigService.delete(fieldConfigService.get(id));
		}
		addMessage(redirectAttributes, "删除成功");
		return "redirect:"+Global.getAdminPath()+"/fieldconfig/fieldConfig/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("fieldconfig:fieldConfig:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(FieldConfig fieldConfig, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "成功"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<FieldConfig> page = fieldConfigService.findPage(new Page<FieldConfig>(request, response, -1), fieldConfig);
    		new ExportExcel("成功", FieldConfig.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出成功记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/fieldconfig/fieldConfig/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("fieldconfig:fieldConfig:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<FieldConfig> list = ei.getDataList(FieldConfig.class);
			for (FieldConfig fieldConfig : list){
				try{
					fieldConfigService.save(fieldConfig);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条成功记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条成功记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入成功失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/fieldconfig/fieldConfig/?repage";
    }
	
	/**
	 * 下载导入成功数据模板
	 */
	@RequiresPermissions("fieldconfig:fieldConfig:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "成功数据导入模板.xlsx";
    		List<FieldConfig> list = Lists.newArrayList(); 
    		new ExportExcel("成功数据", FieldConfig.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/fieldconfig/fieldConfig/?repage";
    }
	
	/**
	 * 验证登录名是否有效
	 * @param oldLoginName
	 * @param loginName
	 * @return
	 */
	@ResponseBody
	@RequiresPermissions(value={"fieldconfig:fieldConfig:add","fieldconfig:fieldConfig:edit"},logical=Logical.OR)
	@RequestMapping(value = "checkFieldName")
	public String checkFieldName(String oldFieldName, String fieldName) {
		if (fieldName !=null && fieldName.equals(oldFieldName)) {
			return "true";
		} else if (fieldName !=null && fieldConfigService.getFieldConfigByFieldName(fieldName) == null) {
			return "true";
		}
		return "false";
	}
	
	
}