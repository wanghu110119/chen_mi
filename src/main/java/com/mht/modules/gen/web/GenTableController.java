/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mht.modules.gen.web;



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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mht.modules.gen.entity.GenTable;
import com.mht.modules.gen.service.GenTableService;
import com.mht.modules.gen.util.GenUtils;
import com.mht.common.persistence.Page;
import com.mht.common.utils.StringUtils;
import com.mht.common.web.BaseController;
import com.mht.modules.sys.entity.User;
import com.mht.modules.sys.utils.UserUtils;

/**
 * 业务表Controller
 * @author ThinkGem
 * @version 2013-10-15
 */
@Controller
@RequestMapping(value = "${adminPath}/gen/genTable")
public class GenTableController extends BaseController {

	@Autowired
	private GenTableService genTableService;
	
	@ModelAttribute
	public GenTable get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return genTableService.get(id);
		}else{
			return new GenTable();
		}
	}
	
	@RequiresPermissions("gen:genTable:list")
	@RequestMapping(value = {"list", ""})
	public String list(GenTable genTable, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();
		if (!user.isAdmin()){
			genTable.setCreateBy(user);
		}
        Page<GenTable> page = genTableService.find(new Page<GenTable>(request, response), genTable); 
        model.addAttribute("page", page);
		return "modules/gen/genTableList";
	}

	@RequiresPermissions("gen:genTable:edit")
	@RequestMapping(value = "form")
	public String form(GenTable genTable, Model model) {
		// 验证表是否存在
		if(StringUtils.isBlank(genTable.getId())){
			GenUtils.defaultColumn(genTable);
		}else{
			genTable = genTableService.getTableFormDb(genTable);
		}
		model.addAttribute("genTable", genTable);
		model.addAttribute("config", GenUtils.getConfig());
		return "modules/gen/genTableForm";
	}

	@RequiresPermissions("gen:genTable:edit")
	@RequestMapping(value = "save")
	public String save(GenTable genTable, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, genTable)){
			return form(genTable, model);
		}
		// 验证表是否已经存在
		if (StringUtils.isBlank(genTable.getId()) && !genTableService.checkTableName(genTable.getName())){
			addMessage(model, "保存失败！" + genTable.getName() + " 表已经存在！");
			genTable.setName("");
			return form(genTable, model);
		}
		genTableService.save(genTable);
		addMessage(redirectAttributes, "保存业务表'" + genTable.getName() + "'成功");
		return "redirect:" + adminPath + "/gen/genTable/?repage";
	}
	/**
	 * 删除业务表，非逻辑删除，
	 * @param genTable
	 * @param redirectAttributes
	 * @return 
	 */
	@RequiresPermissions("gen:genTable:edit")
	@RequestMapping(value = "delete")
	public String delete(GenTable genTable, RedirectAttributes redirectAttributes) {
		genTableService.delete(genTable);
		addMessage(redirectAttributes, "删除业务表成功");
		return "redirect:" + adminPath + "/gen/genTable/?repage";
	}
	/**
	 * 同步界面表单到数据库，创建数据库表
	 * @param genTable
	 * @return
	 */
	@RequiresPermissions("gen:genTable:edit")
	@RequestMapping(value = "synchDb")
	public String synchDb(GenTable genTable,RedirectAttributes redirectAttributes){
		genTableService.synchDb(genTable);
	    genTableService.syncSave(genTable);
	    addMessage(redirectAttributes, new String[] { "强制同步数据库表成功" });
		return "redirect:" + adminPath + "/gen/genTable/?repage";
	}
	/**
	 * 查询数据中的所有数据表
	 * 将数据库中的表导入到代码生成方案列表中
	 * @param genTable
	 * @param model
	 * @return
	 */
	@RequiresPermissions("gen:genTable:edit")
	@RequestMapping(value = "importTableFromDB")
	public String importTableFromDB(GenTable genTable, Model model,RedirectAttributes redirectAttributes){
		if(!StringUtils.isBlank(genTable.getName())){
			if(!genTableService.checkTableName(genTable.getName())){
				addMessage(redirectAttributes, new String[] { "下一步失败！" + genTable.getName() + " 表已经添加！" });
				return "redirect:" + this.adminPath + "/gen/genTable/?repage";
			}
			genTable = genTableService.getTableFormDb(genTable);
			genTable.setTableType("0");
			genTableService.saveFromDB(genTable);
			addMessage(redirectAttributes, new String[] { "数据库导入表单'" + genTable.getName() +"'成功" });
			return "redirect:" + this.adminPath + "/gen/genTable/?repage";
		}else{
			List<GenTable> tableList = genTableService.findTableListFormDb(genTable);
			model.addAttribute("tableList", tableList);
			return "modules/gen/importTableFromDB";
		}
	}
	/**
	 * 移除业务表
	 * @param genTable
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("gen:genTable:edit")
	@RequestMapping(value = "remove")
	public String remove(GenTable genTable, RedirectAttributes redirectAttributes){
		genTableService.remove(genTable);
		addMessage(redirectAttributes, "移除业务表成功");
		return "redirect:" + adminPath + "/gen/genTable/?repage";
	}
}
