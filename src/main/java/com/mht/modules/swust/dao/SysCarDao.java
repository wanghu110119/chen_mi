package com.mht.modules.swust.dao;

import com.mht.common.persistence.CrudDao;
import com.mht.common.persistence.annotation.MyBatisDao;
import com.mht.modules.swust.entity.Manager;
import com.mht.modules.swust.entity.SysCar;
import com.mht.modules.swust.entity.SysCarExample;

import scala.annotation.meta.param;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
@MyBatisDao
public interface SysCarDao extends CrudDao<SysCar>{
    int countByExample(SysCarExample example);

    int deleteByExample(SysCarExample example);

    int deleteByPrimaryKey(String id);

    int insert(SysCar record);

    int insertSelective(SysCar record);

    List<SysCar> selectByExample(SysCarExample example);

    SysCar selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") SysCar record, @Param("example") SysCarExample example);

    int updateByExample(@Param("record") SysCar record, @Param("example") SysCarExample example);

    int updateByPrimaryKeySelective(SysCar record);

    int updateByPrimaryKey(SysCar record);

    SysCar selectMaxRemark();
    
    @Select("select max(cast(remarks as decimal)) from mht_oeg.sys_car")
	public String findCarMaxRemarks();

	void batchDelete(@Param("ids") String[] ids);
    
}

