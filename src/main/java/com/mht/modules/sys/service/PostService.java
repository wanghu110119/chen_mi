
package com.mht.modules.sys.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mht.common.json.ZTreeDto;
import com.mht.common.persistence.Page;
import com.mht.common.service.TreeService;
import com.mht.common.utils.StringUtils;
import com.mht.modules.sys.dao.PostDao;
import com.mht.modules.sys.entity.Post;

/**
 * @ClassName: PostService
 * @Description: 岗位serivice
 * @author com.mhout.sx
 * @date 2017年4月5日 下午12:48:56
 * @version 1.0.0
 */
@Service
@Transactional(readOnly = true)
public class PostService extends TreeService<PostDao, Post> {

    @Autowired
    private PostDao postDao;

    /**
     * @Title: findList
     * @Description: TODO 查询所有
     * @param b
     * @return
     * @author com.mhout.sx
     */
    public List<Post> findList(boolean b) {
        // TODO Auto-generated method stub
        List<Post> list = postDao.findAllList(new Post());
        return list;
    }

    /**
     * @Title: findList
     * @Description: TODO 查询同级及下级所有节点
     * @param b
     * @return
     * @author com.mhout.sx
     */
    public Page<Post> findList(Page<Post> page, Post post) {
        // TODO Auto-generated method stub
        if (!StringUtils.isBlank(post.getParentIds())) {
            post.setParentIds(post.getParentIds() + post.getParentId() + "%");
        }
        // List<Post> list = postDao.findByParentIdsLike(post);
        // return list;
//        post.getSqlMap().put("dsf", dataScopeFilter(post.getCurrentUser(), "o", "a"));
        post.setPage(page);
        page.setList(postDao.findList(post));
        return page;
    }

    /**
     * @Title: getTree
     * @Description: TODO ajax懒加载树，获取下级节点
     * @param id
     * @return
     * @author com.mhout.sx
     */
    public List<ZTreeDto> getTree(String id) {
        // TODO Auto-generated method stub
        String parentId;
        if (id == null || "".equals(id)) {
            parentId = "0";
        } else {
            parentId = id;
        }
        // 获取下一级部门
        List<Post> list = this.postDao.findPostByParent(parentId);
        List<ZTreeDto> tree = new ArrayList<ZTreeDto>();
        if (list == null || list.size() == 0) {
        } else {
            for (Post post : list) {
                ZTreeDto treeObj = this.getTreeObject(post);
                tree.add(treeObj);
            }
        }
        return tree;
    }

    /**
     * @Title: getTreeObject
     * @Description: TODO 转换为ztree对象
     * @param post
     * @return
     * @author com.mhout.sx
     */
    private ZTreeDto getTreeObject(Post post) {
        ZTreeDto dto = new ZTreeDto();
        dto.setObj(post.getParentIds());
        dto.setName(post.getName());
        dto.setId(post.getId());
        // 判断当前还有没有子级
        List<Post> list = this.postDao.findPostByParent(post.getId());
        if (list == null || list.isEmpty()) {
            dto.setIsParent(false);
        }
        return dto;
    }

    /**
     * @Title: deleteByLogic
     * @Description: TODO 逻辑删除岗位
     * @param post
     * @author com.mhout.sx
     */
    @Transactional(readOnly = false)
    public void deleteByLogic(Post post) {
        // TODO Auto-generated method stub
        this.postDao.deleteByLogic(post);
    }

    /**
     * 
     * @Title: findByOffice
     * @Description: TODO
     * @param offceId
     * @author com.mhout.zjh
     */
    public List<Post> findByOffice(String officeId) {
        return this.postDao.findPostByOffice(officeId);
    }
}
