package com.mht.modules.sys.entity;

import com.mht.common.persistence.DataEntity;
import com.mht.modules.ident.entity.SysProject;

/**
 * @ClassName: BackupLog
 * @Description: 备份文件路径
 * @author com.mhout.xyb
 * @date 2017年5月17日 下午4:51:39
 * @version 1.0.0
 */
public class BackupLog extends DataEntity<BackupLog> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private SysProject sysProject; // 平台应用
	private String name; // 文件名称

	public SysProject getSysProject() {
		return sysProject;
	}

	public void setSysProject(SysProject sysProject) {
		this.sysProject = sysProject;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
