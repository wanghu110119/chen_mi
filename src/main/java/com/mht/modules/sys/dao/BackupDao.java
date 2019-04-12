package com.mht.modules.sys.dao;

import com.mht.common.persistence.CrudDao;
import com.mht.common.persistence.annotation.MyBatisDao;
import com.mht.modules.sys.entity.Backup;

/**
 * @ClassName: BackupDao
 * @Description: 应用数据库备份配置持久层
 * @author com.mhout.xyb
 * @date 2017年5月17日 下午5:00:13 
 * @version 1.0.0
 */
@MyBatisDao
public interface BackupDao extends CrudDao<Backup>{
	
	/**
	 * @Title: findBySysProject 
	 * @Description: 应用备份系统配置
	 * @param backup
	 * @return
	 * @author com.mhout.xyb
	 */
	public Backup findBySysProject(Backup backup);
}
