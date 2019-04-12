/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.mht.modules.gen.dao;

import com.mht.modules.gen.entity.GenTable;
import com.mht.modules.gen.entity.GenTableColumn;
import com.mht.common.persistence.CrudDao;
import com.mht.common.persistence.annotation.MyBatisDao;

/**
 * 业务表字段DAO接口
 * @author 张继平
 * @version 2017-3-22
 */
@MyBatisDao
public interface GenTableColumnDao extends CrudDao<GenTableColumn> {
	
	public void deleteByGenTable(GenTable genTable);
}
