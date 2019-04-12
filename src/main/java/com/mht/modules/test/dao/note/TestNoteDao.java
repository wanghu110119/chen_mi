/**
 * Copyright &copy; 2015-2020 <a href="http://www.mht.org/">mht</a> All rights reserved.
 */
package com.mht.modules.test.dao.note;

import com.mht.common.persistence.CrudDao;
import com.mht.common.persistence.annotation.MyBatisDao;
import com.mht.modules.test.entity.note.TestNote;

/**
 * 富文本测试DAO接口
 * @author liugf
 * @version 2016-10-04
 */
@MyBatisDao
public interface TestNoteDao extends CrudDao<TestNote> {

	
}