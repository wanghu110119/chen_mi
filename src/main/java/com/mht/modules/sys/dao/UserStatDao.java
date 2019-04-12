/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.mht.modules.sys.dao;

import org.apache.ibatis.annotations.Select;

import com.mht.common.persistence.CrudDao;
import com.mht.common.persistence.annotation.MyBatisDao;
import com.mht.modules.sys.entity.UserStat;

/**
 * @ClassName: UserStatDao
 * @Description: 用户统计DAO接口
 * @author com.mhout.sx
 * @date 2017年3月23日 上午11:54:21
 * @version 1.0.0
 */
@MyBatisDao
public interface UserStatDao extends CrudDao<UserStat> {

    /**
     * @Title: getCountByTimeAndType
     * @Description: 按照时间段和操作类型统计数量
     * @param timeStart
     * @param timeEnd
     * @param typeId
     * @return
     * @author com.mhout.sx
     */
    @Select("select count(a.id) from sys_user_stat as a where a.create_date>= #{0} and a.create_date<#{1} and a.operation_type=#{2}")
    public int getCountByTimeAndType(String timeStart, String timeEnd, String typeId);

}
