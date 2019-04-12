/**
 * 
 */
package com.mht.modules.swust.service;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.hssf.util.HSSFColor.GOLD;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mht.common.config.Global;
import com.mht.common.service.CrudService;
import com.mht.common.utils.DateUtils;
import com.mht.common.utils.IdGen;
import com.mht.common.utils.MD5Utils;
import com.mht.common.utils.StringUtils;
import com.mht.modules.swust.dao.SysAreaDao;
import com.mht.modules.swust.dao.SysOfficeDao;
import com.mht.modules.swust.dao.SysRoleDao;
import com.mht.modules.swust.dao.SysUserDao;
import com.mht.modules.sys.dao.AreaDao;
import com.mht.modules.sys.dao.OfficeDao;
import com.mht.modules.sys.dao.RoleDao;
import com.mht.modules.sys.dao.UserDao;
import com.mht.modules.sys.entity.Area;
import com.mht.modules.sys.entity.Office;
import com.mht.modules.sys.entity.Role;
import com.mht.modules.sys.entity.User;
import com.mht.modules.sys.utils.UserUtils;

/**
 * @ClassName: ManagerService
 * @Description:
 * @author com.mhout.dhn
 * @date 2017年7月26日 下午4:16:30
 * @version 1.0.0
 */
@Service
@Transactional(readOnly = true)
public class ManagerService extends CrudService<SysOfficeDao, Office> {
	
	@Autowired
	private SysOfficeDao officeDao;
	
	@Autowired
	private SysUserDao userDao;
	
	@Autowired
	private SysRoleDao roleDao;
	
	@Autowired
	private SysAreaDao arearDao;
	
	/**
	 * @Title: checkName 
	 * @Description: 检查用户是否存在
	 * @param name
	 * @param loginname
	 * @param oldname
	 * @return
	 * @author com.mhout.xyb
	 */
	public String checkName(String name, String loginname, String oldname) {
		String exist = "true";
		//true表示不存在：可以添加
		//校验单位名称
		if (StringUtils.isNotBlank(name)) {
			if (!name.equals(oldname)) {
				Office office = officeDao.findByName(name);
				exist = office != null ? "false":"true";
			}
		} 
		//登录名
		else if (StringUtils.isNotBlank(loginname)) {
			if (!loginname.equals(oldname)) {
				User user = userDao.findUserByLoginName(loginname);
				exist = user != null ? "false":"true";
			}
		}
		return exist;
	}
	
	/**
	 * @Title: saveOffice 
	 * @Description:  保存单位
	 * @param office
	 * @param oldloginName
	 * @author com.mhout.xyb
	 */
	@Transactional(readOnly = false)
	public void saveOffice(Office office, String oldloginName) {
		String newLoginName = office.getPrimaryPerson().getLoginName();
		String newPersonname = office.getPrimaryPerson().getName();
		String newPersonPhone = office.getPrimaryPerson().getPhone();
		
		User user = saveUser(office, oldloginName);
		if (user != null) {
			office.setPrimaryPerson(user);
		}
		if (office.getIsNewRecord()){
			office.setCode(getCode());
			//添加角色信息
			Office poffice = officeDao.get("1");
			Area area = arearDao.get("1");
			office.setParent(poffice);
			office.setParentIds("0,1,");
			office.setArea(area);
			office.setGrade("2");
			office.setUseable(Global.ENABLE);
			office.preInsert();
			dao.insert(office);
		} else {
			List<User> list = officeDao.findUserByOfficeCache(office.getId());
			if (CollectionUtils.isNotEmpty(list)) {
				for (User reuser : list) {
					UserUtils.clearCache(reuser);
				}
			}
			office.preUpdate();
			dao.update(office);
		}
		if (user != null) {
			if(newLoginName!=null&&!"".equals(newLoginName)){
				user.setLoginName(newLoginName);
			}
			if(newPersonname!=null&&!"".equals(newPersonname)){
				user.setName(newPersonname);
			}
			if(newPersonPhone!=null&&!"".equals(newPersonPhone)){
				user.setPhone(newPersonPhone);
			}
			user.setOffice(office);
			userDao.update(user);
		}
	}
	
	private String getCode() {
		String code = officeDao.findOfficeMaxCode();
		String year = DateUtils.getYearBack();
		if (code.indexOf(year) > -1) {
			// 序号加1
			String end = String.valueOf(Integer.parseInt(code) + 1);
		return end;
		} else {
		return	DateUtils.getYearBack() + "001";
		}
	
		}
	
	
	/**
	 * @Title: saveUser 
	 * @Description: 保存用户
	 * @param office
	 * @param oldloginName
	 * @return
	 * @author com.mhout.xyb
	 */
	private User saveUser(Office office, String oldloginName) {
		if(officeDao.get(office.getId())!=null){
			return userDao.get(office.getPrimaryPerson().getId());
		}
		if (!oldloginName.equals(office.getPrimaryPerson().getLoginName())) {
			User user = new User();
			BeanUtils.copyProperties(office.getPrimaryPerson(), user);
			user.setId(IdGen.uuid());
			user.setPassword(MD5Utils.make("123456"));
			user.preInsert();
			userDao.insert(user);
			//添加角色信息
			List<Role> list = roleDao.findRoleByEnname("general_user");
			if (CollectionUtils.isNotEmpty(list)) {
				user.getRoleList().add(list.get(0));
			}
			return user;
		}
		return null;
	}
	
	/**
	 * @Title: updateState 
	 * @Description: 禁用/启用
	 * @param office
	 * @param state
	 * @author com.mhout.xyb
	 */
	@Transactional(readOnly = false)
	public void updateState(Office office, String state) {
		List<User> list = officeDao.findUserByOfficeCache(office.getId());
		if (CollectionUtils.isNotEmpty(list)) {
			for (User user : list) {
				UserUtils.clearCache(user);
			}
		}
		officeDao.updateState(office.getId(), state);
	}

	public static Office getOfficeExample() {
	Office office = new Office();
	office.setName("（必填）");
	office.setPrimaryPerson(UserUtils.getUser());
		return office;
	}
	@Transactional(readOnly = false)
	public void batchDelete(String[] ids) {
		// TODO Auto-generated method stub
		for (String string : ids) {
			List<User> list = officeDao.findUserByOfficeCache(string);
			if (CollectionUtils.isNotEmpty(list)) {
				for (User user : list) {
					userDao.delete(user.getId());
					UserUtils.clearCache(user);
				}
			}
		}
		officeDao.batchDelete(ids);
	}

}