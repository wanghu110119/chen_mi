package com.mht.modules.ident.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mht.common.persistence.Page;
import com.mht.common.service.CrudService;
import com.mht.modules.ident.dao.SysProjectDao;
import com.mht.modules.ident.entity.SysProject;

/**
 * @ClassName: SysProjectService
 * @Description: 项目管理业务层
 * @author com.mhout.xyb
 * @date 2017年5月4日 上午10:32:11 
 * @version 1.0.0
 */
@Service
@Transactional(readOnly = true)
public class SysProjectService extends CrudService<SysProjectDao, SysProject>{
	
	@Autowired
	private SysProjectDao sysProjectDao;
	
	public Page<SysProject> find(Page<SysProject> page, SysProject sysProject) {
		sysProject.setPage(page);
		page.setList(sysProjectDao.findList(sysProject));
		return page;
	}
	
	
	@Transactional(readOnly = false)
    public void deleteSysProject(SysProject sysProject) {
		sysProjectDao.deleteByLogic(sysProject);
	}
	
	/**
	 * @Title: findAll 
	 * @Description: 获取所有项目应用
	 * @return
	 * @author com.mhout.xyb
	 */
	public List<SysProject> findAll() {
		return sysProjectDao.findAll();
	}
}
