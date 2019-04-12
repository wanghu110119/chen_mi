
package com.mht.modules.account.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.mht.common.config.Global;
import com.mht.common.json.ZTreeDto;
import com.mht.common.service.BaseService;
import com.mht.common.utils.StringUtils;
import com.mht.modules.account.dao.StudentDao;
import com.mht.modules.account.entity.Student;
import com.mht.modules.audit.dao.SafeStrategyDao;
import com.mht.modules.audit.entity.SafeStrategy;
import com.mht.modules.ident.dao.IdentityGroupDao;
import com.mht.modules.ident.entity.IdentityGroup;
import com.mht.modules.sys.dao.OfficeDao;
import com.mht.modules.sys.dao.RoleDao;
import com.mht.modules.sys.dao.UserDao;
import com.mht.modules.sys.entity.Office;
import com.mht.modules.sys.entity.Post;
import com.mht.modules.sys.entity.Role;
import com.mht.modules.sys.entity.User;
import com.mht.modules.sys.service.UserStatService;

/**
 * @ClassName: CommonService
 * @Description: 账号通用service
 * @author com.mhout.sx
 * @date 2017年4月18日 下午5:45:11
 * @version 1.0.0
 */
@Service
@Transactional(readOnly = true)
public class CommonService extends BaseService implements InitializingBean {

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private IdentityGroupDao identityGroupDao;

    @Autowired
    private StudentDao studentDao;

    @Autowired
    private UserStatService userStatService;

    @Autowired
    private OfficeDao officeDao;
    
    @Autowired
    private SafeStrategyDao safeStrategyDao;
    
    @Autowired
    private UserDao userDao;

    /**
     * @Title: findRoleList
     * @Description: TODO 获取所有角色，供ztree加载
     * @param id
     * @return
     * @author com.mhout.sx
     */
    public List<Role> findRoleList(String id) {
        // TODO Auto-generated method stub
        if (StringUtils.isBlank(id)) {
            // 新增
            return roleDao.getAllRoleForUser();
        } else {
            // 修改
            return roleDao.getAllRoleForUser2(id);
        }
    }

    /**
     * @Title: findGroupList
     * @Description: TODO 获取所有用户组，供账号选择
     * @param id
     * @return
     * @author com.mhout.sx
     */
    public List<IdentityGroup> findGroupList(String id) {
        if (StringUtils.isBlank(id)) {
            // 新增
            return identityGroupDao.getAllGroupForUser();
        } else {
            // 修改
            return identityGroupDao.getAllGroupForUser2(id);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // TODO Auto-generated method stub

    }

    /**
     * @Title: encryption
     * @Description: TODO md5 36位加密
     * @param plainText
     * @return
     * @author com.mhout.sx
     */
    public static String encryption(String plainText) {
        String re_md5 = new String();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();

            int i;

            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }

            re_md5 = buf.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return re_md5;
    }

    /**
     * @Title: savePassword
     * @Description: TODO 保存、修改密码
     * @param id
     * @param newPassword
     * @author com.mhout.sx
     */
    @Transactional(readOnly = false)
    public void savePassword(String id, String newPassword) {
        // TODO Auto-generated method stub
        String password = encryption(newPassword);
        studentDao.updatePasswordById(id, password);
        User user = new User(id);
        userStatService.save(user, UserStatService.UPDATE_PWD);
    }

    /**
     * @Title: updatePhoto
     * @Description: TODO 修改头像
     * @param id
     * @param photoNameType
     * @param url
     * @author com.mhout.sx
     */
    @Transactional(readOnly = false)
    public boolean updatePhoto(String id, String photoNameType, String url) {
        boolean value = false;
    	User user = new User();
        if ("0".equals(photoNameType)) {// 学号
        	user.setNo(id);
        	if(userDao.findUserByNoOrIdNo(user) != null){
        		studentDao.updatePhotoByNo(id, url);
        		value = true;
        	}
        } else {// 证件号
        	user.setIdNo(id);
        	if(userDao.findUserByNoOrIdNo(user) != null){
        		studentDao.updatePhotoByIdNo(id, url);
        		value = true;
        	}
        }
        return value;
    }

    /**
     * @Title: officePostTreeData
     * @Description: TODO 岗位组织机构树、懒加载
     * @param id
     * @param userId
     * @return
     * @author com.mhout.sx
     */
    public List<ZTreeDto> officePostTreeData(String id, String userId) {
        // TODO Auto-generated method stub
        String parentId;
        // String type = "";
        List<ZTreeDto> tree = new ArrayList<ZTreeDto>();
        if (StringUtils.isBlank(id)) {
            parentId = "0";
        } else {
            parentId = id;
        }
        // 获取下一级部门,部门不需要判断是否选中
        List<Office> list = this.officeDao.findOfficeByParent(parentId);
        if (list == null || list.size() == 0) {
        } else {
            for (Office office : list) {
                ZTreeDto treeObj = this.getTreeObject(office, userId);
                tree.add(treeObj);
            }
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        String path = request.getContextPath();
        // 获取部门岗位
        List<Post> list2 = this.officeDao.findPostByOffice(parentId, userId);
        if (list2 == null || list2.size() == 0) {
        } else {
            for (Post post : list2) {
                ZTreeDto treeObj = this.getTreeObject(post, path);
                tree.add(treeObj);
            }
        }
        return tree;
    }

    /**
     * @Title: officeStudentTreeData
     * @Description: TODO 获取部门与学生树结构
     * @param id
     * @param userId
     * @return
     * @author com.mhout.sx
     */
    public List<ZTreeDto> officeStudentTreeData(String id, String userId) {
        String parentId;
        String type = "2";
        List<ZTreeDto> tree = new ArrayList<ZTreeDto>();
        if (StringUtils.isBlank(id)) {
            parentId = "0";
        } else {
            parentId = id;
        }
        // 获取下一级部门,部门不需要判断是否选中，学生树
        List<Office> list = this.officeDao.findOfficeByParentAndType(parentId, type);
        if (list == null || list.size() == 0) {
        } else {
            for (Office office : list) {
                ZTreeDto treeObj = this.getTreeObject2(office, userId);
                tree.add(treeObj);
            }
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        String path = request.getContextPath();
        // 获取部门学生
        List<Student> list2 = this.officeDao.findUserByOffice(parentId, userId, Student.ROLE_STUDENT);
        if (list2 == null || list2.size() == 0) {
        } else {
            for (Student student : list2) {
                ZTreeDto treeObj = this.getTreeObject(student, path);
                tree.add(treeObj);
            }
        }
        return tree;
    }

    /**
     * @Title: getTreeObject
     * @Description: TODO 转换Student为ztree，可选
     * @param student
     * @param path
     * @return
     * @author com.mhout.sx
     */
    private ZTreeDto getTreeObject(Student student, String path) {
        ZTreeDto dto = new ZTreeDto();
        dto.setObj("student");
        dto.setName(student.getName());
        dto.setFullName(student.getName() + "学号：" + student.getNo());
        dto.setId(student.getId());
        dto.setIsParent(false);
        dto.setChkDisabled(false);
        dto.setNocheck(false);
        dto.setIcon(path + ZTreeDto.USER_ICON);
        if (StringUtils.isNotBlank(student.getOtherId())) {
            dto.setChecked(true);
        }
        return dto;
    }

    /**
     * @Title: getTreeObject2
     * @Description: TODO Office转ztree ，不可选
     * @param office
     * @param userId
     * @return
     * @author com.mhout.sx
     */
    private ZTreeDto getTreeObject2(Office office, String userId) {
        ZTreeDto dto = new ZTreeDto();
        dto.setObj("office");
        dto.setName(office.getName());
        dto.setId(office.getId());
        dto.setNocheck(false);
        dto.setFullName(office.getName());
        if ("0".equals(office.getParentId())) {
            dto.setChildren(officeStudentTreeData(office.getId(), userId));
        }
        return dto;
    }

    /**
     * @Title: getTreeObject
     * @Description: TODO 转换Office为ztree
     * @param office
     * @param userId
     * @return
     * @author com.mhout.sx
     */
    private ZTreeDto getTreeObject(Office office, String userId) {
        ZTreeDto dto = new ZTreeDto();
        dto.setObj("office");
        dto.setName(office.getName());
        dto.setId(office.getId());
        dto.setNocheck(false);
        if ("0".equals(office.getParentId())) {
            dto.setChildren(officePostTreeData(office.getId(), userId));
        }
        return dto;
    }

    /**
     * @Title: getTreeObject
     * @Description: TODO 转换Post 为ztree
     * @param post
     * @param path
     * @return
     * @author com.mhout.sx
     */
    private ZTreeDto getTreeObject(Post post, String path) {
        ZTreeDto dto = new ZTreeDto();
        dto.setObj("post");
        dto.setName(post.getName());
        dto.setId(post.getId());
        dto.setIsParent(false);
        dto.setChkDisabled(false);
        dto.setNocheck(false);
        dto.setIcon(path + ZTreeDto.USER_ICON);
        if (StringUtils.isNotBlank(post.getOtherId())) {
            dto.setChecked(true);
        }
        return dto;
    }
    
    /**
     * @Title: setPswRule 
     * @Description: 密码规则
     * @param model
     * @author com.mhout.xyb
     */
    public void setPswRule(Model model) {
    	SafeStrategy complex = safeStrategyDao.findByCode(Global.PWSCOMPLEX);
    	SafeStrategy pwsmin = safeStrategyDao.findByCode(Global.PWSMIN);
    	if (complex != null && Global.COMPLEX.equals(complex.getValue())) {
    		model.addAttribute("complex", false);
    	} else {
    		model.addAttribute("complex", true);
    	}
    	model.addAttribute("pwsmin", pwsmin == null ? "6":pwsmin.getValue());
    }

}
