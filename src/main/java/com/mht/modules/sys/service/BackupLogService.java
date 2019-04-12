package com.mht.modules.sys.service;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mht.common.service.CrudService;
import com.mht.modules.ident.dao.SysProjectDao;
import com.mht.modules.ident.entity.SysProject;
import com.mht.modules.sys.dao.BackupLogDao;
import com.mht.modules.sys.entity.BackupLog;

/**
 * @ClassName: BackupLogService
 * @Description: 数据库备份日志
 * @author com.mhout.xyb
 * @date 2017年5月18日 下午1:55:20 
 * @version 1.0.0
 */
@Service
@Transactional(readOnly = true)
public class BackupLogService  extends CrudService<BackupLogDao, BackupLog>{
	
	@Autowired
	private BackupLogDao backupLogDao;
	
	@Autowired
	private SysProjectDao sysProjectDao;
	
	
	public List<BackupLog> findBySysProject(String sid) {
		if (StringUtils.isNotBlank(sid)) {
			BackupLog log = new BackupLog();
			SysProject sys = sysProjectDao.get(sid);
			log.setSysProject(sys);
			return backupLogDao.findBySysProject(log);
		}
		return null;
	}

}
