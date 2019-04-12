package com.mht.modules.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.mht.common.persistence.CrudDao;
import com.mht.common.persistence.annotation.MyBatisDao;
import com.mht.modules.sys.entity.SysConfig;

/**
 * @ClassName: SysConfigDao
 * @Description: 系统配置持久层
 * @author com.mhout.xyb
 * @date 2017年5月15日 上午11:29:25 
 * @version 1.0.0
 */
@MyBatisDao
public interface SysConfigDao extends CrudDao<SysConfig>{
	
	public List<SysConfig> findTypeList(SysConfig sysConfig);
	
	@Select("select value from sys_config where code = #{0} and del_flag = '0'")
	public SysConfig findByCode(String code);

}
