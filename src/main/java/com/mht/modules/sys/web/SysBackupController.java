package com.mht.modules.sys.web;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mht.common.config.Global;
import com.mht.common.json.AjaxJson;
import com.mht.modules.ident.service.SysProjectService;
import com.mht.modules.sys.entity.BackupLog;
import com.mht.modules.sys.service.BackupLogService;
import com.mht.modules.sys.service.BackupService;

/**
 * @ClassName: SysBackupController
 * @Description: 系统备份与还原
 * @author com.mhout.xyb
 * @date 2017年5月12日 上午9:49:54 
 * @version 1.0.0
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/backup")
public class SysBackupController {

	@Autowired
	private BackupService backupService;
	
	@Autowired
	private SysProjectService sysProjectService;
	
	@Autowired
	private BackupLogService backupLogService;
	
	/**
	 * @Title: index 
	 * @Description: 跳转到备份页面
	 * @return
	 * @author com.mhout.xyb
	 */
	@RequiresPermissions("sys:backup:index")
	@RequestMapping(value = {"index", ""})
	public String index(Model model) {
		model.addAttribute("splist", sysProjectService.findAll());
		return "modules/sys/backup";
	}
	
	/**
	 * @Title: backup 
	 * @Description: 备份
	 * @param sid
	 * @return
	 * @author com.mhout.xyb
	 */
	@ResponseBody
	@RequiresPermissions("sys:backup:copy")
	@RequestMapping(value = {"copy"})
	public AjaxJson backup(String sid) {
		boolean msg = backupService.backup(sid);
		AjaxJson json = new AjaxJson();
		if (msg) {
			json.setSuccess(true);
	    	json.setCode("0");
	    	json.setMsg("操作成功");
		} else {
			json.setSuccess(false);
	    	json.setCode("1");
	    	json.setMsg("操作失败");
		}
		return json;
	}
	
	/**
	 * @Title: restore 
	 * @Description: 还原
	 * @param logid
	 * @return
	 * @author com.mhout.xyb
	 */
	@ResponseBody
	@RequiresPermissions("sys:backup:restore")
	@RequestMapping(value = {"restore"})
	public AjaxJson restore(String logid) {
		boolean msg = backupService.restore(logid);
		AjaxJson json = new AjaxJson();
		if (msg) {
			json.setSuccess(true);
	    	json.setCode("0");
	    	json.setMsg("操作成功");
		} else {
			json.setSuccess(false);
	    	json.setCode("1");
	    	json.setMsg("操作失败");
		}
		return json;
	}
	
	/**
	 * @Title: schedule 
	 * @Description: TODO
	 * @param sid
	 * @param type
	 * @return
	 * @author com.mhout.xyb
	 */
	@ResponseBody
	@RequiresPermissions(value={"sys:backup:restore","sys:backup:copy"},logical=Logical.OR)
	@RequestMapping(value = {"schedule"})
	public String schedule(String sid, String type) {
		if (StringUtils.isNotBlank(sid) && StringUtils.isNotBlank(type)) {
			if ("1".equals(type)) {
				return Global.datamap.get(Global.BACKUP+sid);
			} else if ("2".equals(type)) {
				return Global.datamap.get(Global.RESTORE+sid);
			}
		}
		return "";
	}
	
	/**
	 * @Title: loglist 
	 * @Description: 数据库还原
	 * @return
	 * @author com.mhout.xyb
	 */
	@ResponseBody
	@RequiresPermissions("sys:backup:restore")
	@RequestMapping(value = {"loglist"})
	public List<BackupLog> loglist(String sid) {
		return backupLogService.findBySysProject(sid);
	}
	
}
