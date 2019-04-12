package com.mht.modules.ident.dao;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.mht.common.persistence.CrudDao;
import com.mht.common.persistence.annotation.MyBatisDao;
import com.mht.modules.ident.entity.SysProject;

/**
 * @ClassName: SysProjectDao
 * @Description: 项目管理持久层
 * @author com.mhout.xyb
 * @date 2017年5月4日 上午10:36:50 
 * @version 1.0.0
 */
@MyBatisDao
public interface SysProjectDao extends CrudDao<SysProject>{
	
	public List<SysProject> findAll();
	
	@Select("SELECT id FROM ident_sys_project WHERE `code` = #{0}")
	public SysProject findByCode(String code);
	
}
