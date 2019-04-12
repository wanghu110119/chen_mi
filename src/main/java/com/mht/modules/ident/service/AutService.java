package com.mht.modules.ident.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mht.common.persistence.Page;
import com.mht.common.service.BaseService;
import com.mht.modules.ident.dao.ApplyDao;
import com.mht.modules.ident.entity.Apply;

/**
 * @ClassName: AutService
 * @Description: 
 * @author com.mhout.xyb
 * @date 2017年3月23日 上午11:46:11 
 * @version 1.0.0
 */
@Service
@Transactional(readOnly = true)
public class AutService extends BaseService{

	@Autowired
	private ApplyDao applyDao;
	
	public Apply get(String id) {
		Apply entity = applyDao.get(id);
		return entity;
	}
	
	public Apply getApply(String id) {
		return applyDao.get(id);
	}
	
	public Page<Apply> find(Page<Apply> page, Apply apply) {
		apply.setPage(page);
		page.setList(applyDao.findList(apply));
		return page;
	}
	
	@Transactional(readOnly = false)
	public void save(Apply apply) {
		if (apply.getIsNewRecord()){
			apply.preInsert();
			applyDao.insert(apply);
		}else{
			apply.preUpdate();
			applyDao.update(apply);
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(Apply apply) {
		applyDao.delete(apply);
	}
	
}
