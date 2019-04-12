package com.mht.modules.unifiedauth.web;

import java.io.IOException;
import java.util.ArrayList;
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
import com.mht.common.utils.StringUtils;
import com.mht.common.web.BaseController;
import com.mht.modules.sys.entity.Post;
import com.mht.modules.sys.entity.Role;
import com.mht.modules.unifiedauth.entity.AuthOffice;
import com.mht.modules.unifiedauth.entity.AuthPost;
import com.mht.modules.unifiedauth.entity.AuthRole;
import com.mht.modules.unifiedauth.service.AuthPostService;

/**
 * 
 * @ClassName: AuthPostController
 * @Description: 岗位授权
 * @author com.mhout.zjh
 * @date 2017年4月6日 上午9:01:34 
 * @version 1.0.0
 */
@Controller
@RequestMapping(value = "${adminPath}/unifiedAuth/post")
public class AuthPostController extends BaseController {

    @Autowired
    private AuthPostService authPostService;

    /**
     * @Title: index
     * @Description: TODO用户授权index页面
     * @param model
     * @return
     * @author com.mhout.sx
     */
    @RequiresPermissions("unifiedAuth:post:index")
    @RequestMapping(value = { "" })
    public String index(Model model) {
        return "modules/unifiedauth/postIndex";
    }

    /**
     * @Title: tree
     * @Description: TODO获取组织机构和用户树，树结构懒加载
     * @param id
     * @param request
     * @return
     * @author com.mhout.sx
     */
    @RequiresPermissions("unifiedAuth:post:index")
    @RequestMapping(value = "/treeData", method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody
    public Object tree(@RequestParam(value = "id", required = false) String id, HttpServletRequest request) {
        return this.authPostService.getTree(id);
    }

    /**
     */
    @RequiresPermissions("unifiedAuth:post:index")
    @RequestMapping(value = { "list" })
    public String list(Post post, Model model) {
    	List<AuthPost> list = null;
		if(StringUtils.isBlank(post.getId())){
			list = new ArrayList<>();
			post = new Post();
			this.addMessage(model, "角色信息错误");
		}else{
			list = this.authPostService.findByPost(post.getId());
		}
		model.addAttribute("post", post);
		model.addAttribute("list", list);
		return "modules/unifiedauth/postAppList";
    }

    /**
     * @Title: save 
     * @Description: TODO
     * @param authPostsForm
     * @param postId
     * @return
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     * @author com.mhout.zjh
     */
    @RequiresPermissions("unifiedAuth:user:save")
    @RequestMapping(value = { "save" })
    @ResponseBody
    public AjaxJson save(String authPostsForm, String closePosts,Post post)
            throws JsonParseException, JsonMappingException, IOException {
    	authPostsForm = StringEscapeUtils.unescapeHtml4(authPostsForm);
    	closePosts = StringEscapeUtils.unescapeHtml4(closePosts);
        ObjectMapper jacksonMapper = new ObjectMapper();
        jacksonMapper.configure(Feature.ALLOW_SINGLE_QUOTES, true);
        jacksonMapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        JavaType javaType = null;
        javaType = jacksonMapper.getTypeFactory().constructParametricType(List.class, AuthPost.class);
        List<AuthPost> list = jacksonMapper.readValue(authPostsForm, javaType);
        List<AuthPost> closelist = jacksonMapper.readValue(closePosts, javaType);
        this.authPostService.saveAuths(list,closelist, post.getId());
        AjaxJson json = new AjaxJson();
        json.setSuccess(true);
        json.setCode("0");
        json.setMsg("保存成功");
        return json;
    }

    /**
     * 
     * @Title: postList 
     * @Description: 根据岗位名称查询岗位
     * @param postName
     * @param model
     * @return
     * @author com.mhout.zjh
     */
    @RequestMapping(value = { "postList" })
    public String postList(String postName, Model model) {
        List<Post> list = this.authPostService.findPostsByName(postName);
        model.addAttribute("postName", postName);
        model.addAttribute("list", list);
        return "modules/unifiedauth/postSearchList";
    }

}
