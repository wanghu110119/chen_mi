
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
import com.mht.modules.account.dao.ParentsDao;
import com.mht.modules.account.entity.Parents;
import com.mht.modules.account.entity.Student;
import com.mht.modules.account.web.CommonController;
import com.mht.modules.fieldconfig.service.UserExtendInfoService;
import com.mht.modules.ident.dao.IdentityGroupDao;
import com.mht.modules.ident.entity.IdentityGroup;
import com.mht.modules.ident.entity.IdentityGroupRecord;
import com.mht.modules.sys.dao.RoleDao;
import com.mht.modules.sys.entity.Role;
import com.mht.modules.sys.service.UserStatService;
import com.mht.modules.sys.utils.DictUtils;

/**
 * @ClassName: ParentsService
 * @Description: 家长账号
 * @author com.mhout.sx
 * @date 2017年4月19日 下午3:23:37
 * @version 1.0.0
 */
@Service
@Transactional(readOnly = true)
public class ParentsService extends BaseService implements InitializingBean {

    @Autowired
    private ParentsDao parentsDao;

    @Autowired
    private UserStatService userStatService;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private IdentityGroupDao identityGroupDao;
    
    @Autowired
   	private UserExtendInfoService userExtendInfoService;

    /**
     * @Title: saveParents
     * @Description: TODO 保存
     * @param parents
     * @author com.mhout.sx
     */
    @Transactional(readOnly = false)
    public void saveParents(Parents parents, Map<String,String[]> parameters) {
        // TODO Auto-generated method stub
        if (StringUtils.isBlank(parents.getId())) {
            parents.preInsert();
            parents.setPassword(CommonService.encryption(parents.getNewPassword()));
            parentsDao.insert(parents);
            userStatService.save(parents, UserStatService.INSERT);
        } else {
            // Student oldUser = studentDao.get(student.getId());
            parents.preUpdate();
            parentsDao.update(parents);
            userStatService.save(parents, UserStatService.UPDATE);
        }
        // 保存角色
        saveRole(parents);
        // 保存用户组
        saveGroup(parents);
        // 保存学生
        saveStudent(parents);
        if(parameters != null){
        	 userExtendInfoService.saveUserExtendInfo(parameters, GroupRole.PARENTS, parents.getId());
        }       
    }

    /**
     * @Title: saveStudent
     * @Description: TODO 保存家长与学生的对应关系
     * @param parents
     * @author com.mhout.sx
     */
    private void saveStudent(Parents parents) {
        parentsDao.deleteStudentParents(parents.getId());
        if (!StringUtils.isBlank(parents.getStudentIds())) {
            String studentIds = parents.getStudentIds();
            String[] ids = studentIds.split(",");
            // 再添加
            for (String studentId : ids) {
                parentsDao.insertStudentParents(parents.getId(), studentId);
            }
        }
    }

    /**
     * @Title: saveGroup
     * @Description: TODO 保存家长于用户组的对应关系
     * @param parents
     * @author com.mhout.sx
     */
    private void saveGroup(Parents parents) {
        List<IdentityGroup> list = identityGroupDao.findIdentityGroupByUser(parents.getId());
        for (IdentityGroup identityGroup : list) {
            // 删除，并出组
            identityGroupDao.deleteIdentityGroupUser(identityGroup.getId(), parents.getId());
            IdentityGroupRecord gr = new IdentityGroupRecord(identityGroup, parents, IdentityGroupRecord.GROUP_OUT);
            gr.preInsert();
            identityGroupDao.insertIdentityGroupRecord(gr);
        }

        // 添加并入组
        if (!StringUtils.isBlank(parents.getGroupIds())) {
            String gourpIds = parents.getGroupIds();
            String[] ids = gourpIds.split(",");
            // 再添加
            for (String gourpId : ids) {
                identityGroupDao.insertIdentityGroupUser(IdGen.uuid(), gourpId, parents.getId());
                IdentityGroup group = new IdentityGroup();
                group.setId(gourpId);
                IdentityGroupRecord gr = new IdentityGroupRecord(group, parents, IdentityGroupRecord.GROUP_IN);
                gr.preInsert();
                identityGroupDao.insertIdentityGroupRecord(gr);
            }
        }
    }

    /**
     * @Title: saveRole
     * @Description: TODO 保存家长与角色关系
     * @param parents
     * @author com.mhout.sx
     */
    private void saveRole(Parents parents) {
        roleDao.deleteRoleUser(parents.getId());
        if (!StringUtils.isBlank(parents.getRoleIds())) {
            String roleIds = parents.getRoleIds();
            String[] ids = roleIds.split(",");
            // 再添加
            for (String roleId : ids) {
                roleDao.insertRoleUser(parents.getId(), roleId);
            }
        }
        // 保存默认角色
        Role role = roleDao.getByEnname2(parents.getRoleType());
        roleDao.insertRoleUser(parents.getId(), role.getId());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // TODO Auto-generated method stub

    }

    /**
     * @Title: findParents
     * @Description: TODO 分页查询
     * @param page
     * @param parents
     * @return
     * @author com.mhout.sx
     */
    public Page<Parents> findParents(Page<Parents> page, Parents parents) {
        // TODO Auto-generated method stub
        parents.setPage(page);
        List<Parents> list = parentsDao.findList(parents);
        for (Parents p : list) {
            p.setOriginName(DictUtils.getDictLabel(p.getOrigin(), CommonController.USER_DATA_ORIGIN, "本地"));

            // 获取角色信息
            List<Role> roles = roleDao.findRoleByUser(p.getId(), Parents.ROLE_PARENTS);
            List<String> roleIds = roles.stream().map(Role::getId).collect(Collectors.toList());
            List<String> roleNames = roles.stream().map(Role::getName).collect(Collectors.toList());
            p.setRoleIds(String.join(",", roleIds));
            p.setRoleNames(String.join(",", roleNames));

            // 获取学生信息
            List<Student> students = parentsDao.findStudentByParents(p.getId());
            List<String> studentIds = students.stream().map(Student::getId).collect(Collectors.toList());
            List<String> studentNames = students.stream().map(Student::getName).collect(Collectors.toList());
            List<String> studentIdNos = students.stream().map(Student::getIdNo).collect(Collectors.toList());
            p.setStudentIds(String.join(",", studentIds));
            p.setStudentNames(String.join(",", studentNames));
            p.setStudentIdNo(String.join(",", studentIdNos));
        }
        page.setList(list);
        return page;
    }

    /**
     * @Title: getParents
     * @Description: TODO 获取家长账号详情，用户修改
     * @param id
     * @return
     * @author com.mhout.sx
     */
    public Parents getParents(String id) {
        // TODO Auto-generated method stub
        Parents parents = parentsDao.get(id);
        // 获取组信息
        List<IdentityGroup> groups = identityGroupDao.findIdentityGroupByUser(parents.getId());
        List<String> groupIds = groups.stream().map(IdentityGroup::getId).collect(Collectors.toList());
        List<String> groupNames = groups.stream().map(IdentityGroup::getGroupName).collect(Collectors.toList());
        parents.setGroupIds(String.join(",", groupIds));
        parents.setGroupNames(String.join(",", groupNames));
        // 获取角色信息
        List<Role> roles = roleDao.findRoleByUser(parents.getId(), Parents.ROLE_PARENTS);
        List<String> roleIds = roles.stream().map(Role::getId).collect(Collectors.toList());
        List<String> roleNames = roles.stream().map(Role::getName).collect(Collectors.toList());
        parents.setRoleIds(String.join(",", roleIds));
        parents.setRoleNames(String.join(",", roleNames));
        // 获取学生信息
        List<Student> students = parentsDao.findStudentByParents(parents.getId());
        List<String> studentIds = students.stream().map(Student::getId).collect(Collectors.toList());
        List<String> studentNames = students.stream().map(Student::getName).collect(Collectors.toList());
        parents.setStudentIds(String.join(",", studentIds));
        parents.setStudentNames(String.join(",", studentNames));
        return parents;
    }

    /**
     * @Title: deleteParents
     * @Description: TODO 逻辑删除
     * @param parents
     * @author com.mhout.sx
     */
    @Transactional(readOnly = false)
    public void deleteParents(Parents parents) {
        // TODO Auto-generated method stub
        parentsDao.deleteByLogic(parents);
        // 同步到Activiti
        // deleteActivitiUser(user);
        // 清除用户缓存
        // UserUtils.clearCache(user);
        // // 清除权限缓存
        // systemRealm.clearAllCachedAuthorizationInfo();
        userStatService.save(parents, UserStatService.DELETE);
    }

    /**
     * @Title: saveTeacherFromExcel
     * @Description: TODO 保存家长信息，数据来源于excel
     * @param parents
     * @author com.mhout.sx
     */
    @Transactional(readOnly = false)
    public void saveParentsFromExcel(Parents parents) {
        // TODO Auto-generated method stub
        parents.preInsert();
        parents.setPassword(CommonService.encryption(parents.getNewPassword()));
        parentsDao.insert(parents);
        userStatService.save(parents, UserStatService.INSERT);
        // 保存学生
        if (!StringUtils.isBlank(parents.getStudentIdNo())) {
            String studentNames = parents.getStudentIdNo();
            String[] names = studentNames.split(",");
            for (String name : names) {
                Student student = parentsDao.findStudentByName(name);
                if (student != null && StringUtils.isNotBlank(student.getId())) {
                    parentsDao.insertStudentParents(parents.getId(), student.getId());
                }
            }
        }
        // 保存角色
        saveRole(parents);
    }
}
