/**
 * Copyright &copy; 2015-2020 <a href="http://www.mht.org/">mht</a> All rights reserved.
 */
package com.mht.modules.fieldconfig.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.apache.commons.collections.CollectionUtils;
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
import com.mht.modules.account.constant.GroupRole;
import com.mht.modules.account.constant.GroupType;
import com.mht.modules.fieldconfig.entity.FieldConfig;
import com.mht.modules.fieldconfig.entity.FieldGroup;
import com.mht.modules.fieldconfig.service.FieldConfigService;
import com.mht.modules.fieldconfig.service.FieldGroupService;

/**
 * 代码生成模块功能测试Controller
 * @author 张继平
 * @version 2017-03-31
 */
@Controller
@RequestMapping(value = "${adminPath}/fieldconfig/fieldGroup")
public class FieldGroupController extends BaseController {

	@Autowired
	private FieldGroupService fieldGroupService;
	
	@Autowired
	private FieldConfigService fieldConfigService;
	
	@ModelAttribute
	public FieldGroup get(@RequestParam(required=false) String id) {
		FieldGroup entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = fieldGroupService.get(id);
		}
		if (entity == null){
			entity = new FieldGroup();
		}
		return entity;
	}
	
	/**
	 * 字段分组列表页面
	 */
	@RequiresPermissions("fieldconfig:fieldGroup:list")
	@RequestMapping(value = {"list", ""})
	public String list(FieldGroup fieldGroup, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<FieldGroup> page = fieldGroupService.findPage(new Page<FieldGroup>(request, response), fieldGroup); 
		model.addAttribute("groupRoles", GroupRole.getGroupRoleDicts());
		model.addAttribute("groupTypes", GroupType.getGroupTypeDicts());
		model.addAttribute("page", page);
		return "modules/fieldconfig/fieldGroupList";
	}

	/**
	 * 查看，增加，编辑字段分组表单页面
	 */
	@RequiresPermissions(value={"fieldconfig:fieldGroup:view","fieldconfig:fieldGroup:add","fieldconfig:fieldGroup:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(FieldGroup fieldGroup, Model model) {
		model.addAttribute("fieldGroup", fieldGroup);
		List<FieldConfig> configs = new ArrayList<FieldConfig>();
		if(fieldGroup.getId() != null){
			configs = fieldConfigService.getFieldConfigByGroup(fieldGroup);
		}
		model.addAttribute("fieldGroup", fieldGroup);
		model.addAttribute("groupRoles", GroupRole.getGroupRoleDicts());
		model.addAttribute("groupTypes", GroupType.getGroupTypeDicts());
		model.addAttribute("configs", configs);
		return "modules/fieldconfig/fieldGroupForm";
	}

	/**
	 * 保存字段分组
	 */
	@RequiresPermissions(value={"fieldconfig:fieldGroup:add","fieldconfig:fieldGroup:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(FieldGroup fieldGroup, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, fieldGroup)){
			return form(fieldGroup, model);
		}		
		try{
			if(!fieldGroup.getIsNewRecord()){//编辑表单保存
				FieldGroup t = fieldGroupService.get(fieldGroup.getId());//从数据库取出记录的值
				MyBeanUtils.copyBeanNotNull2Bean(fieldGroup, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
				fieldGroupService.save(t);//保存
			}else{//新增表单保存
				fieldGroupService.save(fieldGroup);//保存
			}
		}catch (BusinessException e) {
			addMessage(model, e.getMessage());
			return form(fieldGroup, model);
		}catch (Exception e) {
			e.printStackTrace();
			addMessage(model, "保存分组失败！");
			return form(fieldGroup, model);
		}
		addMessage(redirectAttributes, "保存字段分组成功");
		return "redirect:"+Global.getAdminPath()+"/fieldconfig/fieldGroup/?repage";
	}
	
	/**
	 * 删除字段分组
	 */
	@RequiresPermissions("fieldconfig:fieldGroup:del")
	@RequestMapping(value = "delete")
	public String delete(FieldGroup fieldGroup, RedirectAttributes redirectAttributes) {
		fieldGroupService.delete(fieldGroup);
		addMessage(redirectAttributes, "删除字段分组成功");
		return "redirect:"+Global.getAdminPath()+"/fieldconfig/fieldGroup/?repage";
	}
	
	/**
	 * 批量删除字段分组
	 */
	@RequiresPermissions("fieldconfig:fieldGroup:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			fieldGroupService.delete(fieldGroupService.get(id));
		}
		addMessage(redirectAttributes, "删除字段分组成功");
		return "redirect:"+Global.getAdminPath()+"/fieldconfig/fieldGroup/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("fieldconfig:fieldGroup:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(FieldGroup fieldGroup, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "字段分组"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<FieldGroup> page = fieldGroupService.findPage(new Page<FieldGroup>(request, response, -1), fieldGroup);
    		new ExportExcel("字段分组", FieldGroup.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出字段分组记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/fieldconfig/fieldGroup/?repage";
    }

	/**
	 * 导入Excel数据
	 */
	@RequiresPermissions("fieldconfig:fieldGroup:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<FieldGroup> list = ei.getDataList(FieldGroup.class);
			for (FieldGroup fieldGroup : list){
				try{
					fieldGroupService.save(fieldGroup);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条字段分组记录。");
			}
			addMessage(redirectAttributes, "已字段分组导入 "+successNum+" 条字段分组记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入字段分组失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/fieldconfig/fieldGroup/?repage";
    }
	
	/**
	 * 下载导入字段分组数据模板
	 */
	@RequiresPermissions("fieldconfig:fieldGroup:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "字段分组数据导入模板.xlsx";
    		List<FieldGroup> list = Lists.newArrayList(); 
    		new ExportExcel("字段分组数据", FieldGroup.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/fieldconfig/fieldGroup/?repage";
    }
	
	/**
	 * 验证登录名是否有效
	 * @param oldLoginName
	 * @param loginName
	 * @return
	 */
	@ResponseBody
	@RequiresPermissions(value={"fieldconfig:fieldGroup:add","fieldconfig:fieldGroup:edit"},logical=Logical.OR)
	@RequestMapping(value = "checkGroupName")
	public String checkGroupName(String oldGroupName, String groupName) {
		if (groupName !=null && groupName.equals(oldGroupName)) {
			return "true";
		} else if (groupName !=null && fieldGroupService.getFieldGroupByGroupName(groupName) == null) {
			return "true";
		}
		return "false";
	}
}