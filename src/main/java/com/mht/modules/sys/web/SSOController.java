package com.mht.modules.sys.web;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mht.common.json.AjaxJson;
import com.mht.modules.sys.entity.SSOConfig;
import com.mht.modules.sys.service.SSOConfigService;

/**
 * @ClassName: SSOController
 * @Description: 单点登录配置
 * @author com.mhout.xyb
 * @date 2017年6月1日 下午1:54:12 
 * @version 1.0.0
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/sso")
public class SSOController {
	
	@Autowired
	private SSOConfigService ssoConfigService;
	
	/**
	 * @Title: index 
	 * @Description: 跳转到备份页面
	 * @return
	 * @author com.mhout.xyb
	 */
	@RequiresPermissions("sys:sso:index")
	@RequestMapping(value = {"index", ""})
	public String index(Model model) {
		return "modules/sys/ssoConfig";
	}
	
	/**
	 * @Title: list 
	 * @Description: 单点登录列表
	 * @return
	 * @author com.mhout.xyb
	 */
	@ResponseBody
	@RequiresPermissions("sys:sso:index")
	@RequestMapping(value = {"list"})
	public List<SSOConfig> list(SSOConfig ssoConfig, Model model) {
		return ssoConfigService.findList(ssoConfig);
	}
	
	/**
	 * @Title: edit 
	 * @Description: 单点登录配置修改
	 * @param list
	 * @return
	 * @author com.mhout.xyb
	 */
	@ResponseBody
	@RequiresPermissions("sys:sso:edit")
	@RequestMapping(value = {"edit"})
	public AjaxJson edit(@RequestBody List<SSOConfig> list) {
		boolean msg = ssoConfigService.edit(list);
		AjaxJson json = new AjaxJson();
		if (msg) {
			json.setSuccess(true);
	    	json.setCode("0");
	    	json.setMsg("操作成功");
		} else {
			json.setSuccess(false);
	    	json.setCode("1");
	    	json.setMsg("操作失败");
		}
		return json;
	}

}
