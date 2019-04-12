/**
 * Copyright &copy; 2015-2020 <a href="http://www.mht.org/">mht</a> All rights reserved.
 */
package com.mht.modules.test.dao.validation;

import com.mht.common.persistence.CrudDao;
import com.mht.common.persistence.annotation.MyBatisDao;
import com.mht.modules.test.entity.validation.TestValidation;

/**
 * 测试校验功能DAO接口
 * @author lgf
 * @version 2016-10-05
 */
@MyBatisDao
public interface TestValidationDao extends CrudDao<TestValidation> {

	
}