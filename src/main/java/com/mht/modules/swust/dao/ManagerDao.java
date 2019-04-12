/**
 * 
 */
package com.mht.modules.swust.dao;

import java.util.List;

import com.mht.common.persistence.CrudDao;
import com.mht.common.persistence.annotation.MyBatisDao;
import com.mht.modules.swust.entity.Manager;

    /**
     * @ClassName: ManagerDao
     * @Description: 
     * @author com.mhout.dhn
     * @date 2017年7月26日 下午4:15:29 
     * @version 1.0.0
     */
@MyBatisDao
public interface ManagerDao extends CrudDao<Manager> {

}
