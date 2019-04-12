/**
 * Copyright &copy; 2015-2020 <a href="http://www.mht.org/">mht</a> All rights reserved.
 */
package com.mht.modules.test.service.validation;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mht.common.persistence.Page;
import com.mht.common.service.CrudService;
import com.mht.modules.test.dao.validation.TestValidationDao;
import com.mht.modules.test.entity.validation.TestValidation;

/**
 * 测试校验功能Service
 * @author lgf
 * @version 2016-10-05
 */
@Service
@Transactional(readOnly = true)
public class TestValidationService extends CrudService<TestValidationDao, TestValidation> {

	public TestValidation get(String id) {
		return super.get(id);
	}
	
	public List<TestValidation> findList(TestValidation testValidation) {
		return super.findList(testValidation);
	}
	
	public Page<TestValidation> findPage(Page<TestValidation> page, TestValidation testValidation) {
		return super.findPage(page, testValidation);
	}
	
	@Transactional(readOnly = false)
	public void save(TestValidation testValidation) {
		super.save(testValidation);
	}
	
	@Transactional(readOnly = false)
	public void delete(TestValidation testValidation) {
		super.delete(testValidation);
	}
	
	
	
	
}