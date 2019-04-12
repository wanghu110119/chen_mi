/**
 * Copyright &copy; 2015-2020 <a href="http://www.mht.org/">mht</a> All rights reserved.
 */
package com.mht.modules.test.service.one;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mht.common.persistence.Page;
import com.mht.common.service.CrudService;
import com.mht.modules.test.dao.one.FormLeaveDao;
import com.mht.modules.test.entity.one.FormLeave;

/**
 * 请假表单Service
 * @author lgf
 * @version 2016-10-06
 */
@Service
@Transactional(readOnly = true)
public class FormLeaveService extends CrudService<FormLeaveDao, FormLeave> {

	public FormLeave get(String id) {
		return super.get(id);
	}
	
	public List<FormLeave> findList(FormLeave formLeave) {
		return super.findList(formLeave);
	}
	
	public Page<FormLeave> findPage(Page<FormLeave> page, FormLeave formLeave) {
		return super.findPage(page, formLeave);
	}
	
	@Transactional(readOnly = false)
	public void save(FormLeave formLeave) {
		super.save(formLeave);
	}
	
	@Transactional(readOnly = false)
	public void delete(FormLeave formLeave) {
		super.delete(formLeave);
	}
	
	
	
	
}