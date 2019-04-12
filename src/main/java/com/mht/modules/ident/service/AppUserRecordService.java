package com.mht.modules.ident.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mht.common.service.CrudService;
import com.mht.modules.ident.dao.AppUserRecordDao;
import com.mht.modules.ident.entity.AppUserRecord;

/**
 * @ClassName: AppUserRecordService
 * @Description: 应用访问记录
 * @author com.mhout.xyb
 * @date 2017年5月25日 上午11:59:44 
 * @version 1.0.0
 */
@Service
@Transactional(readOnly = true)
public class AppUserRecordService  extends CrudService<AppUserRecordDao, AppUserRecord>{

	
	
}
