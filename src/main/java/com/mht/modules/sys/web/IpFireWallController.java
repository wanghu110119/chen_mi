package com.mht.modules.sys.web;

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

import com.mht.common.persistence.Page;
import com.mht.common.utils.StringUtils;
import com.mht.common.web.BaseController;
import com.mht.modules.sys.entity.IpFireWall;
import com.mht.modules.sys.service.IpFireWallService;

/**
 * @ClassName: IpFireWallController
 * @Description: IP访问控制
 * @author com.mhout.xyb
 * @date 2017年5月11日 下午3:28:21 
 * @version 1.0.0
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/ipFireWall")
public class IpFireWallController extends BaseController{
	
	@Autowired
	private IpFireWallService ipFireWallService;
	

	@ModelAttribute
	public IpFireWall get(@RequestParam(required=false) String id) {
		IpFireWall entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = ipFireWallService.get(id);
		}
		if (entity == null){
			entity = new IpFireWall();
		}
		return entity;
	}
	
	/**
	 * @Title: index 
	 * @Description: ip访问控制列表
	 * @param ipFireWall
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @author com.mhout.xyb
	 */
	@RequiresPermissions("sys:ipFireWall:index")
	@RequestMapping(value = {"index", ""})
	public String index(IpFireWall ipFireWall,HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<IpFireWall> page = ipFireWallService.find(new Page<IpFireWall>(request, response), ipFireWall);
		model.addAttribute("page", page);
		return "modules/sys/ipFireWall";
	}
	
	/**
	 * @Title: form 
	 * @Description: 查看，增加，编辑报告表单页面
	 * @param ipFireWall
	 * @param model
	 * @return
	 * @author com.mhout.xyb
	 */
	@RequiresPermissions(value={"sys:ipFireWall:view","sys:ipFireWall:add","sys:ipFireWall:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(IpFireWall ipFireWall, Model model) {
		model.addAttribute("ipFireWall", ipFireWall);
		return "modules/sys/ipFireWallForm";
	}
	
	/**
	 * @Title: save 
	 * @Description: 保存或者编辑
	 * @param application
	 * @param model
	 * @param redirectAttributes
	 * @return
	 * @author com.mhout.xyb
	 */
	@RequiresPermissions(value={"sys:ipFireWall:add","sys:ipFireWall:edit"},logical=Logical.OR)
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public String save(IpFireWall ipFireWall, Model model,
			MultipartFile file, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, ipFireWall)){
			return form(ipFireWall, model);
		}
		ipFireWallService.save(ipFireWall);
		addMessage(redirectAttributes, "保存成功");
		return "redirect:" + adminPath + "/sys/ipFireWall/?repage";
	}
	
	/**
	 * @Title: delete 
	 * @Description: 单个删除
	 * @param apply
	 * @param redirectAttributes
	 * @return
	 * @author com.mhout.xyb
	 */
	@RequiresPermissions("sys:ipFireWall:del")
	@RequestMapping(value = "delete")
	public String delete(IpFireWall ipFireWall, RedirectAttributes redirectAttributes) {
		ipFireWallService.delete(ipFireWall);
		addMessage(redirectAttributes, "删除成功");
		return "redirect:" + adminPath + "/sys/ipFireWall/?repage";
	}
	
	/**
	 * @Title: deleteAll 
	 * @Description: 批量删除
	 * @param ids
	 * @param redirectAttributes
	 * @return
	 * @author com.mhout.xyb
	 */
	@RequiresPermissions("sys:ipFireWall:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split("\\,");
		for(String id : idArray){
			IpFireWall ipFireWall = ipFireWallService.get(id);
			ipFireWallService.delete(ipFireWall);
		}
		addMessage(redirectAttributes, "删除成功");
		return "redirect:" + adminPath + "/sys/ipFireWall/?repage";
	}
	
}
