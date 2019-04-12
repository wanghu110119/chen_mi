package com.mht.modules.tools.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mht.common.persistence.Page;
import com.mht.common.service.CrudService;
import com.mht.modules.sys.entity.User;
import com.mht.modules.tools.dao.UserVerificationCodeDao;
import com.mht.modules.tools.entity.UserVerificationCode;

@Service
@Transactional
public class UserVerificationCodeService extends CrudService<UserVerificationCodeDao, UserVerificationCode> {

	@Autowired
	private UserVerificationCodeDao userVerificationCodeDao;

	public UserVerificationCode get(String id) {
		return super.get(id);
	}

	public List<UserVerificationCode> findList(UserVerificationCode userVerificationCode) {
		return super.findList(userVerificationCode);
	}

	public Page<UserVerificationCode> findPage(Page<UserVerificationCode> page,
			UserVerificationCode userVerificationCode) {
		return super.findPage(page, userVerificationCode);
	}

	public UserVerificationCode getByUserId(User user) {
		return userVerificationCodeDao.getByUserId(user);
	}

	@Transactional(readOnly = false)
	public void save(UserVerificationCode userVerificationCode) {
		super.save(userVerificationCode);
	}

	@Transactional(readOnly = false)
	public void delete(UserVerificationCode userVerificationCode) {
		super.delete(userVerificationCode);
	}
}
