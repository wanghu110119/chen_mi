/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.mht.modules.unifiedauth.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.mht.common.persistence.CrudDao;
import com.mht.common.persistence.annotation.MyBatisDao;
import com.mht.modules.sys.entity.Post;
import com.mht.modules.unifiedauth.entity.AuthPost;

/**
 * 
 * @ClassName: AuthPostDao
 * @Description: 岗位授权
 * @author com.mhout.zjh
 * @date 2017年4月6日 上午9:05:23 
 * @version 1.0.0
 */
@MyBatisDao
public interface AuthPostDao extends CrudDao<AuthPost> {
	/**
	 * 
	 * @Title: findListById 
	 * @Description: TODO
	 * @param roleId
	 * @return
	 * @author com.mhout.zjh
	 */
	List<AuthPost> findListByPostId(@Param("postId") String postId, @Param("type") String type,
			@Param("status") String status, @Param("accessway") String accessway,
			@Param("parentIds") List<String> parentIds);
	/**
	 * 
	 * @Title: deleteByPostId
	 * @Description: 根据PostID删除授权关系
	 * @return
	 * @author com.mhout.zjh
	 */
	void deleteByPostId(String postId);
	/**
	 * 
	 * @Title: findPostsByName 
	 * @Description: 通过角色名称查询
	 * @param roleName
	 * @return
	 * @author com.mhout.zjh
	 */
	List<Post> findPostsByName(String postName);
	/**
	 * 
	 * @Title: findPostsByParent 
	 * @Description: 通过父级ID查询岗位
	 * @param parentId
	 * @return
	 * @author com.mhout.zjh
	 */
	List<Post> findPostsByParent(String parentId);
}
