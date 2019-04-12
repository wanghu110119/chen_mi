/**
 * Copyright &copy; 2015-2020 <a href="http://www.mht.org/">mht</a> All rights reserved.
 */
package com.mht.modules.sys.web;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mht.common.config.Global;
import com.mht.common.utils.StringUtils;
import com.mht.common.web.BaseController;
import com.mht.modules.sys.entity.Office;
import com.mht.modules.sys.entity.Post;
import com.mht.modules.sys.entity.User;
import com.mht.modules.sys.service.OfficeService;
import com.mht.modules.sys.utils.DictUtils;
import com.mht.modules.sys.utils.UserUtils;

/**
 * 机构Controller
 * 
 * @author mht
 * @version 2013-5-15
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/office")
public class OfficeController extends BaseController {

    @Autowired
    private OfficeService officeService;

    /**
     * @Title: postTree
     * @Description: TODO 新增、修改部门信息时显示选择岗位页面
     * @param id
     * @param model
     * @return
     * @author com.mhout.sx
     */
    @RequiresPermissions(value = { "sys:office:add", "sys:office:edit" }, logical = Logical.OR)
    @RequestMapping(value = "postTree")
    public String postTree(String id, Model model) {
        model.addAttribute("id", id);
        return "modules/sys/officePostTree";
    }

    /**
     * @Title: postTreeData
     * @Description: TODO 显示岗位树结构，并选中已拥有的
     * @param id
     * @return
     * @author com.mhout.sx
     */
    @ResponseBody
    @RequestMapping(value = "postTreeData")
    public List<Map<String, Object>> postTreeData(@RequestParam(required = false) String id) {
        List<Map<String, Object>> mapList = Lists.newArrayList();
        List<Post> list = officeService.findPostList(id);
        for (int i = 0; i < list.size(); i++) {
            Post e = list.get(i);
            Map<String, Object> map = Maps.newHashMap();
            map.put("id", e.getId());
            map.put("pId", e.getParentId());
            map.put("pIds", e.getParentIds());
            map.put("name", e.getName());
            if (StringUtils.isBlank(e.getOtherId())) {
                map.put("checked", false);
            } else {
                map.put("checked", true);
            }
            mapList.add(map);
        }
        return mapList;
    }

    @ModelAttribute("office")
    public Office get(@RequestParam(required = false) String id) {
        if (StringUtils.isNotBlank(id)) {
            return officeService.get(id);
        } else {
            return new Office();
        }
    }

    @RequiresPermissions("sys:office:index")
    @RequestMapping(value = { "" })
    public String index(Office office, Model model) {
        // model.addAttribute("list", officeService.findAll());
        return "modules/sys/officeIndex";
    }

    @RequiresPermissions("sys:office:index")
    @RequestMapping(value = { "list" })
    public String list(Office office, Model model) {
        if (office == null || office.getParentIds() == null) {
            model.addAttribute("list", officeService.findList(false, null));
        } else {
            model.addAttribute("list", officeService.findList(office));
        }
        return "modules/sys/officeList";
    }

    @RequiresPermissions(value = { "sys:office:view", "sys:office:add", "sys:office:edit" }, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(Office office, Model model) {
        User user = UserUtils.getUser();
        if (office.getParent() == null || office.getParent().getId() == null) {
            // office.setParent(user.getOffice());
        }
        office.setParent(officeService.get(office.getParent().getId()));
        if (office.getArea() == null) {
            // office.setArea(user.getOffice().getArea());
        }
        // 自动获取排序号
        if (StringUtils.isBlank(office.getId()) && office.getParent() != null) {
            int size = 0;
            List<Office> list = officeService.findAll();
            for (int i = 0; i < list.size(); i++) {
                Office e = list.get(i);
                if (e.getParent() != null && e.getParent().getId() != null
                        && e.getParent().getId().equals(office.getParent().getId())) {
                    size++;
                }
            }
            office.setCode(office.getParent().getCode()
                    + StringUtils.leftPad(String.valueOf(size > 0 ? size + 1 : 1), 3, "0"));
        }
        // 获取岗位信息
        if (!StringUtils.isBlank(office.getId())) {
            List<Post> posts = officeService.findPosts(office.getId());
            List<String> ids = posts.stream().map(Post::getId).collect(Collectors.toList());
            List<String> names = posts.stream().map(Post::getName).collect(Collectors.toList());
            office.setPostIds(String.join(",", ids));
            office.setPostNames(String.join(",", names));
        }
        model.addAttribute("office", office);
        return "modules/sys/officeForm";
    }

    @RequiresPermissions(value = { "sys:office:add", "sys:office:edit" }, logical = Logical.OR)
    @RequestMapping(value = "save")
    public String save(Office office, Model model, RedirectAttributes redirectAttributes) {
        if (Global.isDemoMode()) {
            addMessage(redirectAttributes, "演示模式，不允许操作！");
            return "redirect:" + adminPath + "/sys/office/";
        }
        if (!beanValidator(model, office)) {
            return form(office, model);
        }
        officeService.save(office);

        if (office.getChildDeptList() != null) {
            Office childOffice = null;
            for (String id : office.getChildDeptList()) {
                childOffice = new Office();
                childOffice.setName(DictUtils.getDictLabel(id, "sys_office_common", "未知"));
                childOffice.setParent(office);
                childOffice.setArea(office.getArea());
                childOffice.setType("2");
                childOffice.setGrade(String.valueOf(Integer.valueOf(office.getGrade()) + 1));
                childOffice.setUseable(Global.YES);
                officeService.save(childOffice);
            }
        }
        // 保存岗位与部门对应关系
        officeService.savePost(office);

        addMessage(redirectAttributes, "保存机构'" + office.getName() + "'成功");
        String id = "0".equals(office.getParentId()) ? "" : office.getParentId();
        return "redirect:" + adminPath + "/sys/office/list?id=" + id + "&parentIds=" + office.getParentIds();
    }

    @RequiresPermissions("sys:office:del")
    @RequestMapping(value = "delete")
    public String delete(Office office, RedirectAttributes redirectAttributes) {
        if (Global.isDemoMode()) {
            addMessage(redirectAttributes, "演示模式，不允许操作！");
            return "redirect:" + adminPath + "/sys/office/list";
        }
        // if (Office.isRoot(id)){
        // addMessage(redirectAttributes, "删除机构失败, 不允许删除顶级机构或编号空");
        // }else{
        officeService.delete(office);
        addMessage(redirectAttributes, "删除机构成功");
        // }
        return "redirect:" + adminPath + "/sys/office/list?id=" + office.getParentId() + "&parentIds="
                + office.getParentIds();
    }

    /**
     * 获取机构JSON数据。
     * 
     * @param extId
     *            排除的ID
     * @param type
     *            类型（1：公司；2：部门/小组/其它：3：用户）
     * @param grade
     *            显示级别
     * @param response
     * @return
     */
    @RequiresPermissions("user")
    @ResponseBody
    @RequestMapping(value = "treeData")
    public List<Map<String, Object>> treeData(@RequestParam(required = false) String extId,
            @RequestParam(required = false) String type, @RequestParam(required = false) Long grade,
            @RequestParam(required = false) Boolean isAll,@RequestParam(required = false) String name,
            HttpServletResponse response) {
        List<Map<String, Object>> mapList = Lists.newArrayList();
        List<Office> list = officeService.findList(isAll, name);
        for (int i = 0; i < list.size(); i++) {
            Office e = list.get(i);
            if ((StringUtils.isBlank(extId)
                    || (extId != null && !extId.equals(e.getId()) && e.getParentIds().indexOf("," + extId + ",") == -1))
                    && (type == null || (type != null && (type.equals("1") ? type.equals(e.getType()) : true)))
                    && (grade == null || (grade != null && Integer.parseInt(e.getGrade()) <= grade.intValue()))
                    && Global.YES.equals(e.getUseable())) {
                Map<String, Object> map = Maps.newHashMap();
                map.put("id", e.getId());
                map.put("pId", e.getParentId());
                map.put("pIds", e.getParentIds());
                map.put("name", e.getName());
                if (type != null && "3".equals(type)) {
                    map.put("isParent", true);
                }
                if ("0".equals(e.getParentId())) {
                    map.put("open", true);
                }
                mapList.add(map);
            }
        }
        return mapList;
    }

    /**
     * @Title: treeData2
     * @Description: TODO 获取教学类组织机构树
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
    @RequestMapping(value = "treeData2")
    public List<Map<String, Object>> treeData2(@RequestParam(required = false) String extId,
            @RequestParam(required = false) String type, @RequestParam(required = false) Long grade,
            @RequestParam(required = false) Boolean isAll,@RequestParam(required = false) String name,
            HttpServletResponse response) {
        List<Map<String, Object>> mapList = Lists.newArrayList();
        Office office = new Office();
        office.setName(name);
        List<Office> list = officeService.findListForStudent(office);
        for (int i = 0; i < list.size(); i++) {
            Office e = list.get(i);
            Map<String, Object> map = Maps.newHashMap();
            map.put("id", e.getId());
            map.put("pId", e.getParentId());
            map.put("pIds", e.getParentIds());
            map.put("name", e.getName());
            if (type != null && "3".equals(type)) {
                map.put("isParent", true);
            }
            if ("0".equals(e.getParentId())) {
                map.put("open", true);
            }
            mapList.add(map);
        }
        return mapList;
    }
}
