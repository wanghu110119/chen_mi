/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.mht.modules.sys.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cas.CasAuthenticationException;
import org.apache.shiro.cas.CasRealm;
import org.apache.shiro.cas.CasToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.TicketValidationException;
import org.jasig.cas.client.validation.TicketValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.mht.common.config.Global;
import com.mht.common.datasource.DynamicDataSource;
import com.mht.common.servlet.ValidateCodeServlet;
import com.mht.common.utils.CacheUtils;
import com.mht.common.utils.MobileUtils;
import com.mht.common.utils.SpringContextHolder;
import com.mht.common.utils.StringUtils;
import com.mht.common.web.Servlets;
import com.mht.modules.ident.entity.AutRecord;
import com.mht.modules.ident.service.AutRecordService;
import com.mht.modules.sys.entity.Log;
import com.mht.modules.sys.entity.Menu;
import com.mht.modules.sys.entity.Role;
import com.mht.modules.sys.entity.User;
import com.mht.modules.sys.security.SystemAuthorizingRealm.Principal;
import com.mht.modules.sys.service.SystemService;
import com.mht.modules.sys.utils.LogUtils;
import com.mht.modules.sys.utils.OauthLoginUtils;
import com.mht.modules.sys.utils.UserUtils;
import com.mht.modules.sys.web.LoginController;

/**
 * 系统安全认证实现类
 * 
 * @author jeeplus
 * @version 2014-7-5
 */
@Service
// @DependsOn({"userDao","roleDao","menuDao"})
public class SystemAuthorizingCasRealm extends CasRealm {

    @Autowired
    private SystemService systemService;

    @Autowired
    private AutRecordService autRecordService;

    @Autowired
    HttpServletRequest request;
    
    private CacheManager cacheManager;

    private final static Logger log = LoggerFactory.getLogger(SystemAuthorizingCasRealm.class);

    public SystemAuthorizingCasRealm() {
        super();
        setCacheManager(cacheManager);
    }

    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
    	//判断用户是否来自手机端登录
		HttpServletRequest httpServletRequest = (HttpServletRequest)request;  
		if (MobileUtils.judgeIsMoblie(httpServletRequest)
			&& OauthLoginUtils.existCode(httpServletRequest)){
			return mobileLogin(token, request);
		} else {
			return casLogin(token);
		}
    }
    
    /**
     * 
     * @Title: mobileLogin 
     * @Description: 手机端登录
     * @param token
     * @return
     * @author com.mhout.xyb
     */
    public AuthenticationInfo mobileLogin(AuthenticationToken authcToken, HttpServletRequest request) {
    	WebUtils.getAndClearSavedRequest(request);
//    	WebUtils.getCleanParam(request, "username");
        // 校验用户名密码
        User user = getSystemService().getUserByLoginName(WebUtils.getCleanParam(request, "username"));
        if (user != null) {
            if (Global.NO.equals(user.getLoginFlag())){
                throw new AuthenticationException("msg:该已帐号禁止登录.");
            }
            PrincipalCollection principalCollection = new SimplePrincipalCollection(new Principal(user, false),
                    getName());
            return new SimpleAuthenticationInfo(principalCollection, "admin");
        } else {
            return null;
        }
    }
    
    /**
     * 
     * @Title: casLogin 
     * @Description: cas登录
     * @param token
     * @return
     * @author com.mhout.xyb
     */
    public AuthenticationInfo casLogin(AuthenticationToken token) {
    	 WebUtils.getAndClearSavedRequest(request); //流程需要把这里注释掉
         CasToken casToken = (CasToken) token;
         if (token == null)
             return null;
         String ticket = (String) casToken.getCredentials();
         TicketValidator ticketValidator = ensureTicketValidator();
         try {
             Assertion casAssertion = ticketValidator.validate(ticket, getCasService());
             AttributePrincipal casPrincipal = casAssertion.getPrincipal();
             String userId = casPrincipal.getName();
             log.debug("Validate ticket : {} in CAS server : {} to retrieve user : {}",
                     new Object[] { ticket, getCasServerUrlPrefix(), userId });
             Map attributes = casPrincipal.getAttributes();
             casToken.setUserId(userId);
             String rememberMeAttributeName = getRememberMeAttributeName();
             String rememberMeStringValue = (String) attributes.get(rememberMeAttributeName);
             boolean isRemembered = rememberMeStringValue != null && Boolean.parseBoolean(rememberMeStringValue);
             if (isRemembered)
                 casToken.setRememberMe(true);
             // List principals = CollectionUtils.asList(new Object[] { userId,
             // attributes });
             boolean mobile = WebUtils.isTrue(request, FormAuthenticationFilter.DEFAULT_MOBILE_PARAM);
             // User user = systemService.getUserByLoginName(userId);
             User user = systemService.getTypeUser(userId);
             PrincipalCollection principalCollection = new SimplePrincipalCollection(new Principal(user, mobile),
                     getName());

             // 这里可以拿到Cas的登录账号信息,加载到对应权限体系信息放到缓存中...
             Global.logoutmap.put(ticket, UserUtils.getSubject());
             return new SimpleAuthenticationInfo(principalCollection, ticket);
         } catch (TicketValidationException e) {
             throw new CasAuthenticationException(
                     (new StringBuilder()).append("Unable to validate ticket [").append(ticket).append("]").toString(),
                     e);
         }
    }

    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        // String userName= (String) getAvailablePrincipal(principals);
        // User user = getSystemService().getUserByLoginName(userName);
        // Principal principal =new Principal(user,false);
        // 获取到该方法前的数据源，等认证完毕后，再切换回去
        String oldDataSource = DynamicDataSource.getCurrentLookupKey();

        DynamicDataSource.setCurrentLookupKey(DynamicDataSource.DATA_SOURCE);
        Principal principal = (Principal) getAvailablePrincipal(principals);
        // 获取当前已登录的用户
//        if (!Global.TRUE.equals(Global.getConfig("user.multiAccountLogin"))) {
//            Collection<Session> sessions = getSystemService().getSessionDao().getActiveSessions(true, principal,
//                    UserUtils.getSession());
//            if (sessions.size() > 0) {
//                // 如果是登录进来的，则踢出已在线用户
//                if (UserUtils.getSubject().isAuthenticated()) {
//                    for (Session session : sessions) {
//                        getSystemService().getSessionDao().delete(session);
//                    }
//                }
//                // 记住我进来的，并且当前用户已登录，则退出当前用户提示信息。
//                else {
//                    UserUtils.getSubject().logout();
//                    throw new AuthenticationException("msg:账号已在其它地方登录，请重新登录。");
//                }
//            }
//        }
        User user = getSystemService().getUserByLoginName(principal.getLoginName());
        if (user != null) {
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
            List<Menu> list = UserUtils.getMenuList();
            for (Menu menu : list) {
                if (StringUtils.isNotBlank(menu.getPermission())) {
                    // 添加基于Permission的权限信息
                    for (String permission : StringUtils.split(menu.getPermission(), ",")) {
                        info.addStringPermission(permission);
                    }
                }
            }
            // 添加用户权限
            info.addStringPermission("user");
            // 添加用户角色信息
            for (Role role : user.getRoleList()) {
                info.addRole(role.getEnname());
            }
            // 更新登录IP和时间
            getSystemService().updateUserLoginInfo(user);
            // 记录登录日志
            LogUtils.saveLog(Servlets.getRequest(), Log.TYPE_LOGIN, "系统登录");
            // 如果原来的数据源不是基础数据源，则切换回之前的数据源
            if (StringUtils.isNotEmpty(oldDataSource) && !DynamicDataSource.DATA_SOURCE.equals(oldDataSource)) {
                DynamicDataSource.setCurrentLookupKey(oldDataSource);
            }
            return info;
        } else {
            return null;
        }
    }

    
    
    
    
    
    protected List split(String s) {
        List list = new ArrayList();
        String elements[] = StringUtils.split(s, ',');
        if (elements != null && elements.length > 0) {
            String arr$[] = elements;
            int len$ = arr$.length;
            for (int i$ = 0; i$ < len$; i$++) {
                String element = arr$[i$];
                // if (StringUtils.hasText(element))
                list.add(element.trim());
            }

        }
        return list;
    }

    protected void addRoles(SimpleAuthorizationInfo simpleAuthorizationInfo, List roles) {
        String role;
        for (Iterator i$ = roles.iterator(); i$.hasNext(); simpleAuthorizationInfo.addRole(role))
            role = (String) i$.next();

    }

    protected void addPermissions(SimpleAuthorizationInfo simpleAuthorizationInfo, List permissions) {
        String permission;
        for (Iterator i$ = permissions.iterator(); i$.hasNext(); simpleAuthorizationInfo
                .addStringPermission(permission))
            permission = (String) i$.next();

    }

    /*  *//** 重写退出时缓存处理方法 */

    /*
     * protected void doClearCache(PrincipalCollection principals) { Object
     * principal = principals.getPrimaryPrincipal(); try {
     * getCache().remove(principal); log.debug(new
     * StringBuffer().append(principal).
     * append(" on logout to remove the cache [").append(principal)
     * .append("]").toString()); } catch (CacheException e) {
     * log.error(e.getMessage()); } }
     * 
     *//** 获取缓存管理器的缓存堆实例 *//*
                           * protected Cache<Object, Object> getCache() throws
                           * CacheException { return cacheManager.g }
                           */

    public CacheManager getCacheManager() {
        return cacheManager;
    }

    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    /**
     * 获取系统业务对象
     */
    public SystemService getSystemService() {
        if (systemService == null) {
            systemService = SpringContextHolder.getBean(SystemService.class);
        }
        return systemService;
    }

    /**
     * @Title: saveAuth
     * @Description: 保存认证信息
     * @author com.mhout.xyb
     */
    public void saveAuth(String isSuccess) {
        AutRecord autRecord = new AutRecord();
        autRecord.setUser(UserUtils.getUser());
        autRecord.setIp(StringUtils.getRemoteAddr(request));
        autRecord.setIsSuccess(isSuccess);
        autRecordService.save(autRecord);
    }

    // /**
    // * 授权用户信息
    // */
    // public static class Principal implements Serializable {
    //
    // private static final long serialVersionUID = 1L;
    //
    // private String id; // 编号
    // private String loginName; // 登录名
    // private String name; // 姓名
    // private boolean mobileLogin; // 是否手机登录
    //
    // // private Map<String, Object> cacheMap;
    //
    // public Principal(User user, boolean mobileLogin) {
    // this.id = user.getId();
    // this.loginName = user.getLoginName();
    // this.name = user.getName();
    // this.mobileLogin = mobileLogin;
    // }
    //
    // public String getId() {
    // return id;
    // }
    //
    // public String getLoginName() {
    // return loginName;
    // }
    //
    // public String getName() {
    // return name;
    // }
    //
    // public boolean isMobileLogin() {
    // return mobileLogin;
    // }
    //
    // // @JsonIgnore
    // // public Map<String, Object> getCacheMap() {
    // // if (cacheMap==null){
    // // cacheMap = new HashMap<String, Object>();
    // // }
    // // return cacheMap;
    // // }
    //
    // /**
    // * 获取SESSIONID
    // */
    // public String getSessionid() {
    // try {
    // return (String) UserUtils.getSession().getId();
    // } catch (Exception e) {
    // return "";
    // }
    // }
    //
    // @Override
    // public String toString() {
    // return id;
    // }
    //
    // }
}
