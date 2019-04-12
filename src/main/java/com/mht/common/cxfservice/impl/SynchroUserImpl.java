package com.mht.common.cxfservice.impl;

import java.util.List;

import javax.jws.WebService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.mht.common.cxfservice.SynchroUser;
import com.mht.common.cxfservice.entity.SynchUser;
import com.mht.common.cxfservice.entity.UserVo;
import com.mht.common.cxfservice.utils.SychConstant;
import com.mht.common.json.AjaxJson;
import com.mht.modules.ident.service.ApplicationService;
import com.mht.modules.sys.entity.User;
import com.mht.modules.sys.service.SystemService;

/**
 * @ClassName: SynchroUserImpl
 * @Description: 同步接口实现
 * @author com.mhout.xyb
 * @date 2017年6月5日 上午10:36:16 
 * @version 1.0.0
 */
@WebService(endpointInterface = "com.mht.common.cxfservice.SynchroUser")
public class SynchroUserImpl implements SynchroUser{
	
	@Autowired
	private ApplicationService applicationService;
	
	@Autowired
	private SystemService systemService;

	@Override
	public AjaxJson synch(UserVo synchUser) {
		AjaxJson ajax = new AjaxJson();
		String appId = synchUser.getAppid();
		String secret = synchUser.getSecret();
		boolean msg = synchUser != null && StringUtils.isNotBlank(appId)
				&& StringUtils.isNotBlank(appId);
		if (msg) {
			if (applicationService.findAppAuth(appId, secret)) {
				updateUser(synchUser);
				ajax.setSuccess(true);
				ajax.setCode("200");
				ajax.setMsg("操作成功！");
			}
		}
		ajax.setSuccess(false);
		ajax.setCode("100");
		ajax.setMsg("操作失败！");
		return ajax;
	}
	
	
	/**
	 * @Title: updateUser 
	 * @Description: TODO
	 * @param synchUser
	 * @author com.mhout.xyb
	 */
	private void updateUser(UserVo synchUser) {
		String type  = synchUser.getType();
		List<SynchUser> list = synchUser.getUsers();
		if (CollectionUtils.isNotEmpty(list)) {
			for (SynchUser user : list) {
				String loginName = user.getUsername();
				if (StringUtils.isNotBlank(loginName)) {
					User reuser = systemService.getTypeUser(loginName);
					//新增
					if (SychConstant.ADD.equals(type)) {
						saveUserAccount(user, reuser);
					} 
					//修改
					else if (SychConstant.UPDATE.equals(type)) {
						modifyUser(user, reuser);
					}
					//删除
					else if (SychConstant.DELETE.equals(type)) {
						if (reuser != null) {
							systemService.deleteByLogic(reuser);
						}
					}
				}
			}
		}
	}
	
	
	
	/**
	 * @Title: updateUser 
	 * @Description: 修改账号
	 * @param sychUser
	 * @param reuser
	 * @author com.mhout.xyb
	 */
	private void modifyUser(SynchUser sychUser, User reuser) {
		if (reuser != null) {
			reuser.setPassword(sychUser.getPassword());
			systemService.saveUser(reuser);
		}
	}
	
	/**
	 * @Title: saveUserAccount 
	 * @Description: 新增账户
	 * @param sychUser
	 * @author com.mhout.xyb
	 */
	private void saveUserAccount(SynchUser sychUser, User user) {
		String loginName = sychUser.getUsername();
		if (user == null) {
			String type = sychUser.getType();
			User newuser = new User();
			setAccountType(type, loginName, newuser);
			systemService.saveUser(newuser);
		}
	}
	
	/**
	 * @Title: setAccountType 
	 * @Description: 设置账号类型
	 * @param type
	 * @author com.mhout.xyb
	 */
	private void setAccountType(String type, String name, User newuser) {
		switch (type) {
		case SychConstant.LOGINNAME:
			newuser.setLoginName(name);
			break;
		case SychConstant.PHONE:
			newuser.setPhone(name);
			break;
		case SychConstant.IDNO:
			newuser.setNo(name);
			break;
		case SychConstant.EMAIL:
			newuser.setEmail(name);
			break;
		case SychConstant.STUDENT:
			newuser.setRoleType(SychConstant.STUROLE);
			newuser.setNo(name);
			break;
		case SychConstant.TEACHER:
			newuser.setRoleType(SychConstant.TEACHER);
			newuser.setNo(name);
			break;
		default:
			break;
		}
	}

}
