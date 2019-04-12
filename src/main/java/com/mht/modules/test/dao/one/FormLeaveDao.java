/**
 * Copyright &copy; 2015-2020 <a href="http://www.mht.org/">mht</a> All rights reserved.
 */
package com.mht.modules.test.dao.one;

import com.mht.common.persistence.CrudDao;
import com.mht.common.persistence.annotation.MyBatisDao;
import com.mht.modules.test.entity.one.FormLeave;

/**
 * 请假表单DAO接口
 * @author lgf
 * @version 2016-10-06
 */
@MyBatisDao
public interface FormLeaveDao extends CrudDao<FormLeave> {

	
}