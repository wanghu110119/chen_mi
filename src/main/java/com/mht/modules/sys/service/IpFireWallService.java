package com.mht.modules.sys.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mht.common.persistence.Page;
import com.mht.common.service.CrudService;
import com.mht.modules.sys.dao.IpFireWallDao;
import com.mht.modules.sys.entity.IpFireWall;

/**
 * @ClassName: IpFireWallService
 * @Description: IP访问控制业务层
 * @author com.mhout.xyb
 * @date 2017年5月11日 下午3:41:02 
 * @version 1.0.0
 */
@Service
@Transactional(readOnly = true)
public class IpFireWallService extends CrudService<IpFireWallDao, IpFireWall> {
	
	@Autowired
	private IpFireWallDao ipFireWallDao;
	
	public Page<IpFireWall> find(Page<IpFireWall> page, IpFireWall fireWall) {
		fireWall.setPage(page);
		page.setList(ipFireWallDao.findList(fireWall));
		return page;
	}

}
