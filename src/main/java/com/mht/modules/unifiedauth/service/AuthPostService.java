
package com.mht.modules.unifiedauth.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mht.common.config.Global;
import com.mht.common.json.ZTreeDto;
import com.mht.common.service.CrudService;
import com.mht.common.utils.StringUtils;
import com.mht.modules.sys.dao.PostDao;
import com.mht.modules.sys.entity.Post;
import com.mht.modules.unifiedauth.dao.AuthPostDao;
import com.mht.modules.unifiedauth.dao.AuthUserDao;
import com.mht.modules.unifiedauth.entity.AuthPost;
import com.mht.modules.unifiedauth.entity.AuthUser;

/**
 * 
 * @ClassName: AuthPostService
 * @Description: 岗位授权服务
 * @author com.mhout.zjh
 * @date 2017年4月6日 上午9:02:18
 * @version 1.0.0
 */
@Service
@Transactional(readOnly = true)
public class AuthPostService extends CrudService<AuthUserDao, AuthUser> {

    @Autowired
    private PostDao postDao;

    @Autowired
    private AuthPostDao authPostDao;

    /**
     * 
     * @Title: getTree
     * @Description: 获取岗位树
     * @param id
     * @return
     * @author com.mhout.zjh
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
        List<Post> list = this.authPostDao.findPostsByParent(parentId);
        List<ZTreeDto> tree = new ArrayList<ZTreeDto>();
        if (list != null && list.size() != 0) {
            for (Post post : list) {
                ZTreeDto treeObj = this.getTreeObject(post);
                tree.add(treeObj);
            }
        }
        return tree;
    }

    /**
     * 
     * @Title: getTreeObject
     * @Description: TODO
     * @param post
     * @return
     * @author com.mhout.zjh
     */
    private ZTreeDto getTreeObject(Post post) {
        ZTreeDto dto = new ZTreeDto();
        dto.setObj("post");
        dto.setName(post.getName());
        dto.setId(post.getId());
        if ("0".equals(post.getParentId())) {
            dto.setChildren(getTree(post.getId()));
        } else {
            // 判断是否为叶子节点
            List<Post> list = this.authPostDao.findPostsByParent(post.getId());
            if (list == null || list.size() == 0) {
                dto.setIsParent(false);
            }
        }
        return dto;
    }

    /**
     * 
     * @Title: saveAuths
     * @Description: 保存岗位与应用权限的关系
     * @param list
     * @param userId
     * @author com.mhout.zjh
     */
    @Transactional(readOnly = false)
    public void saveAuths(List<AuthPost> list, List<AuthPost> closelist, String postId) {
        // 通过postId删除
        this.authPostDao.deleteByPostId(postId);
        if (list != null) {
            for (AuthPost authPost : list) {
                authPost.preInsert();
                authPost.setCloseType("2");
                this.authPostDao.insert(authPost);
            }
        }
        if (closelist != null) {
            for (AuthPost authPost : closelist) {
                authPost.preInsert();
                authPost.setCloseType("1");
                this.authPostDao.insert(authPost);
            }
        }
    }

    /**
     * 
     * @Title: findPostsByName
     * @Description: 根据岗位名称查询岗位
     * @param postName
     * @return
     * @author com.mhout.zjh
     */
    public List<Post> findPostsByName(String postName) {
        return this.authPostDao.findPostsByName(postName);
    }

    /**
     * 
     * @Title: findByPost
     * @Description: TODO
     * @param postId
     * @return
     * @author com.mhout.zjh
     */
    public List<AuthPost> findByPost(String postId) {
        String type = Global.SYSTEM;
        String status = Global.ENABLE;
        String accessway = Global.CASACESSWAY;
        List<AuthPost> userlist = this.authPostDao.findListByPostId(postId, type, status, accessway, null);
        return userlist;
    };
}
