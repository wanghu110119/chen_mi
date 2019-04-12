package com.mht.modules.sys.service;

import java.io.File;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mht.common.config.Global;
import com.mht.common.service.CrudService;
import com.mht.common.utils.BackupUtils;
import com.mht.common.utils.DateUtils;
import com.mht.modules.ident.dao.SysProjectDao;
import com.mht.modules.ident.entity.SysProject;
import com.mht.modules.sys.dao.BackupDao;
import com.mht.modules.sys.entity.Backup;
import com.mht.modules.sys.entity.BackupLog;

/**
 * @ClassName: BackupService
 * @Description:
 * @author com.mhout.xyb
 * @date 2017年5月17日 下午5:03:05
 * @version 1.0.0
 */
@Service
@Transactional(readOnly = true)
public class BackupService extends CrudService<BackupDao, Backup> {

	@Autowired
	private SysProjectDao sysProjectDao;

	@Autowired
	private BackupDao backupDao;
	
	@Autowired
	private BackupLogService backupLogService;
	
	/**
	 * @Title: backup
	 * @Description: 数据库备份
	 * @param sid
	 * @return
	 * @author com.mhout.xyb
	 */
	public boolean backup(String sid) {
		if (StringUtils.isNotBlank(sid)) {
			SysProject sys = sysProjectDao.get(sid);
			if (sys != null) {
				Backup backup = new Backup();
				backup.setSysProject(sys);
				Backup reback = backupDao.findBySysProject(backup);
				String status = Global.datamap.get(Global.BACKUP+sys.getId());
				boolean msg = StringUtils.isBlank(status) || status != null && !status.equals(Backup.TYPE_WORKING);
				if (reback != null && msg) {
					Global.threadPool.execute(new Runnable() {
						@Override
						public void run() {
							Global.datamap.put(Global.BACKUP+sys.getId(), Backup.TYPE_WORKING);
							String fileName = createFile(reback);
							boolean value = BackupUtils.backup(reback.getBackupExecute(), reback.getHost(),
									reback.getUsername(), reback.getPassword(), reback.getDataname(),
									reback.getTarget() + fileName);
							if (value) {
								BackupLog log = new BackupLog();
								log.setSysProject(sys);
								log.setName(fileName);
								backupLogService.save(log);
								Global.datamap.put(Global.BACKUP+sys.getId(), Backup.TYPE_SUCCESS);
							} else {
								Global.datamap.put(Global.BACKUP+sys.getId(), Backup.TYPE_FAILURE);
							}
						}
					});
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * @Title: restore 
	 * @Description: 数据库还原
	 * @param logid
	 * @return
	 * @author com.mhout.xyb
	 */
	public boolean restore(String logid) {
		if (StringUtils.isNotBlank(logid)) {
			BackupLog log = backupLogService.get(logid);
			SysProject sys = log.getSysProject();
			if (sys != null) {
				Backup backup = new Backup();
				backup.setSysProject(sys);
				Backup reback = backupDao.findBySysProject(backup);
				String status = Global.datamap.get(Global.RESTORE+sys.getId());
				boolean msg = StringUtils.isBlank(status) || status != null && !status.equals(Backup.TYPE_WORKING);
				if (reback != null && msg) {
					Global.threadPool.execute(new Runnable() {
						@Override
						public void run() {
							Global.datamap.put(Global.RESTORE+sys.getId(), Backup.TYPE_WORKING);
							boolean value = BackupUtils.restore(reback.getRestoreExecute(), reback.getHost(),
									reback.getUsername(), reback.getPassword(), reback.getDataname(),
									reback.getTarget() + log.getName());
							if (value) {
								Global.datamap.put(Global.RESTORE+sys.getId(), Backup.TYPE_SUCCESS);
							} else {
								Global.datamap.put(Global.RESTORE+sys.getId(), Backup.TYPE_FAILURE);
							}
						}
					});
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * @Title: createFile 
	 * @Description: 生成sql备份文件
	 * @param reback
	 * @return
	 * @author com.mhout.xyb
	 */
	private String createFile(Backup reback) {
		String time = DateUtils.getDate("yyyy-MM-dd HH-mm-ss") + "";
		String sqlFile = reback.getDataname() + "-" + time +".sql";
		File file = new File(reback.getTarget());
		 if (!file.exists()) {
            if (!file.isDirectory()) {
                file.mkdirs();
            }
        }
		return sqlFile;
	}
}
