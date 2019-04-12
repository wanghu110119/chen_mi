package com.mht.modules.ident.dao;

import org.apache.ibatis.annotations.Update;

import com.mht.common.persistence.CrudDao;
import com.mht.common.persistence.annotation.MyBatisDao;
import com.mht.modules.ident.entity.ApplicationUser;

/**
 * @ClassName: ApplicationManagerDao
 * @Description: 应用管理员
 * @author com.mhout.xyb
 * @date 2017年3月31日 下午3:31:53 
 * @version 1.0.0
 */
@MyBatisDao
public interface ApplicationUserDao extends CrudDao<ApplicationUser> {
	
	@Update("delete from ident_user_application where application_id = #{0}")
	public void deleteByApp(String id);

}
