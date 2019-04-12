/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.mht.modules.sys.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.mht.common.config.Global;
import com.mht.common.enumutils.ProjectNameEnum;
import com.mht.common.service.BaseService;
import com.mht.common.sms.SMSUtils;
import com.mht.common.utils.CacheUtils;
import com.mht.common.utils.IDCardUtil;
import com.mht.common.utils.RegularUtils;
import com.mht.common.utils.SpringContextHolder;
import com.mht.modules.ident.dao.SysProjectDao;
import com.mht.modules.ident.entity.SysProject;
import com.mht.modules.sys.dao.AreaDao;
import com.mht.modules.sys.dao.MenuDao;
import com.mht.modules.sys.dao.OfficeDao;
import com.mht.modules.sys.dao.RoleDao;
import com.mht.modules.sys.dao.UserDao;
import com.mht.modules.sys.entity.Area;
import com.mht.modules.sys.entity.Menu;
import com.mht.modules.sys.entity.Office;
import com.mht.modules.sys.entity.Role;
import com.mht.modules.sys.entity.User;
import com.mht.modules.sys.security.SystemAuthorizingRealm.Principal;

/**
 * 用户工具类
 * 
 * @author jeeplus
 * @version 2013-12-05
 */
@Component
public class UserUtils {

    private static UserDao userDao = SpringContextHolder.getBean(UserDao.class);
    private static RoleDao roleDao = SpringContextHolder.getBean(RoleDao.class);
    private static MenuDao menuDao = SpringContextHolder.getBean(MenuDao.class);
    private static AreaDao areaDao = SpringContextHolder.getBean(AreaDao.class);
    private static OfficeDao officeDao = SpringContextHolder.getBean(OfficeDao.class);
    private static SysProjectDao sysProjectDao = SpringContextHolder.getBean(SysProjectDao.class);
    
    
    private static String projectName;
    @Value("${projectName}")
    private String initpn;
    
    
    public static final String USER_CACHE = "userCache";
    public static final String USER_CACHE_ID_ = "id_";
    public static final String USER_CACHE_LOGIN_NAME_ = "ln";
    public static final String USER_CACHE_LIST_BY_OFFICE_ID_ = "oid_";

    public static final String CACHE_ROLE_LIST = "roleList";
    public static final String CACHE_MENU_LIST = "menuList";
    public static final String CACHE_AREA_LIST = "areaList";
    public static final String CACHE_OFFICE_LIST = "officeList";
    public static final String CACHE_OFFICE_ALL_LIST = "officeAllList";

	@PostConstruct
    public void init() { 
		UserUtils.projectName = this.initpn; 
    }

	/**
     * 根据ID获取用户
     * 
     * @param id
     * @return 取不到返回null
     */
    public static User get(String id) {
        User user = (User) CacheUtils.get(USER_CACHE, USER_CACHE_ID_ + id);
        if (user == null) {
            user = userDao.get(id);
            if (user == null) {
                return null;
            }
            user.setRoleList(roleDao.findList(new Role(user)));
            CacheUtils.put(USER_CACHE, USER_CACHE_ID_ + user.getId(), user);
            CacheUtils.put(USER_CACHE, USER_CACHE_LOGIN_NAME_ + user.getLoginName(), user);
        }
        return user;
    }

    /**
     * 根据登录名获取用户
     * 
     * @param loginName
     * @return 取不到返回null
     */
    public static User getByLoginName(String loginName) {
        User user = (User) CacheUtils.get(USER_CACHE, USER_CACHE_LOGIN_NAME_ + loginName);
        if (user == null) {
            user = userDao.getByLoginName(new User(null, loginName));
            if (user == null) {
                return null;
            }
            user.setRoleList(roleDao.findList(new Role(user)));
            CacheUtils.put(USER_CACHE, USER_CACHE_ID_ + user.getId(), user);
            CacheUtils.put(USER_CACHE, USER_CACHE_LOGIN_NAME_ + user.getLoginName(), user);
        }
        return user;
    }
    
    /**
   	 * @Title: getUser 
   	 * @Description: 获取单个用户信息
   	 * @param String
   	 * @return
   	 * @author com.mhout.xyb
   	 */
   	public static String checkUser(User user) {
   		String loginName = user.getLoginName();
   		String oldLoginName = user.getOldLoginName();
   		User reuser = userDao.getByLoginName(user);
   		String oldId = user.getId() == null?"":user.getId();
   		String newId = reuser != null ? reuser.getId():oldId;
   		boolean msg = loginName != null && loginName.equals(oldLoginName)
   				&& reuser == null;
   		if (msg) {
            return "true";
        } else if (StringUtils.isBlank(newId) || oldId.equals(newId)) {
        	return "true";
        }
        return "false";
   	}
   	
   	/**
   	 * @Title: checkParamUser 
   	 * @Description: 用户参数校验
   	 * @return
   	 * @author com.mhout.xyb
   	 */
   	public static String checkParamUser(User user) {
   		if (!RegularUtils.telphone(user.getMobile())){
   			return "false";
   		} else if (!RegularUtils.checkEmail(user.getEmail())) {
   			return "false";
   		} else if (!IDCardUtil.isIDCard(user.getIdNo())) {
   			return "false";
   		} else {
   			return "true";
   		}
   	}

    /**
     * 清除当前用户缓存
     */
    public static void clearCache() {
        removeCache(CACHE_ROLE_LIST);
        removeCache(CACHE_MENU_LIST);
        removeCache(CACHE_AREA_LIST);
        removeCache(CACHE_OFFICE_LIST);
        removeCache(CACHE_OFFICE_ALL_LIST);
        UserUtils.clearCache(getUser());
    }

    /**
     * 清除指定用户缓存
     * 
     * @param user
     */
    public static void clearCache(User user) {
        CacheUtils.remove(USER_CACHE, USER_CACHE_ID_ + user.getId());
        CacheUtils.remove(USER_CACHE, USER_CACHE_LOGIN_NAME_ + user.getLoginName());
        CacheUtils.remove(USER_CACHE, USER_CACHE_LOGIN_NAME_ + user.getOldLoginName());
        // if (user.getOffice() != null && user.getOffice().getId() != null){
        // CacheUtils.remove(USER_CACHE, USER_CACHE_LIST_BY_OFFICE_ID_ +
        // user.getOffice().getId());
        // }
    }

    /**
     * 获取当前用户
     * 
     * @return 取不到返回 new User()
     */
    public static User getUser() {
        Principal principal = getPrincipal();
        if (principal != null) {
            User user = get(principal.getId());
            if (user != null) {
                return user;
            }
            return new User();
        }
        // 如果没有登录，则返回实例化空的User对象。
        return new User();
    }
    /**
     * @Title: getUserImage 
     * @Description: 默认头像
     * @return
     * @author com.mhout.xyb
     */
    public static String getUserImage(String baseurl) {
    	Principal principal = getPrincipal();
        if (principal != null) {
            User user = get(principal.getId());
            if (user != null && StringUtils.isNotBlank(user.getPhoto())) {
                return user.getPhoto();
            }
        }
        return baseurl + Global.PHOTO_IMAGE;
    }
    
    /**
     * 通过ID获取用户角色
     * 
     * @param userId
     * @return
     */
    public static String getUserRole(String userId) {
        List<Role> roleList = new ArrayList<Role>();
        User user = get(userId);
        if (user != null) {
            Role role = new Role();
            role.setUser(user);
            roleList = roleDao.findUserRole(role);
        }
        // 获取角色信息
        String msg = "";
        if (roleList != null && roleList.size() > 0) {
            for (Role role : roleList) {
                msg += role.getName() + ",";
            }
        }
        return msg.length() > 50 ? msg.substring(0, 50) + "..." : msg;
    }

    /**
     * 获取当前用户角色列表
     * 
     * @return
     */
    public static List<Role> getRoleList() {
        @SuppressWarnings("unchecked")
        List<Role> roleList = (List<Role>) getCache(CACHE_ROLE_LIST);
        if (roleList == null) {
            User user = getUser();
            if (user.isAdmin()) {
                roleList = roleDao.findAllList(new Role());
            } else {
                Role role = new Role();
                role.getSqlMap().put("dsf", BaseService.dataScopeFilter(user.getCurrentUser(), "o", "u"));
                roleList = roleDao.findList(role);
            }
            putCache(CACHE_ROLE_LIST, roleList);
        }
        return roleList;
    }

    /**
     * 获取当前用户授权菜单
     * 
     * @return
     */
    public static List<Menu> getMenuList() {
        @SuppressWarnings("unchecked")
        List<Menu> menuList = (List<Menu>) getCache(CACHE_MENU_LIST);
        if (menuList == null || menuList.size() <= 0) {
        	Menu m = new Menu();
        	m.setSysProject(getProject(projectName));
            User user = getUser();
            if (user.isAdmin()) {
                menuList = menuDao.findAllList(m);
            } else {
                m.setUserId(user.getId());
                menuList = menuDao.findByUserId(m);
                //menuList = menuDao.findByUserIdAndAppID(m);
            }
            putCache(CACHE_MENU_LIST, menuList);
        }
        return menuList;
    }

    /**
     * 获取当前用户授权菜单
     * 
     * @return
     */
    public static Menu getTopMenu() {
    	List<Menu> menuList = getMenuList();
    	Menu topMenu = new Menu();
    	if (menuList != null && menuList.size() > 0) {
    		topMenu = getMenuList().get(0);
    	}
        return topMenu;
    }

    /**
     * 获取当前用户授权的区域
     * 
     * @return
     */
    public static List<Area> getAreaList() {
        @SuppressWarnings("unchecked")
        List<Area> areaList = (List<Area>) getCache(CACHE_AREA_LIST);
        if (areaList == null) {
            areaList = areaDao.findAllList(new Area());
            putCache(CACHE_AREA_LIST, areaList);
        }
        return areaList;
    }

    /**
     * 获取当前用户有权限访问的部门
     * 
     * @return
     */
    public static List<Office> getOfficeList() {
        @SuppressWarnings("unchecked")
        List<Office> officeList = (List<Office>) getCache(CACHE_OFFICE_LIST);
        if (officeList == null) {
            User user = getUser();
            if (user.isAdmin()) {
                officeList = officeDao.findAllList(new Office());
            } else {
                Office office = new Office();
                office.getSqlMap().put("dsf", BaseService.dataScopeFilter(user, "a", ""));
                officeList = officeDao.findList(office);
            }
            putCache(CACHE_OFFICE_LIST, officeList);
        }
        return officeList;
    }

    /**
     * 获取当前用户有权限访问的部门
     * 
     * @return
     */
    public static List<Office> getOfficeAllList() {
        @SuppressWarnings("unchecked")
        List<Office> officeList = (List<Office>) getCache(CACHE_OFFICE_ALL_LIST);
        if (officeList == null) {
            officeList = officeDao.findAllList(new Office());
        }
        return officeList;
    }

    /**
     * 获取授权主要对象
     */
    public static Subject getSubject() {
        return SecurityUtils.getSubject();
    }

    /**
     * 获取当前登录者对象
     */
    public static Principal getPrincipal() {
        try {
            Subject subject = SecurityUtils.getSubject();
            Principal principal = (Principal) subject.getPrincipal();
            if (principal != null) {
                return principal;
            }
            // subject.logout();
        } catch (UnavailableSecurityManagerException e) {

        } catch (InvalidSessionException e) {

        }
        return null;
    }

    public static Session getSession() {
        try {
            Subject subject = SecurityUtils.getSubject();
            Session session = subject.getSession(false);
            if (session == null) {
                session = subject.getSession();
            }
            if (session != null) {
                return session;
            }
            // subject.logout();
        } catch (InvalidSessionException e) {

        }
        return null;
    }

    // ============== User Cache ==============

    public static Object getCache(String key) {
        return getCache(key, null);
    }

    public static Object getCache(String key, Object defaultValue) {
        // Object obj = getCacheMap().get(key);
        Object obj = getSession().getAttribute(key);
        return obj == null ? defaultValue : obj;
    }

    public static void putCache(String key, Object value) {
        // getCacheMap().put(key, value);
        getSession().setAttribute(key, value);
    }

    public static void removeCache(String key) {
        // getCacheMap().remove(key);
        getSession().removeAttribute(key);
    }

    public static String getTime(Date date) {
        StringBuffer time = new StringBuffer();
        Date date2 = new Date();
        long temp = date2.getTime() - date.getTime();
        long days = temp / 1000 / 3600 / 24; // 相差小时数
        if (days > 0) {
            time.append(days + "天");
        }
        long temp1 = temp % (1000 * 3600 * 24);
        long hours = temp1 / 1000 / 3600; // 相差小时数
        if (days > 0 || hours > 0) {
            time.append(hours + "小时");
        }
        long temp2 = temp1 % (1000 * 3600);
        long mins = temp2 / 1000 / 60; // 相差分钟数
        time.append(mins + "分钟");
        return time.toString();
    }

    // 发送注册码
    public static String sendRandomCode(String uid, String pwd, String tel, String randomCode) throws IOException {
        // 发送内容
        String content = "您的验证码是：" + randomCode + "，有效期30分钟，请在有效期内使用。";

        return SMSUtils.send(uid, pwd, tel, content);

    }

    // 注册用户重置密码
    public static String sendPass(String uid, String pwd, String tel, String password) throws IOException {
        // 发送内容
        String content = "您的新密码是：" + password + "，请登录系统，重新设置密码。";
        return SMSUtils.send(uid, pwd, tel, content);

    }

    /**
     * 导出Excel调用,根据姓名转换为ID
     */
    public static User getByUserName(String name) {
        User u = new User();
        u.setName(name);
        List<User> list = userDao.findList(u);
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return new User();
        }
    }

    /**
     * 导出Excel使用，根据名字转换为id
     */
    public static Office getByOfficeName(String name) {
        Office o = new Office();
        o.setName(name);
        List<Office> list = officeDao.findList(o);
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return new Office();
        }
    }

    /**
     * 导出Excel使用，根据名字转换为id
     */
    public static Area getByAreaName(String name) {
        Area a = new Area();
        a.setName(name);
        List<Area> list = areaDao.findList(a);
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return new Area();
        }
    }

    public static Object getByOfficeName2(String string) {
        // TODO Auto-generated method stub
        List<Office> list = officeDao.findListByName(string);
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return new Office();
        }
    }
    
    /**
     * @Title: getProject 
     * @Description: 获取项目ID
     * @param name
     * @return
     * @author com.mhout.xyb
     */
    private static SysProject getProject(String name){
    	return sysProjectDao.findByCode(name);
    }
    

}
