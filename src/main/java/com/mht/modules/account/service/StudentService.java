
package com.mht.modules.account.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mht.common.persistence.Page;
import com.mht.common.service.BaseService;
import com.mht.common.utils.IdGen;
import com.mht.common.utils.StringUtils;
import com.mht.modules.account.constant.GroupRole;
import com.mht.modules.account.dao.StudentDao;
import com.mht.modules.account.entity.Student;
import com.mht.modules.account.web.CommonController;
import com.mht.modules.fieldconfig.service.UserExtendInfoService;
import com.mht.modules.ident.dao.IdentityGroupDao;
import com.mht.modules.ident.entity.IdentityGroup;
import com.mht.modules.ident.entity.IdentityGroupRecord;
import com.mht.modules.sys.dao.OfficeDao;
import com.mht.modules.sys.dao.RoleDao;
import com.mht.modules.sys.entity.Office;
import com.mht.modules.sys.entity.Role;
import com.mht.modules.sys.service.UserStatService;
import com.mht.modules.sys.utils.DictUtils;

/**
 * @ClassName: StudentService
 * @Description: 学生账号
 * @author com.mhout.sx
 * @date 2017年4月19日 下午3:35:54
 * @version 1.0.0
 */
@Service
@Transactional(readOnly = true)
public class StudentService extends BaseService implements InitializingBean {

    @Autowired
    private StudentDao studentDao;

    @Autowired
    private UserStatService userStatService;

    @Autowired
    private OfficeDao officeDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private IdentityGroupDao identityGroupDao;
    
    @Autowired
   	private UserExtendInfoService userExtendInfoService;

    /**
     * @Title: saveStudent
     * @Description: TODO 保存
     * @param student
     * @author com.mhout.sx
     */
    @Transactional(readOnly = false)
    public void saveStudent(Student student, Map<String,String[]> parameters) {
        // TODO Auto-generated method stub
        if (StringUtils.isBlank(student.getId())) {
            student.preInsert();
            student.setPassword(CommonService.encryption(student.getNewPassword()));
            studentDao.insert(student);
            userStatService.save(student, UserStatService.INSERT);
        } else {
            // Student oldUser = studentDao.get(student.getId());
            student.preUpdate();
            studentDao.update(student);
            userStatService.save(student, UserStatService.UPDATE);
        }
        // 保存部门
        saveOffice(student);
        // 保存角色
        saveRole(student);
        // 保存用户组
        saveGroup(student);
        if(parameters != null){
        	userExtendInfoService.saveUserExtendInfo(parameters, GroupRole.STUDENT, student.getId());
        }      
    }

    /**
     * @Title: saveGroup
     * @Description: TODO 保存于用户组对应关系
     * @param student
     * @author com.mhout.sx
     */
    private void saveGroup(Student student) {
        List<IdentityGroup> list = identityGroupDao.findIdentityGroupByUser(student.getId());
        for (IdentityGroup identityGroup : list) {
            // 删除，并出组
            identityGroupDao.deleteIdentityGroupUser(identityGroup.getId(), student.getId());
            IdentityGroupRecord gr = new IdentityGroupRecord(identityGroup, student, IdentityGroupRecord.GROUP_OUT);
            gr.preInsert();
            identityGroupDao.insertIdentityGroupRecord(gr);
        }

        // 添加并入组
        if (!StringUtils.isBlank(student.getGroupIds())) {
            String gourpIds = student.getGroupIds();
            String[] ids = gourpIds.split(",");
            // 再添加
            for (String gourpId : ids) {
                identityGroupDao.insertIdentityGroupUser(IdGen.uuid(), gourpId, student.getId());
                IdentityGroup group = new IdentityGroup();
                group.setId(gourpId);
                IdentityGroupRecord gr = new IdentityGroupRecord(group, student, IdentityGroupRecord.GROUP_IN);
                gr.preInsert();
                identityGroupDao.insertIdentityGroupRecord(gr);
            }
        }
    }

    /**
     * @Title: saveOffice
     * @Description: TODO 保存部门
     * @param student
     * @author com.mhout.sx
     */
    private void saveOffice(Student student) {
        officeDao.deleteOfficeUser(student.getId());
        officeDao.insertOfficeUser(student.getId(), student.getOffice().getId());
    }

    /**
     * @Title: saveRole
     * @Description: TODO 保存与角色对应关系
     * @param student
     * @author com.mhout.sx
     */
    private void saveRole(Student student) {
        roleDao.deleteRoleUser(student.getId());
        if (!StringUtils.isBlank(student.getRoleIds())) {
            String roleIds = student.getRoleIds();
            String[] ids = roleIds.split(",");
            // 再添加
            for (String roleId : ids) {
                roleDao.insertRoleUser(student.getId(), roleId);
            }
        }
        // 保存默认角色
        Role role = roleDao.getByEnname2(student.getRoleType());
        roleDao.insertRoleUser(student.getId(), role.getId());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // TODO Auto-generated method stub

    }

    /**
     * @Title: findStudent
     * @Description: TODO 分页查询
     * @param page
     * @param student
     * @return
     * @author com.mhout.sx
     */
    public Page<Student> findStudent(Page<Student> page, Student student) {
        // TODO Auto-generated method stub
        // student.getSqlMap().put("dsf",
        // dataScopeFilter(student.getCurrentUser(), "o", "a"));
        // if (!StringUtils.isBlank(student.getOfficeId())) {
        // student.setOfficeParentIds(student.getOfficeParentIds() +
        // student.getOfficeId() + "%");
        // }
        student.setPage(page);
        List<Student> students = studentDao.findList(student);
        for (Student s : students) {
            s.setOriginName(DictUtils.getDictLabel(s.getOrigin(), CommonController.USER_DATA_ORIGIN, "本地"));
            List<Office> offices = officeDao.findOfficeByUser(s.getId());
            if (offices != null && !offices.isEmpty()) {
                s.setOffice(offices.get(0));
            }
            // 获取角色信息
            List<Role> roles = roleDao.findRoleByUser(s.getId(), Student.ROLE_STUDENT);
            List<String> roleIds = roles.stream().map(Role::getId).collect(Collectors.toList());
            List<String> roleNames = roles.stream().map(Role::getName).collect(Collectors.toList());
            s.setRoleIds(String.join(",", roleIds));
            s.setRoleNames(String.join(",", roleNames));
        }
        page.setList(students);
        return page;
    }

    /**
     * @Title: getStudent
     * @Description: TODO 获取详情
     * @param id
     * @return
     * @author com.mhout.sx
     */
    public Student getStudent(String id) {
        // TODO Auto-generated method stub
        Student student = studentDao.get(id);
        // 获取部门信息
        List<Office> offices = officeDao.findOfficeByUser(student.getId());
        if (offices != null && !offices.isEmpty()) {
            student.setOffice(offices.get(0));
        }
        // 获取组信息
        List<IdentityGroup> groups = identityGroupDao.findIdentityGroupByUser(student.getId());
        List<String> groupIds = groups.stream().map(IdentityGroup::getId).collect(Collectors.toList());
        List<String> groupNames = groups.stream().map(IdentityGroup::getGroupName).collect(Collectors.toList());
        student.setGroupIds(String.join(",", groupIds));
        student.setGroupNames(String.join(",", groupNames));
        // 获取角色信息
        List<Role> roles = roleDao.findRoleByUser(student.getId(), Student.ROLE_STUDENT);
        List<String> roleIds = roles.stream().map(Role::getId).collect(Collectors.toList());
        List<String> roleNames = roles.stream().map(Role::getName).collect(Collectors.toList());
        student.setRoleIds(String.join(",", roleIds));
        student.setRoleNames(String.join(",", roleNames));
        return student;
    }

    /**
     * @Title: deleteStudent
     * @Description: TODO 逻辑删除
     * @param student
     * @author com.mhout.sx
     */
    @Transactional(readOnly = false)
    public void deleteStudent(Student student) {
        // TODO Auto-generated method stub
        studentDao.deleteByLogic(student);
        // 同步到Activiti
        // deleteActivitiUser(user);
        // 清除用户缓存
        // UserUtils.clearCache(user);
        // // 清除权限缓存
        // systemRealm.clearAllCachedAuthorizationInfo();
        userStatService.save(student, UserStatService.DELETE);
    }
}
