/**
 * 
 */
package com.mht.modules.swust.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mht.common.service.CrudService;
import com.mht.modules.swust.dao.UnitDao;
import com.mht.modules.swust.entity.Unit;

/**
 * @ClassName: UnitService
 * @Description: 
 * @author com.mhout.dhn
 * @date 2017年7月26日 下午4:55:01 
 * @version 1.0.0
 */
@Service 
@Transactional(readOnly = true)
public class UnitService extends CrudService<UnitDao, Unit>{

}
