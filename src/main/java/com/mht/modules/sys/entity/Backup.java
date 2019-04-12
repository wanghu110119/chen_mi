package com.mht.modules.sys.entity;

import com.mht.common.persistence.DataEntity;
import com.mht.modules.ident.entity.SysProject;

/**
 * @ClassName: Backup
 * @Description: 应用数据库备份配置
 * @author com.mhout.xyb
 * @date 2017年5月17日 下午4:38:28
 * @version 1.0.0
 */
public class Backup extends DataEntity<Backup> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private SysProject sysProject; // 平台应用
	private String backupExecute; // 备份执行文件地址
	private String restoreExecute; // 还原执行文件地址
	private String host; // 地址信息
	private String username; // 用户名
	private String password; // 密码
	private String dataname; // 数据库名称
	private String target; // 目标文件
	
	// 备份缓存类型（1.开始；2：进行中；3：失败；4：成功；）
	public static final String TYPE_START = "1";
	public static final String TYPE_WORKING = "2";
	public static final String TYPE_FAILURE = "3";
	public static final String TYPE_SUCCESS = "4";

	public SysProject getSysProject() {
		return sysProject;
	}

	public void setSysProject(SysProject sysProject) {
		this.sysProject = sysProject;
	}

	public String getBackupExecute() {
		return backupExecute;
	}

	public void setBackupExecute(String backupExecute) {
		this.backupExecute = backupExecute;
	}

	public String getRestoreExecute() {
		return restoreExecute;
	}

	public void setRestoreExecute(String restoreExecute) {
		this.restoreExecute = restoreExecute;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDataname() {
		return dataname;
	}

	public void setDataname(String dataname) {
		this.dataname = dataname;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

}
