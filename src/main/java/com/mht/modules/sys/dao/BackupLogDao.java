package com.mht.modules.sys.dao;

import java.util.List;

import com.mht.common.persistence.CrudDao;
import com.mht.common.persistence.annotation.MyBatisDao;
import com.mht.modules.sys.entity.BackupLog;

/**
 * @ClassName: BackupLogDao
 * @Description: 备份日志记录
 * @author com.mhout.xyb
 * @date 2017年5月17日 下午5:01:44 
 * @version 1.0.0
 */
@MyBatisDao
public interface BackupLogDao extends CrudDao<BackupLog> {
	
	public List<BackupLog> findBySysProject(BackupLog log);
	
}
