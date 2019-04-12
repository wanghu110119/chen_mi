package com.mht.modules.account.web;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.mht.common.beanvalidator.BeanValidators;
import com.mht.common.config.Global;
import com.mht.common.persistence.Page;
import com.mht.common.utils.DateUtils;
import com.mht.common.utils.StringUtils;
import com.mht.common.utils.excel.ExportExcel;
import com.mht.common.utils.excel.ImportExcel;
import com.mht.common.web.BaseController;
import com.mht.modules.account.entity.Parents;
import com.mht.modules.account.entity.Student;
import com.mht.modules.account.entity.Teacher;
import com.mht.modules.account.service.CommonService;
import com.mht.modules.account.service.ParentsService;
import com.mht.modules.sys.entity.Office;
import com.mht.modules.sys.entity.User;
import com.mht.modules.sys.utils.DictUtils;
import com.mht.modules.sys.utils.UserUtils;

/**
 * @ClassName: ParentsController
 * @Description: 家长账号
 * @author com.mhout.sx
 * @date 2017年4月19日 下午3:19:11
 * @version 1.0.0
 */
@Controller
@RequestMapping(value = "${adminPath}/account/parents")
public class ParentsController extends BaseController {

    @Autowired
    private ParentsService parentsService;
    
    @Autowired
    private CommonService commonService;

    /**
     * @Title: index
     * @Description: TODO 家长账号首页
     * @param parents
     * @param model
     * @return
     * @author com.mhout.sx
     */
    @RequiresPermissions("account:parents:index")
    @RequestMapping(value = { "index" })
    public String index(Parents parents, Model model) {
        return "modules/account/parentsIndex";
    }

    /**
     * @Title: list
     * @Description: TODO 家长列表页
     * @param parents
     * @param request
     * @param response
     * @param model
     * @return
     * @author com.mhout.sx
     */
    @RequiresPermissions("account:parents:index")
    @RequestMapping(value = { "list", "" })
    public String list(Parents parents, HttpServletRequest request, HttpServletResponse response, Model model) {
        parents.setRoleType(Parents.ROLE_PARENTS);
        Page<Parents> page = parentsService.findParents(new Page<Parents>(request, response), parents);
        model.addAttribute("page", page);
        model.addAttribute("roleTypeName", Parents.ROLE_PARENTS_NAME);
        return "modules/account/parentsList";
    }

    /**
     * @Title: get
     * @Description: TODO 模型加载
     * @param id
     * @return
     * @author com.mhout.sx
     */
    @ModelAttribute
    public Parents get(@RequestParam(required = false) String id) {
        if (StringUtils.isNotBlank(id)) {
            return parentsService.getParents(id);
        } else {
            return new Parents();
        }
    }

    /**
     * @Title: form
     * @Description: TODO 表单页面
     * @param parents
     * @param model
     * @return
     * @author com.mhout.sx
     */
    @RequiresPermissions(value = { "account:parents:view", "account:parents:add",
            "account:parents:edit" }, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(Parents parents, Model model) {
        if (parents.getCompany() == null || parents.getCompany().getId() == null) {
            parents.setCompany(UserUtils.getUser().getCompany());
        }
        if (StringUtils.isBlank(parents.getRoleType())) {
            parents.setRoleType(Parents.ROLE_PARENTS);
        }
        parents.setRoleTypeName(Parents.ROLE_PARENTS_NAME);
        if (StringUtils.isBlank(parents.getOrigin())) {
            parents.setOrigin(CommonController.USER_DATA_ORIGIN_LOCAL);
        }
        parents.setOriginName(DictUtils.getDictLabel(parents.getOrigin(), CommonController.USER_DATA_ORIGIN, "本地"));
        commonService.setPswRule(model);
        model.addAttribute("parents", parents);
        return "modules/account/parentsForm";
    }

    /**
     * @Title: save
     * @Description: TODO 保存家长账号
     * @param parents
     * @param request
     * @param model
     * @param redirectAttributes
     * @return
     * @author com.mhout.sx
     */
    @RequiresPermissions(value = { "account:parents:add", "account:parents:edit" }, logical = Logical.OR)
    @RequestMapping(value = "save")
    public String save(Parents parents, HttpServletRequest request, Model model,
            RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, parents)) {
            return form(parents, model);
        }
        if (!"true".equals(checkUser(parents))) {
            addMessage(model, "保存家长'" + parents.getLoginName() + "'失败，登录名已存在");
            return form(parents, model);
        }
        parentsService.saveParents(parents, null);
        // 清除当前用户缓存
        User user = new User();
        BeanUtils.copyProperties(parents, user);
        user.setOldLoginName(user.getLoginName());
        UserUtils.clearCache(user);
        addMessage(redirectAttributes, "保存家长'" + parents.getLoginName() + "'成功");
        return "redirect:" + adminPath + "/account/parents/list?repage";
    }

    /**
     * @Title: delete
     * @Description: TODO 逻辑删除
     * @param parents
     * @param redirectAttributes
     * @return
     * @author com.mhout.sx
     */
    @RequiresPermissions("account:parents:del")
    @RequestMapping(value = "delete")
    public String delete(Parents parents, RedirectAttributes redirectAttributes) {
        if (Global.isDemoMode()) {
            addMessage(redirectAttributes, "演示模式，不允许操作！");
            return "redirect:" + adminPath + "/account/parents/list?repage";
        }
        if (UserUtils.getUser().getId().equals(parents.getId())) {
            addMessage(redirectAttributes, "删除用户失败, 不允许删除当前用户");
        } else if (User.isAdmin(parents.getId())) {
            addMessage(redirectAttributes, "删除用户失败, 不允许删除超级管理员用户");
        } else {
            parentsService.deleteParents(parents);
            addMessage(redirectAttributes, "删除家长成功");
        }
        return "redirect:" + adminPath + "/account/parents/list?repage";
    }

    /**
     * @Title: importFileTemplate
     * @Description: TODO 导入数据模板下载
     * @param response
     * @param redirectAttributes
     * @return
     * @author com.mhout.sx
     */
    @RequiresPermissions("account:parents:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            String fileName = "家长数据导入模板.xlsx";
            List<Parents> list = Lists.newArrayList();
            Parents test = new Parents();
            Office c = new Office();
            c.setName("XX学校");
            test.setCompany(c);
            test.setLoginName("abcd");
            test.setName("test");
            test.setRemarks("test");
            test.setGender("1");
            test.setNation("1");
            test.setCreateDate(new Date());
            test.setIdNo("5137011988123333");
            test.setStudentIdNo("222222222222221,222222222222222");
            list.add(test);
            new ExportExcel("家长数据", Parents.class, 2).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + adminPath + "/account/parents/list?repage";
    }

    /**
     * @Title: importFile
     * @Description: TODO excel导入数据
     * @param file
     * @param redirectAttributes
     * @return
     * @author com.mhout.sx
     */
    @RequiresPermissions("account:parents:import")
    @RequestMapping(value = "import", method = RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
        if (Global.isDemoMode()) {
            addMessage(redirectAttributes, "演示模式，不允许操作！");
            return "redirect:" + adminPath + "/account/parents/index";
        }
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<Parents> list = ei.getDataList(Parents.class);
            for (Parents user : list) {
                try {
                	if ("false".equals(checkParamUser(user))) {
                		if (StringUtils.isNotBlank(user.getLoginName())) {
                			failureMsg.append("<br/>导入家长 " + user.getLoginName() + "信息不符合相关规则; ");
                            failureNum++;
                		}
                        continue;
                	}
                    if ("true".equals(checkUser(user))) {
                        user.setNewPassword("123456");
                        user.setAccount(user.getLoginName());
                        user.setRoleType(Parents.ROLE_PARENTS);
                        user.setOrigin(CommonController.USER_DATA_ORIGIN_EXCEL);
                        BeanValidators.validateWithException(validator, user);
                        parentsService.saveParentsFromExcel(user);
                        successNum++;
                    } else {
                        failureMsg.append("<br/>导入学生 " + user.getLoginName() + " 的相关信息已存在; ");
                        failureNum++;
                    }
                } catch (ConstraintViolationException ex) {
                    failureMsg.append("<br/>登录名 " + user.getLoginName() + " 导入失败：");
                    List<String> messageList = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
                    for (String message : messageList) {
                        failureMsg.append(message + "; ");
                        failureNum++;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    failureMsg.append("<br/>登录名 " + user.getLoginName() + " 导入失败：" + ex.getMessage());
                }
            }
            if (failureNum > 0) {
                failureMsg.insert(0, "，失败 " + failureNum + " 条家长，导入信息如下：");
            }
            addMessage(redirectAttributes, "已成功导入 " + successNum + " 条家长" + failureMsg);
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入家长失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + adminPath + "/account/parents/index";
    }

    /**
     * @Title: exportFile
     * @Description: TODO 导出excel
     * @param parents
     * @param request
     * @param response
     * @param redirectAttributes
     * @return
     * @author com.mhout.sx
     */
    @RequiresPermissions("account:parents:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public String exportFile(Parents parents, HttpServletRequest request, HttpServletResponse response,
            RedirectAttributes redirectAttributes) {
        try {
            parents.setRoleType(Parents.ROLE_PARENTS);
            String fileName = "家长数据" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            Page<Parents> page = parentsService.findParents(new Page<Parents>(request, response, -1), parents);
            new ExportExcel("家长数据", Parents.class).setDataList(page.getList()).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            addMessage(redirectAttributes, "导出学生失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + adminPath + "/account/student/list";
    }
    
    private String checkUser(Parents parents) {
    	User user = new User();
    	BeanUtils.copyProperties(parents, user);
    	return UserUtils.checkUser(user);
    }
    
    /**
     * @Title: checkParamUser 
     * @Description: 参数校验
     * @param student
     * @return
     * @author com.mhout.xyb
     */
    private String checkParamUser(Parents parents) {
    	User user = new User();
    	BeanUtils.copyProperties(parents, user);
    	return UserUtils.checkParamUser(user);
    }

}
