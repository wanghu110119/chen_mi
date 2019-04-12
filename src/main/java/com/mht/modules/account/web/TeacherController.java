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
import com.mht.modules.account.entity.Student;
import com.mht.modules.account.entity.Teacher;
import com.mht.modules.account.service.CommonService;
import com.mht.modules.account.service.TeacherService;
import com.mht.modules.sys.entity.Office;
import com.mht.modules.sys.entity.User;
import com.mht.modules.sys.utils.DictUtils;
import com.mht.modules.sys.utils.UserUtils;

/**
 * @ClassName: TeacherController
 * @Description: 教工账号
 * @author com.mhout.sx
 * @date 2017年4月19日 下午4:00:18
 * @version 1.0.0
 */
@Controller
@RequestMapping(value = "${adminPath}/account/teacher")
public class TeacherController extends BaseController {

    @Autowired
    private TeacherService teacherService;
    
    @Autowired
    private CommonService commonService;

    /**
     * @Title: index
     * @Description: TODO 首页
     * @param teacher
     * @param model
     * @return
     * @author com.mhout.sx
     */
    @RequiresPermissions("account:teacher:index")
    @RequestMapping(value = { "index" })
    public String index(Teacher teacher, Model model) {
        return "modules/account/teacherIndex";
    }

    /**
     * @Title: list
     * @Description: TODO 列表页
     * @param teacher
     * @param request
     * @param response
     * @param model
     * @return
     * @author com.mhout.sx
     */
    @RequiresPermissions("account:teacher:index")
    @RequestMapping(value = { "list", "" })
    public String list(Teacher teacher, HttpServletRequest request, HttpServletResponse response, Model model) {
        teacher.setRoleType(Teacher.ROLE_TEACHER);
        Page<Teacher> page = teacherService.findTeacher(new Page<Teacher>(request, response), teacher);
        model.addAttribute("page", page);
        model.addAttribute("roleTypeName", Teacher.ROLE_TEACHER_NAME);
        return "modules/account/teacherList";
    }

    /**
     * @Title: get 模型加载
     * @Description: TODO
     * @param id
     * @return
     * @author com.mhout.sx
     */
    @ModelAttribute
    public Teacher get(@RequestParam(required = false) String id) {
        if (StringUtils.isNotBlank(id)) {
            return teacherService.getTeacher(id);
        } else {
            return new Teacher();
        }
    }

    /**
     * @Title: form
     * @Description: TODO 表单页面
     * @param teacher
     * @param model
     * @return
     * @author com.mhout.sx
     */
    @RequiresPermissions(value = { "account:teacher:view", "account:teacher:add",
            "account:teacher:edit" }, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(Teacher teacher, Model model) {
        if (teacher.getCompany() == null || teacher.getCompany().getId() == null) {
            teacher.setCompany(UserUtils.getUser().getCompany());
        }
        if (StringUtils.isBlank(teacher.getRoleType())) {
            teacher.setRoleType(Teacher.ROLE_TEACHER);
        }
        teacher.setRoleTypeName(Teacher.ROLE_TEACHER_NAME);
        if (StringUtils.isBlank(teacher.getOrigin())) {
            teacher.setOrigin(CommonController.USER_DATA_ORIGIN_LOCAL);
        }
        if (StringUtils.isBlank(teacher.getOutside())) {
            teacher.setOutside("0");
        }
        teacher.setOriginName(DictUtils.getDictLabel(teacher.getOrigin(), CommonController.USER_DATA_ORIGIN, "本地"));
        commonService.setPswRule(model);
        model.addAttribute("teacher", teacher);
        return "modules/account/teacherForm";
    }

    /**
     * @Title: save
     * @Description: TODO 表单保存
     * @param teacher
     * @param request
     * @param model
     * @param redirectAttributes
     * @return
     * @author com.mhout.sx
     */
    @RequiresPermissions(value = { "account:teacher:add", "account:teacher:edit" }, logical = Logical.OR)
    @RequestMapping(value = "save")
    public String save(Teacher teacher, HttpServletRequest request, Model model,
            RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, teacher)) {
            return form(teacher, model);
        }
        if (!"true".equals(checkUser(teacher))) {
            addMessage(model, "保存教工'" + teacher.getLoginName() + "'失败，登录名已存在");
            return form(teacher, model);
        }
        teacherService.saveTeacher(teacher, request.getParameterMap());
        // 清除当前用户缓存
        User user = new User();
        BeanUtils.copyProperties(teacher, user);
        user.setOldLoginName(user.getLoginName());
        UserUtils.clearCache(user);
        addMessage(redirectAttributes, "保存教工'" + teacher.getLoginName() + "'成功");
        return "redirect:" + adminPath + "/account/teacher/list?repage";
    }

    /**
     * @Title: delete
     * @Description: TODO 逻辑删除
     * @param teacher
     * @param redirectAttributes
     * @return
     * @author com.mhout.sx
     */
    @RequiresPermissions("account:teacher:del")
    @RequestMapping(value = "delete")
    public String delete(Teacher teacher, RedirectAttributes redirectAttributes) {
        if (Global.isDemoMode()) {
            addMessage(redirectAttributes, "演示模式，不允许操作！");
            return "redirect:" + adminPath + "/account/student/list?repage";
        }
        if (UserUtils.getUser().getId().equals(teacher.getId())) {
            addMessage(redirectAttributes, "删除用户失败, 不允许删除当前用户");
        } else if (User.isAdmin(teacher.getId())) {
            addMessage(redirectAttributes, "删除用户失败, 不允许删除超级管理员用户");
        } else {
            teacherService.deleteTeacher(teacher);
            addMessage(redirectAttributes, "删除教工成功");
        }
        return "redirect:" + adminPath + "/account/teacher/list?repage";
    }

    /**
     * @Title: importFileTemplate
     * @Description: TODO 导入模板下载
     * @param response
     * @param redirectAttributes
     * @return
     * @author com.mhout.sx
     */
    @RequiresPermissions("account:teacher:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            String fileName = "教工数据导入模板.xlsx";
            List<Teacher> list = Lists.newArrayList();
            Teacher test = new Teacher();
            Office c = new Office();
            c.setName("XX学校");
            test.setCompany(c);
            test.setLoginName("abcd");
            test.setOfficeNames("前台,财务部");
            test.setPostNames(" 会计,美工");
            test.setName("test");
            test.setRemarks("test");
            test.setGender("1");
            test.setNation("1");
            test.setDuty("1");
            test.setEducation("1");
            test.setCreateDate(new Date());
            test.setNo("1234455");
            test.setIdNo("5137011988123333");
            list.add(test);
            new ExportExcel("教工数据", Teacher.class, 2).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + adminPath + "/account/teacher/list?repage";
    }

    /**
     * @Title: importFile
     * @Description: TODO excel导入
     * @param file
     * @param redirectAttributes
     * @return
     * @author com.mhout.sx
     */
    @RequiresPermissions("account:teacher:import")
    @RequestMapping(value = "import", method = RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
        if (Global.isDemoMode()) {
            addMessage(redirectAttributes, "演示模式，不允许操作！");
            return "redirect:" + adminPath + "/account/teacher/index";
        }
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<Teacher> list = ei.getDataList(Teacher.class);
            for (Teacher user : list) {
                try {
                	if ("false".equals(checkParamUser(user))) {
                		if (StringUtils.isNotBlank(user.getLoginName())) {
                			failureMsg.append("<br/>导入教工 " + user.getLoginName() + "信息不符合相关规则; ");
                            failureNum++;
                		}
                        continue;
                	}
                    if ("true".equals(checkUser(user))) {
                        user.setNewPassword("123456");
                        user.setAccount(user.getLoginName());
                        user.setRoleType(Teacher.ROLE_TEACHER);
                        user.setOrigin(CommonController.USER_DATA_ORIGIN_EXCEL);
                        BeanValidators.validateWithException(validator, user);
                        teacherService.saveTeacherFromExcel(user);
                        successNum++;
                    } else {
                        failureMsg.append("<br/>导入教工 " + user.getLoginName() + "的相关信息已存在; ");
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
                failureMsg.insert(0, "，失败 " + failureNum + " 条教工，导入信息如下：");
            }
            addMessage(redirectAttributes, "已成功导入 " + successNum + " 条教工" + failureMsg);
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入教工失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + adminPath + "/account/teacher/index";
    }

    /**
     * @Title: exportFile
     * @Description: TODO excel导出
     * @param teacher
     * @param request
     * @param response
     * @param redirectAttributes
     * @return
     * @author com.mhout.sx
     */
    @RequiresPermissions("account:teacher:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public String exportFile(Teacher teacher, HttpServletRequest request, HttpServletResponse response,
            RedirectAttributes redirectAttributes) {
        try {
            teacher.setRoleType(Teacher.ROLE_TEACHER);
            String fileName = "教工数据" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            Page<Teacher> page = teacherService.findTeacher(new Page<Teacher>(request, response, -1), teacher);
            new ExportExcel("教工数据", Teacher.class).setDataList(page.getList()).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            addMessage(redirectAttributes, "导出教工失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + adminPath + "/account/teacher/list";
    }

    private String checkUser(Teacher teacher) {
    	User user = new User();
    	BeanUtils.copyProperties(teacher, user);
    	return UserUtils.checkUser(user);
    }
    
    /**
     * @Title: checkParamUser 
     * @Description: 参数校验
     * @param student
     * @return
     * @author com.mhout.xyb
     */
    private String checkParamUser(Teacher teacher) {
    	User user = new User();
    	BeanUtils.copyProperties(teacher, user);
    	return UserUtils.checkParamUser(user);
    }
}
