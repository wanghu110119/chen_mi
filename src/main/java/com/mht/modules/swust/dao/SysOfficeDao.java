/**
 * Copyright &copy; 2015-2020 <a href="http://www.mht.org/">mht</a> All rights reserved.
 */
package com.mht.modules.swust.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.mht.common.persistence.TreeDao;
import com.mht.common.persistence.annotation.MyBatisDao;
import com.mht.modules.account.entity.Student;
import com.mht.modules.sys.entity.Office;
import com.mht.modules.sys.entity.Post;
import com.mht.modules.sys.entity.User;

/**
 * 机构DAO接口
 * 
 * @author mht
 * @version 2014-05-16
 */
@MyBatisDao
public interface SysOfficeDao extends TreeDao<Office> {

    public Office getByCode(String code);

    /**
     * @Title: deletePost
     * @Description: TODO 删除部门与岗位的关联
     * @param id
     * @author com.mhout.sx
     */
    @Delete("delete from mht_oeg.sys_office_post where mht_oeg.office_id=#{1}")
    public void deletePost(String id);

    /**
     * @Title: insertPost
     * @Description: TODO 插入部门与岗位关联
     * @param id
     * @param postId
     * @author com.mhout.sx
     */
    @Insert("insert into mht_oeg.sys_office_post(office_id,post_id) VALUES(#{0},#{1})")
    public void insertPost(String id, String postId);

    /**
     * @Title: findPosts
     * @Description: TODO 查询部门与岗位的关联
     * @param id
     * @return
     * @author com.mhout.sx
     */
    @Select("select p.id,p.name from mht_oeg.sys_post p,mht_oeg.sys_office_post op where op.office_id=#{1} and p.del_flag='0' and p.id=op.post_id")
    public List<Post> findPosts(String id);

    /**
     * @Title: findListForStudent
     * @Description: TODO 获取教学类组织机构树
     * @return
     * @author com.mhout.sx
     */
    public List<Office> findListForStudent(Office office);

    /**
     * @Title: findByParentIdsLikeForStudent
     * @Description: TODO 查询教学类组织机构
     * @param office
     * @return
     * @author com.mhout.sx
     */
    public List<Office> findByParentIdsLikeForStudent(Office office);

    /**
     * @Title: deleteOfficeUser
     * @Description: TODO 删除用户与部门对应关系
     * @param userid
     * @author com.mhout.sx
     */
    @Delete("delete from mht_oeg.sys_user_office where user_id=#{0}")
    public void deleteOfficeUser(String userid);

    /**
     * @Title: insertOfficeUser
     * @Description: TODO 保存用户与部门对应关系
     * @param userid
     * @param officeId
     * @author com.mhout.sx
     */
    @Insert("insert into mht_oeg.sys_user_office(user_id,office_id) VALUES(#{0},#{1})")
    public void insertOfficeUser(String userid, String officeId);

    /**
     * @Title: findOfficeByUser
     * @Description: TODO 查询用户对应部门
     * @param userId
     * @return
     * @author com.mhout.sx
     */
    @Select("select id,name,parent_ids,grade from mht_oeg.sys_office o,mht_oeg.sys_user_office uo where uo.user_id=#{0} and uo.office_id=o.id and o.del_flag='0'")
    public List<Office> findOfficeByUser(String userId);

    /**
     * @Title: findListByName
     * @Description: TODO 通过部门名字获取部门信息，一般用于excel导入时
     * @param string
     * @return
     * @author com.mhout.sx
     */
    @Select("select id,name from mht_oeg.sys_office where name=#{0} AND useable='1'")
    public List<Office> findListByName(String string);

    /**
     * @Title: findOfficeByParent
     * @Description: TODO 获取下级部门
     * @param parentId
     * @return
     * @author com.mhout.sx
     */
    @Select("select id,name,parent_id \"parent.id\" from mht_oeg.sys_office where parent_id=#{0} and del_flag='0'")
    public List<Office> findOfficeByParent(String parentId);

    /**
     * @Title: findPostByOffice
     * @Description: TODO 获取部门下的岗位
     * @param parentId
     * @param userId
     * @return
     * @author com.mhout.sx
     */
    @Select("select p.id,p.name,up.user_id otherId from mht_oeg.sys_post p inner join mht_oeg.sys_office_post op on (op.office_id=#{0} and op.post_id=p.id) "
            + "left join mht_oeg.sys_user_post up on (up.user_id=#{1} and up.post_id=p.id) where  p.del_flag='0' ")
    public List<Post> findPostByOffice(String parentId, String userId);

    /**
     * @Title: findByName
     * @Description: TODO 通过部门名字获取部门信息，一般用于excel导入时
     * @param name
     * @return
     * @author com.mhout.sx
     */
    @Select("select id from mht_oeg.sys_office where name=#{0} and useable='1' and del_flag='0'")
    public Office findByName(String name);

    /**
     * @Title: findOfficeByParentAndType
     * @Description: TODO 通过父级和类型获取子级
     * @param parentId
     * @param type
     * @return
     * @author com.mhout.sx
     */
    @Select("select id,name,parent_id \"parent.id\" from mht_oeg.sys_office where parent_id=#{0} and (parent_id='0' or type=#{1}) and del_flag='0'")
    public List<Office> findOfficeByParentAndType(String parentId, String type);

    /**
     * @Title: findUserByOffice
     * @Description: TODO 获取部门下学生，用户家长选择对应学生时
     * @param parentId
     * @param userId
     * @param role
     * @return
     * @author com.mhout.sx
     */
    @Select("select u.id,u.name,u.no,ps.student_id otherId from mht_oeg.sys_user u inner join mht_oeg.sys_user_office uo on (uo.office_id=#{0} and uo.user_id=u.id and u.role_type=#{2}) "
            + "left join mht_oeg.sys_parents_student ps on (ps.parents_id=#{1} and ps.student_id=u.id) where  u.del_flag='0' ")
    public List<Student> findUserByOffice(String parentId, String userId, String role);

    /**
     * @Title: findUserByOffice
     * @Description: TODO 获取部门下用户
     * @param officeid
     * @param role
     * @return
     * @author com.mhout.sx
     */
    @Select("select u.id,u.name from mht_oeg.sys_user u inner join mht_oeg.sys_user_office uo on (uo.office_id=#{0} and uo.user_id=u.id and u.role_type=#{1}) where u.del_flag='0'")
    public List<User> findUserByOfficeOnly(String officeid, String role);
    /**
     * 
     * @Title: selectAll 
     * @Description: TODO 获取所有部门
     * @return
     * @author com.mhout.wzw
     */
	public List<Office> selectAll();
	
	@Select("select max(cast(code as decimal)) from mht_oeg.sys_office")
	public String findOfficeMaxCode();
	
	public void updateState(@Param("id")String id, @Param("useable")String useable);
	
	@Select("select id,login_name from mht_oeg.sys_user where office_id = #{0}")
	public List<User> findUserByOfficeCache(String id);

	public void batchDelete(@Param("ids") String[] ids);

}