/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mht.modules.gen.dao;

import com.mht.modules.gen.entity.GenTemplate;
import com.mht.common.persistence.CrudDao;
import com.mht.common.persistence.annotation.MyBatisDao;

/**
 * 代码模板DAO接口
 * @author ThinkGem
 * @version 2013-10-15
 */
@MyBatisDao
public interface GenTemplateDao extends CrudDao<GenTemplate> {
	
}
