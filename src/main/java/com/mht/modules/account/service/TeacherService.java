
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
import com.mht.modules.account.dao.TeacherDao;
import com.mht.modules.account.entity.Teacher;
import com.mht.modules.account.web.CommonController;
import com.mht.modules.fieldconfig.service.UserExtendInfoService;
import com.mht.modules.ident.dao.IdentityGroupDao;
import com.mht.modules.ident.entity.IdentityGroup;
import com.mht.modules.ident.entity.IdentityGroupRecord;
import com.mht.modules.sys.dao.OfficeDao;
import com.mht.modules.sys.dao.PostDao;
import com.mht.modules.sys.dao.RoleDao;
import com.mht.modules.sys.entity.Office;
import com.mht.modules.sys.entity.Post;
import com.mht.modules.sys.entity.Role;
import com.mht.modules.sys.service.UserStatService;
import com.mht.modules.sys.utils.DictUtils;

/**
 * @ClassName: TeacherService
 * @Description: 教工账号
 * @author com.mhout.sx
 * @date 2017年4月19日 下午4:06:33
 * @version 1.0.0
 */
@Service
@Transactional(readOnly = true)
public class TeacherService extends BaseService implements InitializingBean {

    @Autowired
    private TeacherDao teacherDao;

    @Autowired
    private UserStatService userStatService;

    @Autowired
    private OfficeDao officeDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private IdentityGroupDao identityGroupDao;

    @Autowired
    private PostDao postDao;
    
    @Autowired
	private UserExtendInfoService userExtendInfoService;

    /**
     * @Title: saveTeacher
     * @Description: TODO 表单保存
     * @param teacher
     * @param parameters post提交的参数，用于解析扩展字段数据
     * @author com.mhout.sx
     */
    @Transactional(readOnly = false)
    public void saveTeacher(Teacher teacher, Map<String,String[]> parameters) {
        // TODO Auto-generated method stub
        if (StringUtils.isBlank(teacher.getId())) {
            teacher.preInsert();
            teacher.setPassword(CommonService.encryption(teacher.getNewPassword()));
            teacherDao.insert(teacher);
            userStatService.save(teacher, UserStatService.INSERT);
        } else {
            // Student oldUser = studentDao.get(student.getId());
            teacher.preUpdate();
            teacherDao.update(teacher);
            userStatService.save(teacher, UserStatService.UPDATE);
        }
        // 保存部门
        saveOffice(teacher);
        // 保存角色
        saveRole(teacher);
        // 保存用户组
        saveGroup(teacher);
        // 保存岗位
        savePost(teacher);
        //保存教师的扩展信息
        if(parameters != null){
        	 userExtendInfoService.saveUserExtendInfo(parameters,GroupRole.TEACHER,teacher.getId());
        }       
    }

    /**
     * @Title: savePost
     * @Description: TODO 保存于岗位对应关系
     * @param teacher
     * @author com.mhout.sx
     */
    private void savePost(Teacher teacher) {
        postDao.deleteUserPost(teacher.getId());
        if (!StringUtils.isBlank(teacher.getPostIds())) {
            String postIds = teacher.getPostIds();
            String[] ids = postIds.split(",");
            // 再添加
            for (String postId : ids) {
                postDao.insertUserPost(teacher.getId(), postId);
            }
        }
    }

    /**
     * @Title: saveGroup
     * @Description: TODO 保存于用户组关系
     * @param teacher
     * @author com.mhout.sx
     */
    private void saveGroup(Teacher teacher) {
        List<IdentityGroup> list = identityGroupDao.findIdentityGroupByUser(teacher.getId());
        for (IdentityGroup identityGroup : list) {
            // 删除，并出组
            identityGroupDao.deleteIdentityGroupUser(identityGroup.getId(), teacher.getId());
            IdentityGroupRecord gr = new IdentityGroupRecord(identityGroup, teacher, IdentityGroupRecord.GROUP_OUT);
            gr.preInsert();
            identityGroupDao.insertIdentityGroupRecord(gr);
        }

        // 添加并入组
        if (!StringUtils.isBlank(teacher.getGroupIds())) {
            String gourpIds = teacher.getGroupIds();
            String[] ids = gourpIds.split(",");
            // 再添加
            for (String gourpId : ids) {
                identityGroupDao.insertIdentityGroupUser(IdGen.uuid(), gourpId, teacher.getId());
                IdentityGroup group = new IdentityGroup();
                group.setId(gourpId);
                IdentityGroupRecord gr = new IdentityGroupRecord(group, teacher, IdentityGroupRecord.GROUP_IN);
                gr.preInsert();
                identityGroupDao.insertIdentityGroupRecord(gr);
            }
        }
    }

    /**
     * @Title: saveOffice
     * @Description: TODO 保存于部门关系
     * @param teacher
     * @author com.mhout.sx
     */
    private void saveOffice(Teacher teacher) {
        officeDao.deleteOfficeUser(teacher.getId());
        if (!StringUtils.isBlank(teacher.getOfficeIds())) {
            String officeIds = teacher.getOfficeIds();
            String[] ids = officeIds.split(",");
            // 再添加
            for (String officeId : ids) {
                officeDao.insertOfficeUser(teacher.getId(), officeId);
            }
        }
    }

    /**
     * @Title: saveRole
     * @Description: TODO 保存于角色对应关系
     * @param teacher
     * @author com.mhout.sx
     */
    private void saveRole(Teacher teacher) {
        roleDao.deleteRoleUser(teacher.getId());
        if (!StringUtils.isBlank(teacher.getRoleIds())) {
            String roleIds = teacher.getRoleIds();
            String[] ids = roleIds.split(",");
            // 再添加
            for (String roleId : ids) {
                roleDao.insertRoleUser(teacher.getId(), roleId);
            }
        }
        // 保存默认角色
        Role role = roleDao.getByEnname2(teacher.getRoleType());
        roleDao.insertRoleUser(teacher.getId(), role.getId());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // TODO Auto-generated method stub

    }

    /**
     * @Title: findTeacher
     * @Description: TODO 分页查询
     * @param page
     * @param teacher
     * @return
     * @author com.mhout.sx
     */
    public Page<Teacher> findTeacher(Page<Teacher> page, Teacher teacher) {
        teacher.setPage(page);
        List<Teacher> teachers = teacherDao.findList(teacher);
        for (Teacher t : teachers) {
            t.setOriginName(DictUtils.getDictLabel(t.getOrigin(), CommonController.USER_DATA_ORIGIN, "本地"));
            List<Office> offices = officeDao.findOfficeByUser(t.getId());
            List<String> officeIds = offices.stream().map(Office::getId).collect(Collectors.toList());
            List<String> officeNames = offices.stream().map(Office::getName).collect(Collectors.toList());
            t.setOfficeIds(String.join(",", officeIds));
            t.setOfficeNames(String.join(",", officeNames));
            // 获取角色信息
            List<Role> roles = roleDao.findRoleByUser(t.getId(), Teacher.ROLE_TEACHER);
            List<String> roleIds = roles.stream().map(Role::getId).collect(Collectors.toList());
            List<String> roleNames = roles.stream().map(Role::getName).collect(Collectors.toList());
            t.setRoleIds(String.join(",", roleIds));
            t.setRoleNames(String.join(",", roleNames));

            // 获取岗位信息
            List<Post> posts = postDao.findPostByUser(t.getId());
            List<String> postIds = posts.stream().map(Post::getId).collect(Collectors.toList());
            List<String> postNames = posts.stream().map(Post::getName).collect(Collectors.toList());
            t.setPostIds(String.join(",", postIds));
            t.setPostNames(String.join(",", postNames));
        }
        page.setList(teachers);
        return page;
    }

    /**
     * @Title: getTeacher
     * @Description: TODO 获取详情
     * @param id
     * @return
     * @author com.mhout.sx
     */
    public Teacher getTeacher(String id) {
        // TODO Auto-generated method stub
        Teacher teacher = teacherDao.get(id);
        // 获取部门信息
        List<Office> offices = officeDao.findOfficeByUser(teacher.getId());
        List<String> officeIds = offices.stream().map(Office::getId).collect(Collectors.toList());
        List<String> officeNames = offices.stream().map(Office::getName).collect(Collectors.toList());
        teacher.setOfficeIds(String.join(",", officeIds));
        teacher.setOfficeNames(String.join(",", officeNames));
        // 获取岗位信息
        List<Post> posts = postDao.findPostByUser(teacher.getId());
        List<String> postIds = posts.stream().map(Post::getId).collect(Collectors.toList());
        List<String> postNames = posts.stream().map(Post::getName).collect(Collectors.toList());
        teacher.setPostIds(String.join(",", postIds));
        teacher.setPostNames(String.join(",", postNames));
        // 获取组信息
        List<IdentityGroup> groups = identityGroupDao.findIdentityGroupByUser(teacher.getId());
        List<String> groupIds = groups.stream().map(IdentityGroup::getId).collect(Collectors.toList());
        List<String> groupNames = groups.stream().map(IdentityGroup::getGroupName).collect(Collectors.toList());
        teacher.setGroupIds(String.join(",", groupIds));
        teacher.setGroupNames(String.join(",", groupNames));
        // 获取角色信息
        List<Role> roles = roleDao.findRoleByUser(teacher.getId(), Teacher.ROLE_TEACHER);
        List<String> roleIds = roles.stream().map(Role::getId).collect(Collectors.toList());
        List<String> roleNames = roles.stream().map(Role::getName).collect(Collectors.toList());
        teacher.setRoleIds(String.join(",", roleIds));
        teacher.setRoleNames(String.join(",", roleNames));
        return teacher;
    }

    /**
     * @Title: deleteTeacher
     * @Description: TODO 逻辑删除
     * @param teacher
     * @author com.mhout.sx
     */
    @Transactional(readOnly = false)
    public void deleteTeacher(Teacher teacher) {
        // TODO Auto-generated method stub
        teacherDao.deleteByLogic(teacher);
        // 同步到Activiti
        // deleteActivitiUser(user);
        // 清除用户缓存
        // UserUtils.clearCache(user);
        // // 清除权限缓存
        // systemRealm.clearAllCachedAuthorizationInfo();
        userStatService.save(teacher, UserStatService.DELETE);
    }

    /**
     * @Title: saveTeacherFromExcel
     * @Description: TODO 保存来自excel的数据
     * @param teacher
     * @author com.mhout.sx
     */
    @Transactional(readOnly = false)
    public void saveTeacherFromExcel(Teacher teacher) {
        // TODO Auto-generated method stub
        teacher.preInsert();
        teacher.setPassword(CommonService.encryption(teacher.getNewPassword()));
        teacherDao.insert(teacher);
        userStatService.save(teacher, UserStatService.INSERT);
        // 保存角色
        saveRole(teacher);
        // 保存组织机构
        if (!StringUtils.isBlank(teacher.getOfficeNames())) {
            String officeNames = teacher.getOfficeNames();
            String[] names = officeNames.split(",");
            for (String name : names) {
                Office office = officeDao.findByName(name);
                if (office != null && StringUtils.isNotBlank(office.getId())) {
                    officeDao.insertOfficeUser(teacher.getId(), office.getId());
                }
            }
        }
        // 保存岗位
        if (!StringUtils.isBlank(teacher.getPostNames())) {
            String postNames = teacher.getPostNames();
            String[] names = postNames.split(",");
            for (String name : names) {
                Post post = postDao.findByName(name);
                if (post != null && StringUtils.isNotBlank(post.getId())) {
                    postDao.insertUserPost(teacher.getId(), post.getId());
                }
            }
        }
    }
}
