package com.mht.modules.ident.dao;

import com.mht.common.persistence.CrudDao;
import com.mht.common.persistence.annotation.MyBatisDao;
import com.mht.modules.ident.entity.Apply;

/**
 * @ClassName: ApplyDao
 * @Description: 应用管理Dao接口
 * @author com.mhout.xyb
 * @date 2017年3月23日 上午11:21:23 
 * @version 1.0.0
 */
@MyBatisDao
public interface ApplyDao extends CrudDao<Apply>{

}
