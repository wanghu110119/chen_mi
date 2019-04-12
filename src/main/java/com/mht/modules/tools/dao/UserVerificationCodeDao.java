package com.mht.modules.tools.dao;

import com.mht.common.persistence.CrudDao;
import com.mht.common.persistence.annotation.MyBatisDao;
import com.mht.modules.sys.entity.User;
import com.mht.modules.tools.entity.UserVerificationCode;

/**
 * 用户验证码Dao
 * 
 * @author lanny
 *
 */
@MyBatisDao
public interface UserVerificationCodeDao extends CrudDao<UserVerificationCode> {

	public UserVerificationCode getByUserId(User user);
}
