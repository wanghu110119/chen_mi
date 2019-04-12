package com.mht.modules.unifiedauth.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mht.common.json.AjaxJson;
import com.mht.common.web.BaseController;
import com.mht.modules.sys.entity.User;
import com.mht.modules.unifiedauth.entity.AuthPost;
import com.mht.modules.unifiedauth.entity.AuthUser;
import com.mht.modules.unifiedauth.service.AuthUserService;

/**
 * @ClassName: AuthUserController
 * @Description: 用户授权应用权限
 * @author com.mhout.sx
 * @date 2017年3月30日 下午2:15:48
 * @version 1.0.0
 */
@Controller
@RequestMapping(value = "${adminPath}/unifiedAuth/user")
public class AuthUserController extends BaseController {

    @Autowired
    private AuthUserService authUserService;

    /**
     * @Title: index
     * @Description: TODO用户授权index页面
     * @param model
     * @return
     * @author com.mhout.sx
     */
    @RequiresPermissions("unifiedAuth:user:index")
    @RequestMapping(value = { "" })
    public String index(Model model) {
        return "modules/unifiedauth/userIndex";
    }

    /**
     * @Title: tree
     * @Description: TODO获取组织机构和用户树，树结构懒加载
     * @param id
     * @param request
     * @return
     * @author com.mhout.sx
     */
    @RequestMapping(value = "/tree", method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody
    public Object tree(@RequestParam(value = "id", required = false) String id,
    		@RequestParam(value = "type", required = false) String type, HttpServletRequest request) {
        return this.authUserService.getTree(id, type);
    }

    /**
     * @Title: list
     * @Description: TODO应用权限列表页面
     * @param authUser
     * @param model
     * @return
     * @author com.mhout.sx
     */
    @RequiresPermissions("unifiedAuth:user:index")
    @RequestMapping(value = { "list" })
    public String list(AuthUser authUser, Model model) {
        List<AuthUser> list = this.authUserService.findListByUser(authUser);
        model.addAttribute("user", authUser.getUser());
        model.addAttribute("apps", list);
        return "modules/unifiedauth/userAppList";
    }

    /**
     * @Title: save
     * @Description: TODO ajax保存用户应用权限
     * @param authOffices
     * @param userId
     * @return
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     * @author com.mhout.sx
     */
    @RequiresPermissions("unifiedAuth:user:save")
    @RequestMapping(value = { "save" })
    @ResponseBody
    public AjaxJson save(String authOffices,String closeUsers, String userId)
            throws JsonParseException, JsonMappingException, IOException {
        authOffices = StringEscapeUtils.unescapeHtml4(authOffices);
        closeUsers = StringEscapeUtils.unescapeHtml4(closeUsers);
        ObjectMapper jacksonMapper = new ObjectMapper();
        jacksonMapper.configure(Feature.ALLOW_SINGLE_QUOTES, true);
        jacksonMapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        JavaType javaType = null;
        javaType = jacksonMapper.getTypeFactory().constructParametricType(List.class, AuthUser.class);
        List<AuthUser> list = jacksonMapper.readValue(authOffices, javaType);
        List<AuthUser> closelist = jacksonMapper.readValue(closeUsers, javaType);
        this.authUserService.saveAuths(list,closelist, userId);
        AjaxJson json = new AjaxJson();
        json.setSuccess(true);
        json.setCode("0");
        json.setMsg("保存成功");
        return json;
    }

    /**
     * @Title: userList
     * @Description: TODO通过用户名字搜索用户，以便给用户授权
     * @param userName
     * @param model
     * @return
     * @author com.mhout.sx
     */
    @RequestMapping(value = { "userList" })
    public String userList(String userName, Model model) {
        List<User> list = this.authUserService.findUserByName(userName);
        model.addAttribute("userName", userName);
        model.addAttribute("users", list);
        return "modules/unifiedauth/userSearchList";
    }

}
