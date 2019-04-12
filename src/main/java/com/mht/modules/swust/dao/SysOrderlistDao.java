package com.mht.modules.swust.dao;

import com.mht.common.persistence.CrudDao;
import com.mht.common.persistence.annotation.MyBatisDao;
import com.mht.modules.swust.entity.SysOrderlist;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 
 * @ClassName: SysOrderlistDao
 * @Description: 
 * @author com.mhout.wzw
 * @date 2017年7月26日 下午3:45:00 
 * @version 1.0.0
 */
@MyBatisDao
public interface SysOrderlistDao extends CrudDao<SysOrderlist> {

    int deleteByPrimaryKey(Integer id);

    int insert(SysOrderlist record);

    int insertSelective(SysOrderlist record);


    SysOrderlist selectByPrimaryKey(Integer id);

    List<SysOrderlist>  selectByCarId(SysOrderlist sysOrderlist);
	
	/**
	 * @Title: findMaxOrder 
	 * @Description: 查找最大预约序号
	 * @return
	 * @author com.mhout.xyb
	 */
	@Select("select max(cast(order_id as decimal)) from mht_oeg.sys_orderlist")
	public String findMaxOrder();

	Object selectByRemark(int i);

	void batchDelete(@Param("ids") String[] ids);

	List<SysOrderlist> selectRemoveOrder(Date date);

	void deleteRemoveOrder(List<SysOrderlist> list);

	String selectSmsPhone();
	
}






