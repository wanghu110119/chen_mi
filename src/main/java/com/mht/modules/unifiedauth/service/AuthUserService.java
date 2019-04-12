
package com.mht.modules.unifiedauth.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.mht.common.config.Global;
import com.mht.common.json.ZTreeDto;
import com.mht.common.service.CrudService;
import com.mht.common.utils.StringUtils;
import com.mht.modules.account.entity.Student;
import com.mht.modules.account.entity.Teacher;
import com.mht.modules.ident.entity.Application;
import com.mht.modules.sys.dao.OfficeDao;
import com.mht.modules.sys.entity.Office;
import com.mht.modules.sys.entity.User;
import com.mht.modules.unifiedauth.dao.AuthOfficeDao;
import com.mht.modules.unifiedauth.dao.AuthUserDao;
import com.mht.modules.unifiedauth.entity.AuthOffice;
import com.mht.modules.unifiedauth.entity.AuthUser;

/**
 * @ClassName: AuthUserService
 * @Description: 用户应用授权
 * @author com.mhout.sx
 * @date 2017年3月30日 下午2:19:54
 * @version 1.0.0
 */
@Service
@Transactional(readOnly = true)
public class AuthUserService extends CrudService<AuthUserDao, AuthUser> {

    @Autowired
    private AuthUserDao authUserDaoDao;

    @Autowired
    private AuthOfficeDao authOfficeDao;

    @Autowired
    private OfficeDao officeDao;

    // @Autowired
    // private UserDao userDao;

    @Value("${adminPath}")
    private String adminPath;

    /**
     * @Title: getTree
     * @Description: TODO获取组织结构和用户树
     * @param id
     * @return
     * @author com.mhout.sx
     */
    public List<ZTreeDto> getTree(String id, String type) {
        // TODO Auto-generated method stub
        // 判断类型
        if (StringUtils.isNotBlank(type) && !"2".equals(type)) {
            type = "";
        }
        String parentId;
        if (id == null || "".equals(id)) {
            parentId = "0";
            type = "";
        } else {
            parentId = id;
        }
        // 获取下一级部门
        List<Office> list = this.authOfficeDao.findOfficeByParentAndType(parentId, type);
        List<ZTreeDto> tree = new ArrayList<ZTreeDto>();
        if (list == null || list.size() == 0) {
        } else {
            for (Office office : list) {
                ZTreeDto treeObj = this.getTreeObject(office);
                tree.add(treeObj);
            }
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        String path = request.getContextPath();
        // 获取部门下面人员
        // List<User> list2 = this.authUserDaoDao.findUserByOffice(parentId);
        List<User> list2;
        if ("".equals(type) || "1".equals(type)) {
            list2 = this.officeDao.findUserByOfficeOnly(parentId, Teacher.ROLE_TEACHER);
        } else {
            list2 = this.officeDao.findUserByOfficeOnly(parentId, Student.ROLE_STUDENT);
        }
        if (list2 == null || list2.size() == 0) {
        } else {
            for (User user : list2) {
                ZTreeDto treeObj = this.getTreeObject(user, path);
                tree.add(treeObj);
            }
        }
        return tree;
    }

    /**
     * @Title: getTreeObject
     * @Description: TODO User转换为ztree
     * @param user
     * @return
     * @author com.mhout.sx
     */
    private ZTreeDto getTreeObject(User user, String path) {
        ZTreeDto dto = new ZTreeDto();
        dto.setObj("user");
        dto.setName(user.getName());
        dto.setId(user.getId());
        dto.setIsParent(false);
        // dto.setIconSkin("fa fa-user");

        dto.setIcon(path + ZTreeDto.USER_ICON);
        return dto;
    }

    /**
     * @Title: getTreeObject
     * @Description: TODO Office转换为ztree
     * @param user
     * @return
     * @author com.mhout.sx
     */
    private ZTreeDto getTreeObject(Office office) {
        ZTreeDto dto = new ZTreeDto();
        dto.setObj("office");
        dto.setName(office.getName());
        dto.setId(office.getId());
        return dto;
    }

    /**
     * @Title: findListByUser
     * @Description: TODO 获取应用及用户对应权限
     * @param authUser
     * @return
     * @author com.mhout.sx
     */
    public List<AuthUser> findListByUser(AuthUser authUser) {
        // TODO Auto-generated method stub
        String type = Global.SYSTEM;
        String status = Global.ENABLE;
        String accessway = Global.CASACESSWAY;
        String userId = authUser.getUser().getId();
        // 获取父级访问权限
        List<Office> list = this.authUserDaoDao.findByUserIdOfficeIds(userId);
        List<String> parentIds = new ArrayList<String>();
        if (CollectionUtils.isNotEmpty(list)) {
            for (Office office : list) {
                String ids = office.getParentIds();
                parentIds.add(office.getId());
                if (StringUtils.isNotBlank(ids)) {
                    String[] values = ids.split("\\,");
                    for (String id : values) {
                        parentIds.add(id);
                    }
                }
            }
        } else {
            parentIds.add("0");
        }
        List<AuthUser> authlist = this.authUserDaoDao.findListByAuthUser(userId, type, status, accessway);
        List<AuthOffice> officelist = this.authOfficeDao.findListByOffice(null, type, status, accessway, parentIds);
        List<String> ids = new ArrayList<String>();
        if (CollectionUtils.isNotEmpty(list)) {
            for (AuthOffice authOffice2 : officelist) {
                ids.add(authOffice2.getApply() == null ? "" : authOffice2.getApply().getId());
            }
        }
        List<AuthUser> relist = new ArrayList<AuthUser>();
        if (CollectionUtils.isNotEmpty(authlist)) {
            for (AuthUser authUser2 : authlist) {
                String id = authUser2.getApply() == null ? "" : authUser2.getApply().getId();
                String close = authUser2.getCloseType();
                if (StringUtils.isNotBlank(close) && "1".equals(close)) {
                    authUser2.setAccessAuth("");
                }
                if (ids.contains(id)) {
                    authUser2.setIsEdit("2");
                }
                relist.add(authUser2);
            }
        }
        return relist;
        // List<String> parentIds = new ArrayList<String>();
        // if (StringUtils.isNotBlank(postId)) {
        // Post post = this.postDao.get(postId);
        // if (post != null) {
        // String ids = post.getParentIds();
        // if (StringUtils.isNotBlank(ids)) {
        // String[] list = ids.split("\\,");
        // for (String id : list) {
        // parentIds.add(id);
        // }
        // }
        // } else {
        // parentIds.add("0");
        // }
        // } else {
        // parentIds.add("0");
        // }
        // List<AuthPost> list =
        // this.authPostDao.findListByPostId(null,type,status,accessway,parentIds);
        // List<AuthPost> userlist =
        // this.authPostDao.findListByPostId(postId,type,status,accessway,null);
        // List<String> ids = new ArrayList<String>();
        // if (CollectionUtils.isNotEmpty(list)) {
        // for (AuthPost authPost2 : list) {
        // ids.add(authPost2.getApply() == null ? "" :
        // authPost2.getApply().getId());
        // }
        // }
        // List<AuthPost> relist = new ArrayList<AuthPost>();
        // if (CollectionUtils.isNotEmpty(userlist)) {
        // for (AuthPost authPost2 : userlist) {
        // String id = authPost2.getApply() == null ? "" :
        // authPost2.getApply().getId();
        // String close = authPost2.getCloseType();
        // if (StringUtils.isNotBlank(close) && "1".equals(close)) {
        // authPost2.setAccessAuth("");
        // }
        // if (ids.contains(id)) {
        // authPost2.setIsEdit("2");
        // }
        // relist.add(authPost2);
        // }
        // }
        // return relist;
        // return this.authUserDaoDao.findListByAuthUser(userId, type, status,
        // accessway);
    }

    /**
     * @Title: saveAuths
     * @Description: TODO 保存用户与应用权限的关系
     * @param list
     * @param userId
     * @author com.mhout.sx
     */
    @Transactional(readOnly = false)
    public void saveAuths(List<AuthUser> list, List<AuthUser> closelist, String userId) {
        // TODO Auto-generated method stub
        // 通过officeid删除
        this.authUserDaoDao.deleteByUser(userId);
        if (list != null) {
            for (AuthUser authUser : list) {
                authUser.preInsert();
                authUser.setCloseType("2");
                this.authUserDaoDao.insert(authUser);
            }
        }
        if (closelist != null) {
            for (AuthUser authUser : closelist) {
                authUser.preInsert();
                authUser.setCloseType("1");
                this.authUserDaoDao.insert(authUser);
            }
        }
    }

    /**
     * @Title: getApplicationByUser
     * @Description: TODO 获取用户对应应用
     * @param user
     * @return
     * @author com.mhout.sx
     */
    public List<Application> getApplicationByUser(User user) {
        // 通过userId获取Application
        return this.authUserDaoDao.getApplicationByUser(user.getId());
    }

    /**
     * @Title: findUserByName
     * @Description: TODO 通过用户姓名搜索用户
     * @param userName
     * @return
     * @author com.mhout.sx
     */
    public List<User> findUserByName(String userName) {
        // TODO Auto-generated method stub
        return this.authUserDaoDao.findUserByName("%" + userName + "%");
    }

    /**
     * 
     * @Title: findByAppId
     * @Description: TODO 通过AppID获取用户信息
     * @param id
     * @return
     * @author com.mhout.wj
     */
    public List<AuthUser> findByAppId(String id) {
        return this.authUserDaoDao.findByAppId(id);
    }
}
