/**
 * Copyright &copy; 2015-2020 <a href="http://www.mht.org/">mht</a> All rights reserved.
 */
package com.mht.modules.sys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mht.common.service.BaseService;
import com.mht.common.service.TreeService;
import com.mht.common.utils.StringUtils;
import com.mht.modules.sys.dao.OfficeDao;
import com.mht.modules.sys.dao.PostDao;
import com.mht.modules.sys.entity.Office;
import com.mht.modules.sys.entity.Post;
import com.mht.modules.sys.entity.User;
import com.mht.modules.sys.utils.UserUtils;

/**
 * 机构Service
 * 
 * @author mht
 * @version 2014-05-16
 */
@Service
@Transactional(readOnly = true)
public class OfficeService extends TreeService<OfficeDao, Office> {

    @Autowired
    private PostDao postDao;
    
    @Autowired
    private OfficeDao officeDao;

    public List<Office> findAll() {
        return UserUtils.getOfficeList();
    }

    public List<Office> findList(Boolean isAll, String name) {
    	Office office = new Office();
    	office.setName(name);
        if (isAll != null && isAll) {
        	//return UserUtils.getOfficeAllList();
            return officeDao.findAllList(office);
        } else {
        	//return UserUtils.getOfficeList();
        	User user = UserUtils.getUser();
            if (user.isAdmin()) {
                return officeDao.findAllList(office);
            } else {
                office.getSqlMap().put("dsf", BaseService.dataScopeFilter(user, "a", ""));
                return officeDao.findList(office);
            }
        }
    }

    @Transactional(readOnly = true)
    public List<Office> findList(Office office) {
        office.setParentIds(office.getParentIds() + "%");
        if ("2".equals(office.getType())) {
            return dao.findByParentIdsLikeForStudent(office);
        } else {
            return dao.findByParentIdsLike(office);
        }
    }

    @Transactional(readOnly = true)
    public Office getByCode(String code) {
        return dao.getByCode(code);
    }

    @Transactional(readOnly = false)
    public void save(Office office) {
        super.save(office);
        UserUtils.removeCache(UserUtils.CACHE_OFFICE_LIST);
    }

    @Transactional(readOnly = false)
    public void delete(Office office) {
        super.delete(office);
        UserUtils.removeCache(UserUtils.CACHE_OFFICE_LIST);
    }

    /**
     * @Title: findPostList
     * @Description: TODO 获取所有岗位和部门拥有的岗位，用于部门新增和修改
     * @param id
     * @return
     * @author com.mhout.sx
     */
    public List<Post> findPostList(String id) {
        // TODO Auto-generated method stub
        if (StringUtils.isBlank(id)) {
            // 新增
            return postDao.getAllPostForOffice();
        } else {
            // 修改
            return postDao.getAllPostForOffice2(id);
        }
    }

    /**
     * @Title: savePost
     * @Description: TODO 保存部门与岗位对应关系
     * @param office
     * @author com.mhout.sx
     */
    @Transactional(readOnly = false)
    public void savePost(Office office) {
        // TODO Auto-generated method stub
        // 先删除
        dao.deletePost(office.getId());
        String postIds = office.getPostIds();
        String[] ids = postIds.split(",");
        // 再添加
        for (String postId : ids) {
            dao.insertPost(office.getId(), postId);
        }
    }

    /**
     * @Title: findPosts
     * @Description: TODO 获取部门拥有的岗位
     * @param id
     * @return
     * @author com.mhout.sx
     */
    public List<Post> findPosts(String id) {
        // TODO Auto-generated method stub
        return dao.findPosts(id);
    }

    public List<Office> findListForStudent(Office office) {
        // TODO Auto-generated method stub
        // DictUtils.getDict("2", "sys_office_type");
        return dao.findListForStudent(office);
    }
    
    public List<Office> findAllOffices(Office office) {
    	return officeDao.findAllList(office);
    }

}
