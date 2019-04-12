package com.mht.modules.audit.web;

import java.util.List;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mht.common.json.AjaxJson;
import com.mht.modules.audit.entity.SafeStrategy;
import com.mht.modules.audit.service.SafeStrategyService;
import com.mht.modules.sys.entity.Dict;

/**
 * @ClassName: SafeStrategyController
 * @Description: 安全配置策略管理
 * @author com.mhout.xyb
 * @date 2017年4月20日 下午1:39:05 
 * @version 1.0.0
 */
@Controller
@RequestMapping(value = "${adminPath}/audit/safeStrategy")
public class SafeStrategyController {
	
	@Autowired
	private SafeStrategyService safeStrategyService;
	
	/**
	 * @Title: index 
	 * @Description: 主页
	 * @return
	 * @author com.mhout.xyb
	 */
	@RequiresPermissions("audit:safe:index")
	@RequestMapping(value = {""})
	public String index(SafeStrategy safeStrategy, Model model) {
		return "modules/audit/safeStrategy";
	}
	
	/**
	 * @Title: list 
	 * @Description: 列表
	 * @param safeStrategy
	 * @param model
	 * @return
	 * @author com.mhout.xyb
	 */
	@ResponseBody
	@RequiresPermissions("audit:safe:index")
	@RequestMapping(value = {"list"})
	public List<SafeStrategy> list(SafeStrategy safeStrategy, String did, Model model) {
		safeStrategy.setDict(new Dict(did));
		return safeStrategyService.findList(safeStrategy);
	}
	
	/**
	 * @Title: getTypeList 
	 * @Description: 类型
	 * @param safeStrategy
	 * @param model
	 * @return
	 * @author com.mhout.xyb
	 */
	@ResponseBody
	@RequiresPermissions("audit:safe:index")
	@RequestMapping(value = {"getTypeList"})
	public List<SafeStrategy> getTypeList(SafeStrategy safeStrategy, Model model){
		return safeStrategyService.findTypeList(safeStrategy);
	}
	
	/**
	 * @Title: save 
	 * @Description: 编辑/保存配置
	 * @param safeStrategy
	 * @param model
	 * @return
	 * @author com.mhout.xyb
	 */
	@ResponseBody
	@RequiresPermissions(value={"ident:safe:add","ident:safe:edit"},logical=Logical.OR)
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public AjaxJson save(@RequestBody List<SafeStrategy> list, Model model){
		safeStrategyService.saveSafe(list);
		AjaxJson json = new AjaxJson();
    	json.setSuccess(true);
    	json.setCode("0");
    	json.setMsg("操作成功");
		return json;
	}
	
	/**
	 * @Title: defaultValue 
	 * @Description: 恢复默认值
	 * @param id
	 * @return
	 * @author com.mhout.xyb
	 */
	@ResponseBody
	@RequiresPermissions(value={"ident:safe:edit"},logical=Logical.OR)
	@RequestMapping(value = "defaultValue", method = RequestMethod.POST)
	public AjaxJson defaultValue(String id){
		safeStrategyService.defaultValue(id);
		AjaxJson json = new AjaxJson();
    	json.setSuccess(true);
    	json.setCode("0");
    	json.setMsg("操作成功");
		return json;
	}
}
