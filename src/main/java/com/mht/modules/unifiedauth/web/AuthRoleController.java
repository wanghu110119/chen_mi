package com.mht.modules.unifiedauth.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mht.common.json.AjaxJson;
import com.mht.common.utils.StringUtils;
import com.mht.common.web.BaseController;
import com.mht.modules.sys.entity.Role;
import com.mht.modules.sys.service.SystemService;
import com.mht.modules.unifiedauth.entity.AuthRole;
import com.mht.modules.unifiedauth.service.AuthRoleService;

/**
 * 
 * @ClassName: AuthRoleController
 * @Description: 角色授权（应用）
 * @author com.mhout.zjh
 * @date 2017年3月29日 下午3:27:31
 * @version 1.0.0
 */
@Controller
@RequestMapping(value = "${adminPath}/unifiedAuth/role")
public class AuthRoleController extends BaseController {

	@Autowired
	private SystemService systemService;
	
	@Autowired
	private AuthRoleService authRoleService;
	/**
	 * 
	 * @Title: index 
	 * @Description: 跳转至授权页面
	 * @return
	 * @author com.mhout.zjh
	 */
	@RequiresPermissions("unifiedAuth:role:index")
	@RequestMapping(value = {""},method = RequestMethod.GET)
	public String index(Model model) {
		return "modules/unifiedauth/roleIndex";
	}
	/**
	 * 
	 * @Title: applyList 
	 * @Description: TODO
	 * @param model
	 * @param id
	 * @return
	 * @author com.mhout.zjh
	 */
	@RequiresPermissions("unifiedAuth:role:index")
	@RequestMapping(value = "/list")
	public String list(Model model,Role role){
		List<AuthRole> list = null;
		if(StringUtils.isBlank(role.getId())){
			list = new ArrayList<>();
			role = new Role();
			this.addMessage(model, "角色信息错误");
		}else{
//			role = systemService.getRole(role.getId());
			list = authRoleService.findListById(role.getId());
		}
		model.addAttribute("role", role);
		model.addAttribute("list", list);
		return "modules/unifiedauth/roleAppList";
	}
	/**
	 * 获取角色树
	 * @Title: treeData 
	 * @Description: TODO
	 * @param extId
	 * @param type
	 * @param grade
	 * @param isAll
	 * @param response
	 * @return
	 * @author com.mhout.zjh
	 */
	@RequiresPermissions("unifiedAuth:role:index")
	@ResponseBody
	@RequestMapping(value = "/treeData")
	public List<Map<String, Object>> treeData( ) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<Role> list = systemService.findRole(new Role());
		//添加根节点
		Role parent = new Role();
		parent.setId("0");
		parent.setName("ROOT");
		list.add(parent);
		for (int i=0; i<list.size(); i++){
			Role e = list.get(i);
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", e.getId());
			map.put("pId", parent.getId());
			map.put("pIds", parent.getId());
			map.put("name", e.getName());
			mapList.add(map);
		}
		return mapList;
	}
	/**
	 * 
	 * @Title: save 
	 * @Description: TODO
	 * @return
	 * @author com.mhout.zjh
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	@RequiresPermissions("unifiedAuth:role:save")
	@ResponseBody
	@RequestMapping(value = "/save",method=RequestMethod.POST)
	public AjaxJson save(String authRoleForm,Role role) throws JsonParseException, JsonMappingException, IOException{
			authRoleForm = StringEscapeUtils.unescapeHtml4(authRoleForm);
	        ObjectMapper jacksonMapper = new ObjectMapper();
	        jacksonMapper.configure(Feature.ALLOW_SINGLE_QUOTES, true);
	        jacksonMapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
	        JavaType javaType = null;
	        javaType = jacksonMapper.getTypeFactory().constructParametricType(List.class, AuthRole.class);
	        List<AuthRole> list = jacksonMapper.readValue(authRoleForm, javaType);
	        this.authRoleService.saveForm(list, role.getId());
	        AjaxJson json = new AjaxJson();
	        json.setSuccess(true);
	        json.setCode("0");
	        json.setMsg("保存成功");
	        return json;
	}
	
	/**
     * @Title: userList
     * @Description: TODO通过组织机构名字搜索，以便给组织机构授权
     * @param userName
     * @param model
     * @return
     * @author com.mhout.sx
     */
	@RequiresPermissions("unifiedAuth:role:save")
    @RequestMapping(value = { "/roleList" })
    public String roleList(String roleName, Model model) {
        List<Role> list = this.authRoleService.findRolesByName(StringUtils.trim(roleName));
        model.addAttribute("roleName", roleName);
        model.addAttribute("list", list);
        return "modules/unifiedauth/roleSearchList";
    }
}
