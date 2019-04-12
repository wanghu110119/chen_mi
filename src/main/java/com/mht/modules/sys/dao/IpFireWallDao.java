package com.mht.modules.sys.dao;

import com.mht.common.persistence.CrudDao;
import com.mht.common.persistence.annotation.MyBatisDao;
import com.mht.modules.sys.entity.IpFireWall;

/**
 * @ClassName: IpFireWallDao
 * @Description: IP访问控制持久层
 * @author com.mhout.xyb
 * @date 2017年5月11日 下午3:41:57 
 * @version 1.0.0
 */
@MyBatisDao
public interface IpFireWallDao extends CrudDao<IpFireWall>{

}
