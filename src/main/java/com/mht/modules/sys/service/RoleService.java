
package com.mht.modules.sys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mht.common.persistence.Page;
import com.mht.common.service.TreeService;
import com.mht.common.utils.StringUtils;
import com.mht.modules.account.entity.Parents;
import com.mht.modules.account.entity.Student;
import com.mht.modules.account.entity.Teacher;
import com.mht.modules.sys.dao.RoleDao;
import com.mht.modules.sys.entity.Role;
import com.mht.modules.sys.entity.User;
import com.mht.modules.sys.utils.UserUtils;

@Service
@Transactional(readOnly = true)
public class RoleService extends TreeService<RoleDao, Role> {

    @Autowired
    private RoleDao roleDao;

    public List<Role> findList(boolean b) {
        // TODO Auto-generated method stub
        // List<Post> list = postDao.findAllList(new Post());
        List<Role> list = UserUtils.getRoleList();
        return list;
    }

    public Page<Role> findList(Page<Role> page, Role role) {
        // TODO Auto-generated method stub
        if (!StringUtils.isBlank(role.getParentIds())) {
            role.setParentIds(role.getParentIds() + role.getParentId() + "%");
        }
        // List<Post> list = postDao.findByParentIdsLike(post);
        // return list;
        User user = UserUtils.getUser();
        role.getSqlMap().put("dsf", dataScopeFilter(user.getCurrentUser(), "o", "u"));
        role.setPage(page);
        if (!user.isAdmin()) {
        	page.setList(roleDao.findList(role));
        } else {
        	page.setList(roleDao.findAllList(role));
        }
        return page;
    }

    // public List<ZTreeDto> getTree(String id) {
    // // TODO Auto-generated method stub
    // String parentId;
    // if (id == null || "".equals(id)) {
    // parentId = "0";
    // } else {
    // parentId = id;
    // }
    // // 获取下一级部门
    // List<Post> list = this.postDao.findPostByParent(parentId);
    // List<ZTreeDto> tree = new ArrayList<ZTreeDto>();
    // if (list == null || list.size() == 0) {
    // } else {
    // for (Post post : list) {
    // ZTreeDto treeObj = this.getTreeObject(post);
    // tree.add(treeObj);
    // }
    // }
    // return tree;
    // }
    //
    // private ZTreeDto getTreeObject(Post post) {
    // ZTreeDto dto = new ZTreeDto();
    // dto.setObj(post.getParentIds());
    // dto.setName(post.getName());
    // dto.setId(post.getId());
    // // 判断当前还有没有子级
    // List<Post> list = this.postDao.findPostByParent(post.getId());
    // if (list == null || list.isEmpty()) {
    // dto.setIsParent(false);
    // }
    // return dto;
    // }
    //
    // @Transactional(readOnly = false)
    // public void deleteByLogic(Post post) {
    // // TODO Auto-generated method stub
    // this.postDao.deleteByLogic(post);
    // }
    //
    // public List<Post> findByOffice(String officeId) {
    // return this.postDao.findPostByOffice(officeId);
    // }
    
    
    public boolean isUpdate(String enname) {
    	boolean isupdate = Student.ROLE_STUDENT.equals(enname)
    			|| Teacher.ROLE_TEACHER.equals(enname)
    			|| Parents.ROLE_PARENTS.equals(enname)
    			|| "root".equals(enname);
    	return isupdate;
    }
}
