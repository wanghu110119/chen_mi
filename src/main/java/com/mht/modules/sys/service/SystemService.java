/**
 * Copyright &copy; 2015-2020 <a href="http://www.mht.org/">mht</a> All rights reserved.
 */
package com.mht.modules.sys.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mht.common.config.Global;
import com.mht.common.persistence.Page;
import com.mht.common.security.Digests;
import com.mht.common.security.shiro.session.SessionDAO;
import com.mht.common.service.BaseService;
import com.mht.common.service.ServiceException;
import com.mht.common.utils.CacheUtils;
import com.mht.common.utils.Encodes;
import com.mht.common.utils.StringUtils;
import com.mht.modules.account.constant.GroupRole;
import com.mht.modules.account.service.CommonService;
import com.mht.modules.fieldconfig.service.UserExtendInfoService;
import com.mht.modules.ident.dao.IdentityGroupDao;
import com.mht.modules.ident.dao.SysProjectDao;
import com.mht.modules.ident.entity.IdentityGroup;
import com.mht.modules.ident.entity.SysProject;
import com.mht.modules.sys.dao.MenuDao;
import com.mht.modules.sys.dao.OfficeDao;
import com.mht.modules.sys.dao.PostDao;
import com.mht.modules.sys.dao.RoleDao;
import com.mht.modules.sys.dao.SSOConfigDao;
import com.mht.modules.sys.dao.UserDao;
import com.mht.modules.sys.entity.Menu;
import com.mht.modules.sys.entity.Office;
import com.mht.modules.sys.entity.Role;
import com.mht.modules.sys.entity.SSOConfig;
import com.mht.modules.sys.entity.User;
import com.mht.modules.sys.security.SystemAuthorizingRealm;
import com.mht.modules.sys.utils.LogUtils;
import com.mht.modules.sys.utils.UserUtils;
import com.mht.modules.unifiedauth.dao.AuthUserDao;
import com.mht.modules.unifiedauth.entity.AuthUser;

/**
 * 系统管理，安全相关实体的管理类,包括用户、角色、菜单.
 * 
 * @author mht
 * @version 2013-12-05
 */
@Service
@Transactional(readOnly = true)
public class SystemService extends BaseService implements InitializingBean {

	public static final String HASH_ALGORITHM = "SHA-1";
	public static final int HASH_INTERATIONS = 1024;
	public static final int SALT_SIZE = 8;

	@Autowired
	private UserDao userDao;
	@Autowired
	private RoleDao roleDao;
	@Autowired
	private MenuDao menuDao;
	@Autowired
	private SessionDAO sessionDao;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private SSOConfigDao ssoConfigDao;

	@Autowired
	private OfficeDao officeDao;

	@Autowired
	private IdentityGroupDao identityGroupDao;

	@Autowired
	private AuthUserDao authUserDao;

	@Autowired
	private SysProjectDao sysProjectDao;

	@Autowired
	private UserStatService userStatService;

	@Autowired
	private UserExtendInfoService userExtendInfoService;

	@Autowired
	private PostDao postDao;

	public SessionDAO getSessionDao() {
		return sessionDao;
	}

	// -- User Service --//

	/**
	 * 获取用户
	 * 
	 * @param id
	 * @return
	 */
	public User getUser(String id) {
		return UserUtils.get(id);
	}
	
	public User getTypeUser(String loginName) {
		SSOConfig sso = new SSOConfig();
		sso.setStatus(Global.ENABLE);
		List<SSOConfig> list = ssoConfigDao.findList(sso);
		List<String> values = new ArrayList<String>();
		if (CollectionUtils.isNotEmpty(list)) {
			for (SSOConfig resso : list) {
				values.add(resso.getField());
			}
			return userDao.getTypeUser(values, loginName);
		}
		return null;
	}

	/**
	 * 根据登录名获取用户
	 * 
	 * @param loginName
	 * @return
	 */
	public User getUserByLoginName(String loginName) {
		return UserUtils.getByLoginName(loginName);
	}

	public Page<User> findUser(Page<User> page, User user) {
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		user.getSqlMap().put("dsf", dataScopeFilter(user.getCurrentUser(), "o", "a"));
		// 设置分页参数
		user.setPage(page);
		// 执行分页查询
		page.setList(userDao.findList(user));
		return page;
	}

	/**
	 * 无分页查询人员列表
	 * 
	 * @param user
	 * @return
	 */
	public List<User> findUser(User user) {
		// 生成数据权限过滤条件（dsf为dataScopeFilter的简写，在xml中使用 ${sqlMap.dsf}调用权限SQL）
		user.getSqlMap().put("dsf", dataScopeFilter(user.getCurrentUser(), "o", "a"));
		List<User> list = userDao.findList(user);
		return list;
	}

	/**
	 * 通过部门ID获取用户列表，仅返回用户id和name（树查询用户时用）
	 * 
	 * @param user
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<User> findUserByOfficeId(String officeId) {
		List<User> list = (List<User>) CacheUtils.get(UserUtils.USER_CACHE,
				UserUtils.USER_CACHE_LIST_BY_OFFICE_ID_ + officeId);
		if (list == null) {
			User user = new User();
			user.setOffice(new Office(officeId));
			list = userDao.findUserByOfficeId(user);
			CacheUtils.put(UserUtils.USER_CACHE, UserUtils.USER_CACHE_LIST_BY_OFFICE_ID_ + officeId, list);
		}
		return list;
	}

	@Transactional(readOnly = false)
	public void saveUser(User user) {
		if (StringUtils.isBlank(user.getId())) {
			user.preInsert();
			userDao.insert(user);
		} else {
			// 清除原用户机构用户缓存
			User oldUser = userDao.get(user.getId());
			if (oldUser.getOffice() != null && oldUser.getOffice().getId() != null) {
				CacheUtils.remove(UserUtils.USER_CACHE,
						UserUtils.USER_CACHE_LIST_BY_OFFICE_ID_ + oldUser.getOffice().getId());
			}
			// 更新用户数据
			user.preUpdate();
			userDao.update(user);
		}
		if (StringUtils.isNotBlank(user.getId())) {
			// 更新用户与角色关联
			userDao.deleteUserRole(user);
			if (user.getRoleList() != null && user.getRoleList().size() > 0) {
				userDao.insertUserRole(user);
			} else {
				throw new ServiceException(user.getLoginName() + "没有设置角色！");
			}
			// 清除用户缓存
			UserUtils.clearCache(user);
			// // 清除权限缓存
			// systemRealm.clearAllCachedAuthorizationInfo();
		}
	}

	@Transactional(readOnly = false)
	public void updateUserInfo(User user) {
		user.preUpdate();
		System.out.println(user.getGender());
		userDao.updateUserInfo(user);
		// 清除用户缓存
		UserUtils.clearCache(user);
		// // 清除权限缓存
		// systemRealm.clearAllCachedAuthorizationInfo();
	}

	@Transactional(readOnly = false)
	public void deleteUser(User user) {
		userDao.delete(user);
		// 清除用户缓存
		UserUtils.clearCache(user);
		// // 清除权限缓存
		// systemRealm.clearAllCachedAuthorizationInfo();
	}
	
	@Transactional(readOnly = false)
	public void deleteByLogic(User user) {
		userDao.deleteByLogic(user);
		// 清除用户缓存
		UserUtils.clearCache(user);
	}

	@Transactional(readOnly = false)
	public void updatePasswordById(String id, String loginName, String newPassword) {
		User user = new User(id);
		user.setPassword(CommonService.encryption(newPassword));
		userDao.updatePasswordById(user);
		// 清除用户缓存
		user.setLoginName(loginName);
		UserUtils.clearCache(user);
		// // 清除权限缓存
		// systemRealm.clearAllCachedAuthorizationInfo();
	}

	@Transactional(readOnly = false)
	public void updateUserLoginInfo(User user) {
		// 保存上次登录信息
		user.setOldLoginIp(user.getLoginIp());
		user.setOldLoginDate(user.getLoginDate());
		// 更新本次登录信息
		user.setLoginIp(UserUtils.getSession().getHost());
		user.setLoginDate(new Date());
		userDao.updateLoginInfo(user);
	}

	/**
	 * 生成安全的密码，生成随机的16位salt并经过1024次 sha-1 hash
	 */
	public static String entryptPassword(String plainPassword) {
		byte[] salt = Digests.generateSalt(SALT_SIZE);
		byte[] hashPassword = Digests.sha1(plainPassword.getBytes(), salt, HASH_INTERATIONS);
		return Encodes.encodeHex(salt) + Encodes.encodeHex(hashPassword);
	}

	/**
	 * 验证密码
	 * 
	 * @param plainPassword
	 *            明文密码
	 * @param password
	 *            密文密码
	 * @return 验证成功返回true
	 */
	public static boolean validatePassword(String plainPassword, String password) {
		byte[] salt = Encodes.decodeHex(password.substring(0, 16));
		byte[] hashPassword = Digests.sha1(plainPassword.getBytes(), salt, HASH_INTERATIONS);
		return password.equals(Encodes.encodeHex(salt) + Encodes.encodeHex(hashPassword));
	}

	/**
	 * 获得活动会话
	 * 
	 * @return
	 */
	public Collection<Session> getActiveSessions() {
		return sessionDao.getActiveSessions(false);
	}

	// -- Role Service --//

	public Role getRole(String id) {
		return roleDao.get(id);
	}

	public Role getRoleByName(String name) {
		Role r = new Role();
		r.setName(name);
		return roleDao.getByName(r);
	}

	public Role getRoleByEnname(String enname) {
		Role r = new Role();
		r.setEnname(enname);
		return roleDao.getByEnname(r);
	}

	public List<Role> findRole(Role role) {
		return roleDao.findList(role);
	}

	public List<Role> findAllRole() {
		return UserUtils.getRoleList();
	}

	@Transactional(readOnly = false)
	public void saveRole(Role role) {
		roleService.save(role);
		// 清除用户角色缓存
		UserUtils.removeCache(UserUtils.CACHE_ROLE_LIST);
		// // 清除权限缓存
		// systemRealm.clearAllCachedAuthorizationInfo();
	}

	@Transactional(readOnly = false)
	public void deleteRole(Role role) {
		roleDao.delete(role);
		// 清除用户角色缓存
		UserUtils.removeCache(UserUtils.CACHE_ROLE_LIST);
		// // 清除权限缓存
		// systemRealm.clearAllCachedAuthorizationInfo();
	}

	@Transactional(readOnly = false)
	public Boolean outUserInRole(Role role, User user) {
		List<Role> roles = user.getRoleList();
		for (Role e : roles) {
			if (e.getId().equals(role.getId())) {
				roles.remove(e);
				saveUser(user);
				return true;
			}
		}
		return false;
	}

	@Transactional(readOnly = false)
	public User assignUserToRole(Role role, User user) {
		if (user == null) {
			return null;
		}
		List<String> roleIds = user.getRoleIdList();
		if (roleIds.contains(role.getId())) {
			return null;
		}
		user.getRoleList().add(role);
		saveUser(user);
		return user;
	}

	// -- Menu Service --//

	public Menu getMenu(String id) {
		return menuDao.get(id);
	}

	public List<Menu> findAllMenu() {
		return menuDao.findAllList(new Menu());
	}

	public List<Menu> findProjectAllMenu(String sprId) {
		if (StringUtils.isNotBlank(sprId)) {
			SysProject sysProject = sysProjectDao.get(sprId);
			if (sysProject != null) {
				Menu m = new Menu();
				m.setSysProject(sysProject);
				User user = UserUtils.getUser();
				if (user.isAdmin()) {
					return menuDao.findProjectAllList(m);
				} else {
					m.setRoles(user.getRoleIdList());
					return menuDao.findProjectRoleAllList(m);
				}
			}
		}
		return null;
	}

	@Transactional(readOnly = false)
	public void saveMenu(Menu menu) {

		// 获取父节点实体
		menu.setParent(this.getMenu(menu.getParent().getId()));

		// 获取修改前的parentIds，用于更新子节点的parentIds
		String oldParentIds = menu.getParentIds();

		// 设置新的父节点串
		menu.setParentIds(menu.getParent().getParentIds() + menu.getParent().getId() + ",");

		// 保存或更新实体
		if (StringUtils.isBlank(menu.getId())) {
			menu.preInsert();
			menuDao.insert(menu);
		} else {
			menu.preUpdate();
			menuDao.update(menu);
		}

		// 更新子节点 parentIds
		Menu m = new Menu();
		m.setParentIds("%," + menu.getId() + ",%");
		List<Menu> list = menuDao.findByParentIdsLike(m);
		for (Menu e : list) {
			e.setParentIds(e.getParentIds().replace(oldParentIds, menu.getParentIds()));
			menuDao.updateParentIds(e);
		}
		// 清除用户菜单缓存
		UserUtils.removeCache(UserUtils.CACHE_MENU_LIST);
		// // 清除权限缓存
		// systemRealm.clearAllCachedAuthorizationInfo();
		// 清除日志相关缓存
		CacheUtils.remove(LogUtils.CACHE_MENU_NAME_PATH_MAP);
	}

	@Transactional(readOnly = false)
	public void saveRoleMenu(Role role, String pId) {
		// 更新角色与菜单关联
		role.setpId(pId);
		roleDao.deleteRoleMenu(role);
		roleDao.deleteRoleRootMenu(role.getId(), "1");
		if (role.getMenuList().size() > 0) {
			roleDao.insertRoleMenu(role);
		}
	}

	@Transactional(readOnly = false)
	public void updateMenuSort(Menu menu) {
		menuDao.updateSort(menu);
		// 清除用户菜单缓存
		UserUtils.removeCache(UserUtils.CACHE_MENU_LIST);
		// // 清除权限缓存
		// systemRealm.clearAllCachedAuthorizationInfo();
		// 清除日志相关缓存
		CacheUtils.remove(LogUtils.CACHE_MENU_NAME_PATH_MAP);
	}

	@Transactional(readOnly = false)
	public void deleteMenu(Menu menu) {
		menuDao.delete(menu);
		// 清除用户菜单缓存
		UserUtils.removeCache(UserUtils.CACHE_MENU_LIST);
		// // 清除权限缓存
		// systemRealm.clearAllCachedAuthorizationInfo();
		// 清除日志相关缓存
		CacheUtils.remove(LogUtils.CACHE_MENU_NAME_PATH_MAP);
	}

	/**
	 * 获取Key加载信息
	 */
	public static boolean printKeyLoadMessage() {
		StringBuilder sb = new StringBuilder();
		sb.append("\r\n======================================================================\r\n");
		sb.append("\r\n    欢迎使用 " + Global.getConfig("productName") + "  - Powered By http://www.mht.org\r\n");
		sb.append("\r\n======================================================================\r\n");
		System.out.println(sb.toString());
		return true;
	}

	///////////////// Synchronized to the Activiti //////////////////

	// 已废弃，同步见：ActGroupEntityServiceFactory.java、ActUserEntityServiceFactory.java

	/**
	 * 是需要同步Activiti数据，如果从未同步过，则同步数据。
	 */
	private static boolean isSynActivitiIndetity = true;

	public void afterPropertiesSet() throws Exception {
	}


	@Transactional(readOnly = false)
	public void saveUser(User user, Map<String, String[]> parameters, GroupRole groupRole) {
		if (StringUtils.isBlank(user.getId())) {
			user.preInsert();
			userDao.insert(user);
			userStatService.save(user, UserStatService.INSERT);
		} else {
			// 清除原用户机构用户缓存
			User oldUser = userDao.get(user.getId());
			// if (oldUser.getOffice() != null && oldUser.getOffice().getId() !=
			// null) {
			// CacheUtils.remove(UserUtils.USER_CACHE,
			// UserUtils.USER_CACHE_LIST_BY_OFFICE_ID_ +
			// oldUser.getOffice().getId());
			// }
			// 更新用户数据
			user.preUpdate();
			userDao.update(user);
			userStatService.save(user, UserStatService.UPDATE);
		}
		if (StringUtils.isNotBlank(user.getId())) {
			// 更新用户与角色关联
			userDao.deleteUserRole(user);
			if (user.getRoleList() != null && user.getRoleList().size() > 0) {
				userDao.insertUserRole(user);
			} else {
				throw new ServiceException(user.getLoginName() + "没有设置角色！");
			}

			userExtendInfoService.saveUserExtendInfo(parameters, groupRole, user.getId());
			// 清除用户缓存
			UserUtils.clearCache(user);
			// // 清除权限缓存
			// systemRealm.clearAllCachedAuthorizationInfo();
		}
		// 保存用户与岗位对应关系
		this.postDao.deleteUserPost(user.getId());
		String postIds = user.getPostIds();
		String[] ids = postIds.split(",");
		// 再添加
		for (String postId : ids) {
			this.postDao.insertUserPost(user.getId(), postId);
		}
	}

	public User editUser(User user) {
		// 获取部门信息
		List<Office> offices = officeDao.findOfficeByUser(user.getId());
		if (offices != null && !offices.isEmpty()) {
			user.setOffice(offices.get(0));
		}
		List<String> officeNames = offices.stream().map(Office::getName).collect(Collectors.toList());
		user.setOfficeNames(String.join(",", officeNames));
		// 获取组信息
		List<IdentityGroup> groups = identityGroupDao.findIdentityGroupByUser(user.getId());
		List<String> groupNames = groups.stream().map(IdentityGroup::getGroupName).collect(Collectors.toList());
		user.setGroupNames(String.join(",", groupNames));
		return user;
	}

	public List<AuthUser> findAuthUser(User user) {
		String type = Global.SYSTEM;
		String status = Global.ENABLE;
		String accessway = Global.CASACESSWAY;
		String userId = user.getId();
		List<AuthUser> authlist = this.authUserDao.findListByAuthUser(userId, type, status, accessway);
		return authlist;
	}

	///////////////// Synchronized to the Activiti end //////////////////

}
