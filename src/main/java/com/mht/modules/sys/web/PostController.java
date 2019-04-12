
package com.mht.modules.sys.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mht.common.config.Global;
import com.mht.common.persistence.Page;
import com.mht.common.utils.StringUtils;
import com.mht.common.web.BaseController;
import com.mht.modules.sys.entity.Office;
import com.mht.modules.sys.entity.Post;
import com.mht.modules.sys.service.OfficeService;
import com.mht.modules.sys.service.PostService;

/**
 * @ClassName: PostController
 * @Description: 岗位
 * @author com.mhout.sx
 * @date 2017年4月5日 下午12:51:56
 * @version 1.0.0
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/post")
public class PostController extends BaseController {

    @Autowired
    private PostService postService;

    @Autowired
    private OfficeService officeService;

    @RequestMapping(value = { "" })
    public String index(Model model) {
        return "modules/sys/postIndex";
    }

    /**
     * @Title: list
     * @Description: TODO 获取岗位列表，由前台加载为树结构
     * @param post
     * @param model
     * @return
     * @author com.mhout.sx
     */
    @RequiresPermissions("sys:post:list")
    @RequestMapping(value = { "list" })
    public String list(Post post, HttpServletRequest request, HttpServletResponse response, Model model) {
        // if (post == null || post.getParentIds() == null) {
        // model.addAttribute("page", postService.findList(new
        // Page<User>(request, response), false));
        // } else {
        model.addAttribute("page", postService.findList(new Page<Post>(request, response), post));
        // }
        return "modules/sys/postList";
    }

    /**
     * @Title: form
     * @Description: TODO 显示表单页面，数据加载
     * @param post
     * @param model
     * @return
     * @author com.mhout.sx
     */
    @RequiresPermissions(value = { "sys:post:view", "sys:post:add", "sys:post:edit" }, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(Post post, Model model) {
        // User user = UserUtils.getUser();
        if (post.getParent() == null || post.getParent().getId() == null) {
            post = postService.get(post.getId());
        }
        post.setParent(postService.get(post.getParent().getId()));
        // 自动获取排序号
        if (StringUtils.isBlank(post.getId()) && post.getParent() != null) {
            int size = 0;
            List<Post> list = postService.findList(true);
            for (int i = 0; i < list.size(); i++) {
                Post e = list.get(i);
                if (e.getParent() != null && e.getParent().getId() != null
                        && e.getParent().getId().equals(post.getParent().getId())) {
                    size++;
                }
            }
            post.setCode(
                    post.getParent().getCode() + StringUtils.leftPad(String.valueOf(size > 0 ? size + 1 : 1), 3, "0"));
        }
        model.addAttribute("post", post);
        return "modules/sys/postForm";
    }

    /**
     * @Title: save
     * @Description: TODO 新增、修改岗位
     * @param post
     * @param model
     * @param redirectAttributes
     * @return
     * @author com.mhout.sx
     */
    @RequiresPermissions(value = { "sys:post:add", "sys:post:edit" }, logical = Logical.OR)
    @RequestMapping(value = "save")
    public String save(Post post, Model model, RedirectAttributes redirectAttributes) {
        if (Global.isDemoMode()) {
            addMessage(redirectAttributes, "演示模式，不允许操作！");
            return "redirect:" + adminPath + "/sys/post/list";
        }
        if (!beanValidator(model, post)) {
            return form(post, model);
        }
        postService.save(post);
        addMessage(redirectAttributes, "保存岗位'" + post.getName() + "'成功");
        // String id = "0".equals(post.getParentId()) ? "" : post.getParentId();
        return "redirect:" + adminPath + "/sys/post/";
    }

    /**
     * @Title: delete
     * @Description: TODO 逻辑删除岗位
     * @param post
     * @param redirectAttributes
     * @return
     * @author com.mhout.sx
     */
    @RequiresPermissions("sys:post:del")
    @RequestMapping(value = "delete")
    public String delete(Post post, RedirectAttributes redirectAttributes) {
        if (Global.isDemoMode()) {
            addMessage(redirectAttributes, "演示模式，不允许操作！");
            return "redirect:" + adminPath + "/sys/post/";
        }
        postService.deleteByLogic(post);
        addMessage(redirectAttributes, "删除岗位成功");
        return "redirect:" + adminPath + "/sys/post/";
    }

    /**
     * @Title: tree
     * @Description: TODO 懒加载树
     * @param id
     * @param request
     * @return
     * @author com.mhout.sx
     */
    @RequiresPermissions("user")
    @RequestMapping(value = "/tree", method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseBody
    public Object tree(@RequestParam(value = "id", required = false) String id, HttpServletRequest request) {
        return this.postService.getTree(id);
    }

    /**
     * @Title: treeData
     * @Description: TODO 获取所有岗位并并转换为树，前台一次加载显示
     * @param extId
     * @param type
     * @param grade
     * @param isAll
     * @param response
     * @return
     * @author com.mhout.sx
     */
    @RequiresPermissions("user")
    @ResponseBody
    @RequestMapping(value = "treeData")
    public List<Map<String, Object>> treeData(@RequestParam(required = false) String extId,
            @RequestParam(required = false) String type, @RequestParam(required = false) Long grade,
            @RequestParam(required = false) Boolean isAll, HttpServletResponse response) {
        List<Map<String, Object>> mapList = Lists.newArrayList();
        List<Post> list = postService.findList(false);
        for (int i = 0; i < list.size(); i++) {
            Post e = list.get(i);
            if ((StringUtils.isBlank(extId) || (extId != null && !extId.equals(e.getId())
                    && e.getParentIds().indexOf("," + extId + ",") == -1))) {
                Map<String, Object> map = Maps.newHashMap();
                map.put("id", e.getId());
                map.put("pId", e.getParentId());
                map.put("pIds", e.getParentIds());
                map.put("name", e.getName());
                if (type != null && "3".equals(type)) {
                    map.put("isParent", true);
                }
                mapList.add(map);
            }
        }
        return mapList;
    }

    @RequiresPermissions("user")
    @ResponseBody
    @RequestMapping(value = "treeDataByOffice")
    public List<Map<String, Object>> treeDataByOffice(@RequestParam(required = true) String officeId,
            HttpServletResponse response) {
        List<Map<String, Object>> mapList = Lists.newArrayList();
        Office office = officeService.get(officeId);
        List<Post> list = this.postService.findByOffice(office.getId());
        Post parent = new Post();
        parent.setId("0");
        parent.setName(office.getName());
        list.add(parent);
        for (int i = 0; i < list.size(); i++) {
            Post e = list.get(i);
            Map<String, Object> map = Maps.newHashMap();
            map.put("id", e.getId());
            map.put("pId", parent.getId());
            map.put("pIds", parent.getId());
            map.put("name", e.getName());
            mapList.add(map);
        }
        return mapList;
    }
}
