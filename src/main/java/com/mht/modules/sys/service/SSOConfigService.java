package com.mht.modules.sys.service;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mht.common.service.CrudService;
import com.mht.modules.sys.dao.SSOConfigDao;
import com.mht.modules.sys.entity.SSOConfig;

/**
 * @ClassName: SSOConfigService
 * @Description: 单点登录业务层
 * @author com.mhout.xyb
 * @date 2017年6月1日 下午2:20:06 
 * @version 1.0.0
 */
@Service
@Transactional(readOnly = true)
public class SSOConfigService  extends CrudService<SSOConfigDao, SSOConfig>{
	
	@Autowired
	private SSOConfigDao ssoConfigDao;
	
	/**
	 * @Title: edit 
	 * @Description: 修改sso配置
	 * @param list
	 * @return
	 * @author com.mhout.xyb
	 */
	@Transactional(readOnly = false)
	public boolean edit(List<SSOConfig> list) {
		if (CollectionUtils.isNotEmpty(list)) {
			for (SSOConfig sso : list) {
				SSOConfig resso = ssoConfigDao.get(sso);
				if (resso != null) {
					resso.setStatus(sso.getStatus());
					this.save(resso);
				}
			}
			return true;
		}
		return false;
	}

}
