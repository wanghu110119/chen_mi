package com.mht.modules.ident.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mht.common.persistence.Page;
import com.mht.common.utils.StringUtils;
import com.mht.common.web.BaseController;
import com.mht.modules.ident.entity.Apply;
import com.mht.modules.ident.service.AutService;

/**
 * @ClassName: ApplyController
 * @Description: 认证Controller
 * @author com.mhout.xyb
 * @date 2017年3月23日 上午11:38:40
 * @version 1.0.0
 */
@Controller
@RequestMapping(value = "${adminPath}/ident/aut")
public class AutController extends BaseController {

    @Autowired
    private AutService autService;

    @ModelAttribute("apply")
    public Apply get(@RequestParam(required = false) String id) {
        if (StringUtils.isNotBlank(id)) {
            return autService.getApply(id);
        } else {
            return new Apply();
        }
    }

    /**
     * @Title: list
     * @Description: 认证应用列表
     * @param apply
     *            应用数据
     * @param request
     * @param response
     * @param model
     * @return
     * @author com.mhout.xyb
     */
    @RequiresPermissions("ident:aut:list")
    @RequestMapping(value = { "list", "" })
    public String list(Apply apply, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<Apply> page = autService.find(new Page<Apply>(request, response), apply);
        model.addAttribute("page", page);
        return "modules/ident/identAutList";
    }

    /**
     * @Title: form
     * @Description: 查看，增加，编辑报告表单页面
     * @param apply
     * @param model
     * @return
     * @author com.mhout.xyb
     */
    // @RequiresPermissions(value={"ident:aut:view","ident:aut:add","ident:aut:edit"},logical=Logical.OR)
    @RequestMapping(value = "form")
    public String form(Apply apply, Model model) {
        model.addAttribute("apply", apply);
        return "modules/ident/identAutForm";
    }

    /**
     * @Title: save
     * @Description: 保存/编辑 应用认证
     * @param apply
     * @param model
     * @param redirectAttributes
     * @return
     * @author com.mhout.xyb
     */
    @RequiresPermissions(value = { "ident:aut:add", "ident:aut:edit" }, logical = Logical.OR)
    @RequestMapping(value = "save")
    public String save(Apply apply, Model model, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, apply)) {
            return form(apply, model);
        }
        autService.save(apply);
        addMessage(redirectAttributes, "保存应用认证'" + apply.getName() + "'成功");
        return "redirect:" + adminPath + "/ident/aut/?repage";
    }

    /**
     * @Title: delete
     * @Description: 单个删除
     * @param apply
     * @param redirectAttributes
     * @return
     * @author com.mhout.xyb
     */
    @RequiresPermissions("ident:aut:del")
    @RequestMapping(value = "delete")
    public String delete(Apply apply, RedirectAttributes redirectAttributes) {
        autService.delete(apply);
        addMessage(redirectAttributes, "删除应用成功");
        return "redirect:" + adminPath + "/ident/aut/?repage";
    }

    /**
     * @Title: deleteAll
     * @Description: 批量删除
     * @param ids
     * @param redirectAttributes
     * @return
     * @author com.mhout.xyb
     */
    @RequiresPermissions("ident:aut:del")
    @RequestMapping(value = "deleteAll")
    public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
        String idArray[] = ids.split("\\,");
        for (String id : idArray) {
            autService.delete(autService.get(id));
        }
        addMessage(redirectAttributes, "删除应用成功");
        return "redirect:" + adminPath + "/ident/aut/?repage";
    }

}
