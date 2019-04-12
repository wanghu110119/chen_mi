package com.mht.modules.swust.dao;

import com.mht.common.persistence.CrudDao;
import com.mht.common.persistence.annotation.MyBatisDao;
import com.mht.modules.sys.entity.User;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


/**
 * 
 * @ClassName: UserDao
 * @Description: 
 * @author com.mhout.wzw
 * @date 2017年7月26日 下午2:58:47 
 * @version 1.0.0
 */
@MyBatisDao
public interface SysUserDao extends CrudDao<User>{


    int deleteByPrimaryKey(String id);

    int insert(User record);

    int insertSelective(User record);


    User selectByPrimaryKey(String id);



    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

	User SelectByAcount(String account);

	void updatePasswordById(User user);

	@Select("select id from mht_oeg.sys_user where login_name = #{0} and del_flag = '0'")
	public User findUserByLoginName(String loginName);

	User getByLoginName(User user);

	User getTypeUser(String loginName);

	void updateMobile(String mobile);
	
}