
package com.mht.modules.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import com.mht.common.persistence.TreeDao;
import com.mht.common.persistence.annotation.MyBatisDao;
import com.mht.modules.sys.entity.Post;

/**
 * @ClassName: PostDao
 * @Description: 岗位dao
 * @author com.mhout.sx
 * @date 2017年4月5日 下午12:47:06
 * @version 1.0.0
 */
@MyBatisDao
public interface PostDao extends TreeDao<Post> {

    public Post getByCode(String code);

    /**
     * @Title: findPostByParent
     * @Description: TODO 通过父节点获取子节点
     * @param parentId
     * @return
     * @author com.mhout.sx
     */
    @Select("select id,name,parent_ids parentIds from sys_post where parent_id=#{1} and del_flag='0' order by sort asc")
    public List<Post> findPostByParent(String parentId);

    /**
     * 
     * @Title: findPostByOffice
     * @Description: 根据officeId查询post
     * @param officeId
     * @return
     * @author com.mhout.zjh
     */
    public List<Post> findPostByOffice(String officeId);

    /**
     * @Title: getAllPostForOffice
     * @Description: TODO 新增部门时选择岗位查询
     * @return
     * @author com.mhout.sx
     */
    @Select("select id,name,parent_ids parentIds,parent_id \"parent.id\" from sys_post where del_flag='0'")
    public List<Post> getAllPostForOffice();

    /**
     * @Title: getAllPostForOffice2
     * @Description: TODO 修改部门时选择岗位查询
     * @param officeId
     * @return
     * @author com.mhout.sx
     */
    @Select("select id,name,parent_ids parentIds,parent_id \"parent.id\",op.office_id otherId "
            + "from sys_post p left join sys_office_post op on p.id=op.post_id and op.office_id=#{1} where p.del_flag='0'")
    public List<Post> getAllPostForOffice2(String officeId);

    /**
     * @Title: getAllPostForUserOffice
     * @Description: TODO 新增、修改用户时，通过部门查询部门拥有的岗位和用户已拥有的岗位
     * @param officeId
     * @param userId
     * @return
     * @author com.mhout.sx
     */
    @Select("select a.id,a.name,up.user_id otherId from "
            + "(select p.* from sys_post p inner join sys_office_post op on (p.id=op.post_id and op.office_id=#{0}) where p.del_flag='0') a "
            + "left join sys_user_post up on (a.id=up.post_id and up.user_id=#{1})")
    public List<Post> getAllPostForUserOffice(String officeId, String userId);

    /**
     * @Title: deleteUserPost
     * @Description: TODO 删除用户与岗位对应
     * @param userId
     * @author com.mhout.sx
     */
    @Delete("delete from sys_user_post where user_id=#{0}")
    public void deleteUserPost(String userId);

    /**
     * @Title: insertUserPost
     * @Description: TODO 保存用户与岗位对应
     * @param userId
     * @param postId
     * @author com.mhout.sx
     */
    @Insert("insert into sys_user_post(user_id,post_id) values(#{0},#{1})")
    public void insertUserPost(String userId, String postId);

    /**
     * @Title: findPostByUser
     * @Description: TODO 查询用户拥有的岗位
     * @param userId
     * @return
     * @author com.mhout.sx
     */
    @Select("select p.id,p.name from sys_user_post up,sys_post p where up.user_id=#{0} and p.id=up.post_id")
    public List<Post> findPostByUser(String userId);

    /**
     * @Title: findByName
     * @Description: TODO 通过名称查询
     * @param name
     * @return
     * @author com.mhout.sx
     */
    @Select("select id from sys_post where name=#{0} and del_flag='0'")
    public Post findByName(String name);
}
