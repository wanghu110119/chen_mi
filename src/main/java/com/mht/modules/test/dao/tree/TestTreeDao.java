/**
 * Copyright &copy; 2015-2020 <a href="http://www.mht.org/">mht</a> All rights reserved.
 */
package com.mht.modules.test.dao.tree;

import com.mht.common.persistence.TreeDao;
import com.mht.common.persistence.annotation.MyBatisDao;
import com.mht.modules.test.entity.tree.TestTree;

/**
 * 组织机构DAO接口
 * @author liugf
 * @version 2016-10-04
 */
@MyBatisDao
public interface TestTreeDao extends TreeDao<TestTree> {
	
}