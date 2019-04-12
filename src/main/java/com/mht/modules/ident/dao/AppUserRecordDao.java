package com.mht.modules.ident.dao;

import com.mht.common.persistence.CrudDao;
import com.mht.common.persistence.annotation.MyBatisDao;
import com.mht.modules.ident.entity.AppUserRecord;

/**
 * @ClassName: AppUserRecordDao
 * @Description: 应用访问历史
 * @author com.mhout.xyb
 * @date 2017年4月25日 下午1:40:30 
 * @version 1.0.0
 */
@MyBatisDao
public interface AppUserRecordDao extends CrudDao<AppUserRecord>{

}
