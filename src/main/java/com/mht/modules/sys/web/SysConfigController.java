package com.mht.modules.sys.web;

import java.io.IOException;
import java.util.List;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.mht.common.json.AjaxJson;
import com.mht.modules.ident.service.SysProjectService;
import com.mht.modules.sys.entity.Dict;
import com.mht.modules.sys.entity.SysConfig;
import com.mht.modules.sys.service.SysConfigService;

/**
 * @ClassName: SysConfigController
 * @Description: 系统参数配置管理
 * @author com.mhout.xyb
 * @date 2017年5月15日 上午11:02:05 
 * @version 1.0.0
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/config")
public class SysConfigController {
	
	@Autowired
	private SysConfigService sysConfigService;
	
	@Autowired
	private SysProjectService sysProjectService;
	
	
	
	/**
	 * @Title: index 
	 * @Description: 跳转到备份页面
	 * @return
	 * @author com.mhout.xyb
	 */
	@RequiresPermissions("sys:config:index")
	@RequestMapping(value = {"index", ""})
	public String index(Model model) {
		model.addAttribute("projects", sysProjectService.findAll());
		return "modules/sys/sysConfig";
	}
	
	/**
	 * @Title: getTypeList 
	 * @Description: 类型
	 * @param sysConfig
	 * @param model
	 * @return
	 * @author com.mhout.xyb
	 */
	@ResponseBody
	@RequiresPermissions("sys:config:index")
	@RequestMapping(value = {"getTypeList"})
	public List<SysConfig> getTypeList(SysConfig sysConfig, Model model){
		return sysConfigService.findTypeList(sysConfig);
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
	@RequiresPermissions("sys:config:index")
	@RequestMapping(value = {"list"})
	public List<SysConfig> list(SysConfig sysConfig, String did, Model model) {
		sysConfig.setDict(new Dict(did));
		return sysConfigService.findList(sysConfig);
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
	@RequiresPermissions(value={"sys:config:add","sys:config:edit"},logical=Logical.OR)
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public AjaxJson save(@RequestBody List<SysConfig> list, Model model){
		sysConfigService.saveSafe(list);
		AjaxJson json = new AjaxJson();
    	json.setSuccess(true);
    	json.setCode("0");
    	json.setMsg("操作成功");
		return json;
	}
	
	/**
	 * @Title: saveLogo 
	 * @Description: logo上传
	 * @param file
	 * @return
	 * @author com.mhout.xyb
	 */
	@ResponseBody
	@RequiresPermissions(value={"sys:config:add","sys:config:edit"},logical=Logical.OR)
	@RequestMapping(value = "saveLogo", method = RequestMethod.POST)
	public AjaxJson saveLogo(@RequestParam(value = "file", required = false) MultipartFile file,
			@RequestParam(value = "id", required = false) String id) {
		AjaxJson json = new AjaxJson();
		try {
			sysConfigService.saveFile(file, id);
			json.setSuccess(true);
	    	json.setCode("0");
	    	json.setMsg("操作成功");
		} catch (IOException e) {
			json.setSuccess(false);
	    	json.setCode("1");
	    	json.setMsg("操作失败");
		}
		return json;
	}
}
