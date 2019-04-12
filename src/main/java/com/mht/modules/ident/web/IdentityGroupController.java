package com.mht.modules.ident.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.mht.common.utils.Collections3;
import com.mht.common.utils.StringUtils;
import com.mht.common.web.BaseController;
import com.mht.modules.ident.entity.IdentityGroup;
import com.mht.modules.ident.entity.StatictisData;
import com.mht.modules.ident.service.IdentityGroupService;
import com.mht.modules.sys.entity.Dict;
import com.mht.modules.sys.entity.User;
import com.mht.modules.sys.service.OfficeService;
import com.mht.modules.sys.service.SystemService;

/**
 * 
 * @ClassName: IdentityGroupController
 * @Description: 身份组管理
 * @author com.mhout.zjh
 * @date 2017年3月23日 下午1:36:47
 * @version 1.0.0
 */
@Controller
@RequestMapping(value = "${adminPath}/auth/identityGroup")
public class IdentityGroupController extends BaseController {

    @Autowired
    private IdentityGroupService identityGroupService;

    @Autowired
    private OfficeService officeService;

    @Autowired
    private SystemService systemService;

    /**
     * @Title: get
     * @Description: 所有方法前执行
     * @param id
     * @return
     * @author com.mhout.zjh
     */
    @ModelAttribute
    public IdentityGroup get(@RequestParam(required = false) String id) {
        IdentityGroup entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = identityGroupService.get(id);
        }
        if (entity == null) {
            entity = new IdentityGroup();
        }
        return entity;
    }

    /**
     * 
     * @Title: list
     * @Description: 查询列表
     * @param identityGroup
     * @param model
     * @return
     * @author com.mhout.zjh
     */
    @RequestMapping(value = { "/list", "" })
    public String list(IdentityGroup identityGroup, Model model) {
        List<IdentityGroup> list = identityGroupService.findList(identityGroup);
        model.addAttribute("list", list);
        return "modules/ident/identityGroupList";
    }

    /**
     * 
     * @Title: form
     * @Description: TODO
     * @param role
     * @param model
     * @return
     * @author com.mhout.zjh
     */
    @RequestMapping(value = "form")
    public String form(IdentityGroup identityGroup, Model model) {
        model.addAttribute("group", identityGroup);
        return "modules/ident/identityGroupForm";
    }

    /**
     * 验证组名是否有效
     * 
     * @param oldName
     * @param name
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "checkName")
    public String checkName(IdentityGroup identityGroup) {
        String flag = "false";
        IdentityGroup iden = this.identityGroupService.findByName(identityGroup.getGroupName());
        if (StringUtils.isBlank(identityGroup.getId())) {
            if (iden == null) {
                flag = "true";
            }
        } else {
            // 如果传过来的id不为空 并且通过名字查找的组的id与传过来的id相同 或者通过名字查找的用户为空 则可以使用这个组名
            if (iden == null || iden.getId().equals(identityGroup.getId())) {
                flag = "true";
            }
        }
        return flag;
    }

    /**
     * 
     * @Title: save
     * @Description: TODO
     * @return
     * @author com.mhout.zjh
     */
    @RequestMapping(value = "/save")
    public String save(IdentityGroup identityGroup, Model model, RedirectAttributes redirectAttributes) {
        // if(!UserUtils.getUser().isAdmin()){
        // addMessage(redirectAttributes, "越权操作，只有超级管理员才能修改此数据！");
        // return "redirect:" + adminPath + "/sys/role/?repage";
        // }
        // if(Global.isDemoMode()){
        // addMessage(redirectAttributes, "演示模式，不允许操作！");
        // return "redirect:" + adminPath + "/sys/role/?repage";
        // }
        if (!beanValidator(model, identityGroup)) {
            return list(identityGroup, model);
        }

        if (!"true".equals(checkName(identityGroup))) {
            addMessage(redirectAttributes, "保存身份组'" + identityGroup.getGroupName() + "'失败, 组名已存在");
            return list(identityGroup, model);
        }
        this.identityGroupService.save(identityGroup);
        addMessage(redirectAttributes, "保存身份组'" + identityGroup.getGroupName() + "'成功");
        return "redirect:" + adminPath + "/auth/identityGroup/?repage";
    }

    /**
     * 
     * @Title: delete
     * @Description: TODO
     * @param identityGroup
     * @param model
     * @return
     * @author com.mhout.zjh
     */
    @RequestMapping(value = "/delete")
    public String delete(String id, Model model) {
        this.identityGroupService.deleteById(id);
        addMessage(model, "删除身份组成功");
        return list(new IdentityGroup(), model);
    }

    /**
     * 
     * @Title: deleteAll
     * @Description: TODO
     * @param identityGroup
     * @param model
     * @return
     * @author com.mhout.zjh
     */
    @RequestMapping(value = "/deleteAll")
    public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
        String[] idsArray = ids.split("\\,");
        for (String id : idsArray) {
            this.identityGroupService.deleteById(id);
        }
        addMessage(redirectAttributes, "删除身份组成功");
        return "redirect:" + adminPath + "/auth/identityGroup?repage";
    }

    /**
     * 分配页面
     * 
     * @param identityGroup
     * @param model
     * @return
     */
    @RequestMapping(value = "/assign")
    public String assign(IdentityGroup identityGroup, Model model) {
        if (identityGroup != null) {
            List<User> userList = this.identityGroupService.findUserByIdentityGroup(identityGroup);
            model.addAttribute("group", identityGroup);
            model.addAttribute("userList", userList);
        } else {
            addMessage(model, "身份组信息错误");
        }
        return "modules/ident/identityGroupAssign";
    }

    /**
     * 角色分配 -- 打开角色分配对话框
     * 
     * @param identityGroup
     * @param model
     * @return
     */
    @RequestMapping(value = "/userToIdentityGroup")
    public String userToIdentityGroup(IdentityGroup identityGroup, Model model) {
        List<User> userList = this.identityGroupService.findUserByIdentityGroup(identityGroup);
        model.addAttribute("group", identityGroup);
        model.addAttribute("userList", userList);
        model.addAttribute("selectIds", Collections3.extractToString(userList, "id", ","));
        // model.addAttribute("officeList", officeService.findAll());
        return "modules/ident/selectUserToIdentityGroup";
    }

    @RequestMapping(value = "/getTeacherByOffice")
    @ResponseBody
    public Object getTeacherByOffice(String officeId) {
        List<User> list = this.identityGroupService.getTeacherByOffice(officeId);
        List<Map<String, Object>> mapList = Lists.newArrayList();
        for (int i = 0; i < list.size(); i++) {
            User e = list.get(i);
            Map<String, Object> map = Maps.newHashMap();
            map.put("id", e.getId());
            map.put("pId", "0");
            map.put("name", e.getName());
            mapList.add(map);
        }
        return mapList;
    }

    @RequestMapping(value = "/getStudentByOffice")
    @ResponseBody
    public Object getStudentByOffice(String officeId) {
        List<User> list = this.identityGroupService.getStudentByOffice(officeId);
        List<Map<String, Object>> mapList = Lists.newArrayList();
        for (int i = 0; i < list.size(); i++) {
            User e = list.get(i);
            Map<String, Object> map = Maps.newHashMap();
            map.put("id", e.getId());
            map.put("pId", "0");
            map.put("name", e.getName());
            mapList.add(map);
        }
        return mapList;
    }

    /**
     * 人员分配至身份组
     * 
     * @param role
     * @param idsArr
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "/assignUserToIdentityGroup")
    public String assignUserToIdentityGroup(IdentityGroup identityGroup, String[] idsArr,
            RedirectAttributes redirectAttributes) {
        // if(Global.isDemoMode()){
        // addMessage(redirectAttributes, "演示模式，不允许操作！");
        // return "redirect:" + adminPath + "/sys/role/assign?id="+role.getId();
        // }
        StringBuilder msg = new StringBuilder();
        int newNum = 0;
        for (int i = 0; i < idsArr.length; i++) {
            User user = systemService.getUser(idsArr[i]);
            if (user != null) {
                int rowsNum = this.identityGroupService.assignUserToIdentityGroup(identityGroup, user);
                if (rowsNum == 1) {
                    msg.append("<br/>新增用户【" + user.getName() + "】到【" + identityGroup.getGroupName() + "】！");
                    newNum++;
                }
            }
        }
        addMessage(redirectAttributes, "已成功分配 " + newNum + " 个用户" + msg);
        return "redirect:" + adminPath + "/auth/identityGroup/assign?id=" + identityGroup.getId();
    }

    /**
     * 从身份组中移除用户
     * 
     * @param userId
     * @param roleId
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "/outIdentityGroup")
    public String outIdentityGroup(String userId, IdentityGroup identityGroup, RedirectAttributes redirectAttributes) {
        // if(Global.isDemoMode()){
        // addMessage(redirectAttributes, "演示模式，不允许操作！");
        // return "redirect:" + adminPath + "/sys/role/assign?id="+roleId;
        // }
        User user = this.systemService.getUser(userId);
        int rowsNum = this.identityGroupService.outIdentityGroup(identityGroup, user);
        if (rowsNum != 1) {
            addMessage(redirectAttributes, "用户【" + user.getName() + "】从【" + identityGroup.getGroupName() + "】中移除失败！");
        } else {
            addMessage(redirectAttributes, "用户【" + user.getName() + "】从【" + identityGroup.getGroupName() + "】中移除成功！");
        }
        return "redirect:" + adminPath + "/auth/identityGroup/assign?id=" + identityGroup.getId();
    }

    /**
     * 
     * @Title: statistics
     * @Description: TODO
     * @param request
     * @param response
     * @param model
     * @return
     * @author com.mhout.zjh
     */
    @RequestMapping(value = { "/statistics" })
    public String statistics(IdentityGroup identityGroup, HttpServletRequest request, HttpServletResponse response,
            Model model) {
        List<IdentityGroup> groups = this.identityGroupService.findList(identityGroup);
        List<Dict> types = new ArrayList<>();
        types.add(new Dict("week", "本周"));
        types.add(new Dict("month", "本月"));
        types.add(new Dict("year", "本年"));
        model.addAttribute("groupList", groups);
        model.addAttribute("types", types);
        return "modules/ident/statistics";
    }

    /**
     * 
     * @Title: getStatictisData
     * @Description: TODO
     * @return
     * @author com.mhout.zjh
     */
    @RequestMapping(value = "/getStatictisData")
    @ResponseBody
    public List<StatictisData> getStatictisData(String dateType, String action, String group) {
        List<StatictisData> list = this.identityGroupService.findRecordList(dateType, action, group);
        return list;
    }
}
