package com.mht.modules.account.web;

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
import com.mht.modules.account.service.StudentService;
import com.mht.modules.sys.entity.User;
import com.mht.modules.sys.utils.DictUtils;
import com.mht.modules.sys.utils.UserUtils;

/**
 * @ClassName: StudentController
 * @Description: 学生账号
 * @author com.mhout.sx
 * @date 2017年4月19日 下午3:31:41
 * @version 1.0.0
 */
@Controller
@RequestMapping(value = "${adminPath}/account/student")
public class StudentController extends BaseController {

    @Autowired
    private StudentService studentService;
    
    @Autowired
    private CommonService commonService;

    /**
     * @Title: index
     * @Description: TODO 首页
     * @param student
     * @param model
     * @return
     * @author com.mhout.sx
     */
    @RequiresPermissions("account:student:index")
    @RequestMapping(value = { "index" })
    public String index(Student student, Model model) {
        return "modules/account/studentIndex";
    }

    /**
     * @Title: list
     * @Description: TODO 列表页
     * @param student
     * @param request
     * @param response
     * @param model
     * @return
     * @author com.mhout.sx
     */
    @RequiresPermissions("account:student:index")
    @RequestMapping(value = { "list", "" })
    public String list(Student student, HttpServletRequest request, HttpServletResponse response, Model model) {
        student.setRoleType(Student.ROLE_STUDENT);
        Page<Student> page = studentService.findStudent(new Page<Student>(request, response), student);
        model.addAttribute("page", page);
        // model.addAttribute("student", student);
        model.addAttribute("roleTypeName", Student.ROLE_STUDENT_NAME);
        return "modules/account/studentList";
    }

    /**
     * @Title: get
     * @Description: TODO 模型加载
     * @param id
     * @return
     * @author com.mhout.sx
     */
    @ModelAttribute
    public Student get(@RequestParam(required = false) String id) {
        if (StringUtils.isNotBlank(id)) {
            return studentService.getStudent(id);
        } else {
            return new Student();
        }
    }

    /**
     * @Title: form
     * @Description: TODO 表单页面
     * @param student
     * @param model
     * @return
     * @author com.mhout.sx
     */
    @RequiresPermissions(value = { "account:student:view", "account:student:add",
            "account:student:edit" }, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(Student student, Model model) {
        if (student.getCompany() == null || student.getCompany().getId() == null) {
            student.setCompany(UserUtils.getUser().getCompany());
        }
        if (StringUtils.isBlank(student.getRoleType())) {
            student.setRoleType(Student.ROLE_STUDENT);
        }
        student.setRoleTypeName(Student.ROLE_STUDENT_NAME);
        if (StringUtils.isBlank(student.getOrigin())) {
            student.setOrigin(CommonController.USER_DATA_ORIGIN_LOCAL);
        }
        student.setOriginName(DictUtils.getDictLabel(student.getOrigin(), CommonController.USER_DATA_ORIGIN, "本地"));
        commonService.setPswRule(model);
        model.addAttribute("student", student);
        return "modules/account/studentForm";
    }

    /**
     * @Title: save
     * @Description: TODO 保存
     * @param student
     * @param request
     * @param model
     * @param redirectAttributes
     * @return
     * @author com.mhout.sx
     */
    @RequiresPermissions(value = { "account:student:add", "account:student:edit" }, logical = Logical.OR)
    @RequestMapping(value = "save")
    public String save(Student student, HttpServletRequest request, Model model,
            RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, student)) {
            return form(student, model);
        }
        if (!"true".equals(checkUser(student))) {
            addMessage(model, "保存学生'" + student.getLoginName() + "'失败，账号已存在");
            return form(student, model);
        }
        studentService.saveStudent(student, request.getParameterMap());
        // 清除当前用户缓存
        User user = new User();
        BeanUtils.copyProperties(student, user);
        user.setOldLoginName(user.getLoginName());
        UserUtils.clearCache(user);
        addMessage(redirectAttributes, "保存学生'" + student.getLoginName() + "'成功");
        return "redirect:" + adminPath + "/account/student/list?repage";
    }

    /**
     * @Title: delete
     * @Description: TODO 逻辑删除
     * @param student
     * @param redirectAttributes
     * @return
     * @author com.mhout.sx
     */
    @RequiresPermissions("account:student:del")
    @RequestMapping(value = "delete")
    public String delete(Student student, RedirectAttributes redirectAttributes) {
        if (Global.isDemoMode()) {
            addMessage(redirectAttributes, "演示模式，不允许操作！");
            return "redirect:" + adminPath + "/account/student/list?repage";
        }
        if (UserUtils.getUser().getId().equals(student.getId())) {
            addMessage(redirectAttributes, "删除用户失败, 不允许删除当前用户");
        } else if (User.isAdmin(student.getId())) {
            addMessage(redirectAttributes, "删除用户失败, 不允许删除超级管理员用户");
        } else {
            studentService.deleteStudent(student);
            addMessage(redirectAttributes, "删除学生成功");
        }
        return "redirect:" + adminPath + "/account/student/list?repage";
    }

    /**
     * @Title: importFileTemplate
     * @Description: TODO 导入模板下载
     * @param response
     * @param redirectAttributes
     * @return
     * @author com.mhout.sx
     */
    @RequiresPermissions("account:student:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            String fileName = "学生数据导入模板.xlsx";
            List<Student> list = Lists.newArrayList();
            list.add(studentService.getStudent(UserUtils.getUser().getId()));
            new ExportExcel("学生数据", Student.class, 2).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + adminPath + "/account/student/list?repage";
    }

    /**
     * @Title: importFile
     * @Description:  excel导入
     * @param file
     * @param redirectAttributes
     * @return
     * @author com.mhout.sx
     */
    @RequiresPermissions("account:student:import")
    @RequestMapping(value = "import", method = RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
        if (Global.isDemoMode()) {
            addMessage(redirectAttributes, "演示模式，不允许操作！");
            return "redirect:" + adminPath + "/account/student/index";
        }
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<Student> list = ei.getDataList(Student.class);
            for (Student user : list) {
                try {
                	if ("false".equals(checkParamUser(user))) {
                		if (StringUtils.isNotBlank(user.getLoginName())) {
                			failureMsg.append("<br/>导入学生 " + user.getLoginName() + "信息不符合相关规则; ");
                            failureNum++;
                		}
                        continue;
                	}
                    if ("true".equals(checkUser(user))) {
                        user.setNewPassword("123456");
                        user.setAccount(user.getLoginName());
                        user.setRoleType(Student.ROLE_STUDENT);
                        user.setOrigin(CommonController.USER_DATA_ORIGIN_EXCEL);
                        BeanValidators.validateWithException(validator, user);
                        studentService.saveStudent(user, null);
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
                failureMsg.insert(0, "，失败 " + failureNum + " 条学生，导入信息如下：");
            }
            addMessage(redirectAttributes, "已成功导入 " + successNum + " 条学生" + failureMsg);
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入学生失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + adminPath + "/account/student/index";
    }

    /**
     * @Title: exportFile
     * @Description: TODO 导出excel
     * @param student
     * @param request
     * @param response
     * @param redirectAttributes
     * @return
     * @author com.mhout.sx
     */
    @RequiresPermissions("account:student:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public String exportFile(Student student, HttpServletRequest request, HttpServletResponse response,
            RedirectAttributes redirectAttributes) {
        try {
            student.setRoleType(Student.ROLE_STUDENT);
            String fileName = "学生数据" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            Page<Student> page = studentService.findStudent(new Page<Student>(request, response, -1), student);
            new ExportExcel("学生数据", Student.class).setDataList(page.getList()).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            addMessage(redirectAttributes, "导出学生失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + adminPath + "/account/student/list";
    }
    
    
    private String checkUser(Student student) {
    	User user = new User();
    	BeanUtils.copyProperties(student, user);
    	return UserUtils.checkUser(user);
    }
    
    /**
     * @Title: checkParamUser 
     * @Description: 参数校验
     * @param student
     * @return
     * @author com.mhout.xyb
     */
    private String checkParamUser(Student student) {
    	User user = new User();
    	BeanUtils.copyProperties(student, user);
    	return UserUtils.checkParamUser(user);
    }
}
