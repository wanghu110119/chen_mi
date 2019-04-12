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
import com.mht.modules.ident.entity.IdentityGroup;
import com.mht.modules.ident.service.IdentityGroupService;
import com.mht.modules.unifiedauth.entity.AuthIdentityGroup;
import com.mht.modules.unifiedauth.service.AuthIdentityGroupService;

/**
 * 
 * @ClassName: AuthIdentityGroupController
 * @Description: 用户组授权
 * @author com.mhout.zjh
 * @date 2017年3月30日 下午4:55:50 
 * @version 1.0.0
 */
@Controller
@RequestMapping(value = "${adminPath}/unifiedAuth/identityGroup")
public class AuthIdentityGroupController extends BaseController {

	@Autowired
	private IdentityGroupService identityGroupService;
	
	@Autowired
	private AuthIdentityGroupService authIdentityGroupService;
	/**
	 * 
	 * @Title: index 
	 * @Description: 跳转至授权页面
	 * @return
	 * @author com.mhout.zjh
	 */
	@RequiresPermissions("unifiedAuth:identityGroup:index")
	@RequestMapping(value = {""},method = RequestMethod.GET)
	public String index(Model model) {
		return "modules/unifiedauth/identityGroupIndex";
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
	@RequiresPermissions("unifiedAuth:identityGroup:index")
	@RequestMapping(value = "/list")
	public String list(Model model,IdentityGroup identityGroup){
		List<AuthIdentityGroup> list = null;
		if(StringUtils.isBlank(identityGroup.getId())){
			list = new ArrayList<>();
			identityGroup = new IdentityGroup();
			this.addMessage(model, "用户组信息错误");
		}else{
			list = authIdentityGroupService.findListByIdentityGroupId(identityGroup.getId());
		}
		model.addAttribute("identityGroup", identityGroup);
		model.addAttribute("list", list);
		return "modules/unifiedauth/identityGroupAppList";
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
	@RequiresPermissions("unifiedAuth:identityGroup:index")
	@ResponseBody
	@RequestMapping(value = "/treeData")
	public List<Map<String, Object>> treeData( ) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<IdentityGroup> list = identityGroupService.findList(new IdentityGroup());
		//添加根节点
		IdentityGroup parent = new IdentityGroup();
		parent.setId("0");
		parent.setGroupName("ROOT");
		list.add(parent);
		for (int i=0; i<list.size(); i++){
			IdentityGroup e = list.get(i);
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", e.getId());
			map.put("pId", parent.getId());
			map.put("pIds", parent.getId());
			map.put("name", e.getGroupName());
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
	@RequiresPermissions("unifiedAuth:identityGroup:save")
	@ResponseBody
	@RequestMapping(value = "/save",method=RequestMethod.POST)
	public AjaxJson save(String authForm,IdentityGroup identityGroup) throws JsonParseException, JsonMappingException, IOException{
			authForm = StringEscapeUtils.unescapeHtml4(authForm);
	        ObjectMapper jacksonMapper = new ObjectMapper();
	        jacksonMapper.configure(Feature.ALLOW_SINGLE_QUOTES, true);
	        jacksonMapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
	        JavaType javaType = null;
	        javaType = jacksonMapper.getTypeFactory().constructParametricType(List.class, AuthIdentityGroup.class);
	        List<AuthIdentityGroup> list = jacksonMapper.readValue(authForm, javaType);
	        this.authIdentityGroupService.saveForm(list, identityGroup.getId());
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
	@RequiresPermissions("unifiedAuth:identityGroup:save")
    @RequestMapping(value = { "/identityGroupList" })
    public String roleList(String searchName, Model model) {
        List<IdentityGroup> list = this.authIdentityGroupService.findIdentityGroupsByName(StringUtils.trim(searchName));
        model.addAttribute("groupName", searchName);
        model.addAttribute("list", list);
        return "modules/unifiedauth/identityGroupSearchList";
    }
}
