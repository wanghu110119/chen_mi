
package com.mht.modules.account.dao;

import com.mht.common.persistence.CrudDao;
import com.mht.common.persistence.annotation.MyBatisDao;
import com.mht.modules.account.entity.Teacher;

/**
 * @ClassName: TeacherDao
 * @Description: 教工账号dao
 * @author com.mhout.sx
 * @date 2017年4月19日 下午3:59:40
 * @version 1.0.0
 */
@MyBatisDao
public interface TeacherDao extends CrudDao<Teacher> {

}
