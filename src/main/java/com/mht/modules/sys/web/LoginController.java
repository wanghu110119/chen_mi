/**
 * Copyright &copy; 2015-2020 <a href="http://www.mht.org/">mht</a> All rights reserved.
 */
package com.mht.modules.sys.web;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;
import com.mht.common.config.Global;
import com.mht.common.json.AjaxJson;
import com.mht.common.security.shiro.session.SessionDAO;
import com.mht.common.servlet.ValidateCodeServlet;
import com.mht.common.utils.CacheUtils;
import com.mht.common.utils.CookieUtils;
import com.mht.common.utils.IdGen;
import com.mht.common.utils.SpringContextHolder;
import com.mht.common.utils.StringUtils;
import com.mht.common.web.BaseController;
import com.mht.common.web.Servlets;
import com.mht.modules.swust.entity.SysPhotolist;
import com.mht.modules.swust.service.impl.OrderUserServiceImpl;
import com.mht.modules.swust.web.SystemSettingsController;
import com.mht.modules.sys.entity.Log;
import com.mht.modules.sys.entity.Menu;
import com.mht.modules.sys.entity.User;
import com.mht.modules.sys.security.FormAuthenticationFilter;
import com.mht.modules.sys.security.SystemAuthorizingRealm.Principal;
import com.mht.modules.sys.service.SysConfigService;
import com.mht.modules.sys.service.SystemService;
import com.mht.modules.sys.utils.CasHttpClientUtils;
import com.mht.modules.sys.utils.LogUtils;
import com.mht.modules.sys.utils.UserUtils;

/**
 * 登录Controller
 * @author mht
 * @version 2013-5-31
 */
@Controller
public class LoginController extends BaseController{
	
	   @Autowired
	    private SystemService systemService;
	
	@Autowired
	private SessionDAO sessionDAO;
	
	@Autowired
	private SysConfigService sysConfigService;
	
	@Autowired
	OrderUserServiceImpl service;
	
	@Autowired
	SystemSettingsController sysController;
	/**
	 * 管理登录
	 * @throws IOException 
	 */
	@RequestMapping(value = "${adminPath}/login", method = RequestMethod.GET)
	public String login(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
		Principal principal = UserUtils.getPrincipal();
//		// 默认页签模式
//		String tabmode = CookieUtils.getCookie(request, "tabmode");
//		if (tabmode == null){
//			CookieUtils.setCookie(response, "tabmode", "1");
//		}
		
		if (logger.isDebugEnabled()){
			logger.debug("login, active session size: {}", sessionDAO.getActiveSessions(false).size());
		}
		
		// 如果已登录，再次访问主页，则退出原账号。
		if (Global.TRUE.equals(Global.getConfig("notAllowRefreshIndex"))){
			CookieUtils.setCookie(response, "LOGINED", "false");
		}
		
		// 如果已经登录，则跳转到管理首页
		if(principal != null && !principal.isMobileLogin()){
			/*return "redirect:" + adminPath;*/
			return "redirect:" + adminPath + "/swust/order";
		}
		
		
		 SavedRequest savedRequest = WebUtils.getSavedRequest(request);//获取跳转到login之前的URL
		// 如果是手机没有登录跳转到到login，则返回JSON字符串
		 if(savedRequest != null){
			 String queryStr = savedRequest.getQueryString();
			if(	queryStr!=null &&( queryStr.contains("__ajax") || queryStr.contains("mobileLogin"))){
				AjaxJson j = new AjaxJson();
				j.setSuccess(false);
				j.setCode("0");
				j.setMsg("没有登录!");
				return renderString(response, j);
			}
		 }
		 request.setAttribute("ulogo", sysConfigService.getConfigValue(Global.ULOGO));
		 request.setAttribute("dns", sysConfigService.getConfigValue(Global.CDNS));
		 /*return "modules/sys/sysLogin";*/
		 
//		 return "modules/swust/userLogin";
		 SysPhotolist photo = service.initializationLogin();
			model.addAttribute("photo", photo);
			return "modules/swust/userLogin";
	}

	/**
	 * 登录失败，真正登录的POST请求由Filter完成
	 */
	@RequestMapping(value = "${adminPath}/login", method = RequestMethod.POST)
	public String loginFail(HttpServletRequest request, HttpServletResponse response, Model model) {
		Principal principal = UserUtils.getPrincipal();
		
		// 如果已经登录，则跳转到管理首页
		if(principal != null){
			return "redirect:" + adminPath;
		}

		String username = WebUtils.getCleanParam(request, FormAuthenticationFilter.DEFAULT_USERNAME_PARAM);
		boolean rememberMe = WebUtils.isTrue(request, FormAuthenticationFilter.DEFAULT_REMEMBER_ME_PARAM);
		boolean mobile = WebUtils.isTrue(request, FormAuthenticationFilter.DEFAULT_MOBILE_PARAM);
		String exception = (String)request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
		String message = (String)request.getAttribute(FormAuthenticationFilter.DEFAULT_MESSAGE_PARAM);
		
		if (StringUtils.isBlank(message) || StringUtils.equals(message, "null")){
			message = "用户或密码错误, 请重试.";
		}

		model.addAttribute(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM, username);
		model.addAttribute(FormAuthenticationFilter.DEFAULT_REMEMBER_ME_PARAM, rememberMe);
		model.addAttribute(FormAuthenticationFilter.DEFAULT_MOBILE_PARAM, mobile);
		model.addAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME, exception);
		model.addAttribute(FormAuthenticationFilter.DEFAULT_MESSAGE_PARAM, message);
		
		if (logger.isDebugEnabled()){
			logger.debug("login fail, active session size: {}, message: {}, exception: {}", 
					sessionDAO.getActiveSessions(false).size(), message, exception);
		}
		
		// 非授权异常，登录失败，验证码加1。
		if (!UnauthorizedException.class.getName().equals(exception)){
			model.addAttribute("isValidateCodeLogin", isValidateCodeLogin(username, true, false));
		}
		
		// 验证失败清空验证码
		request.getSession().setAttribute(ValidateCodeServlet.VALIDATE_CODE, IdGen.uuid());
		
		// 如果是手机登录，则返回JSON字符串
		if (mobile){
			AjaxJson j = new AjaxJson();
			j.setSuccess(false);
			j.setMsg(message);
			j.put("username", username);
			j.put("name","");
			j.put("mobileLogin", mobile);
			j.put("JSESSIONID", "");
	        return renderString(response, j.getJsonStr());
		}
		request.setAttribute("errorMsg", "用户名或密码错误");
		SysPhotolist photo = service.initializationLogin();
		model.addAttribute("photo", photo);
//		return "modules/sys/sysLogin";
		return "modules/swust/userLogin";
	}

	/**
	 * 管理登出
	 * @throws IOException 
	 */
	@RequestMapping(value = "${adminPath}/logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
		Principal principal = UserUtils.getPrincipal();
		// 如果已经登录，则跳转到管理首页
		if(principal != null){
			LogUtils.saveLog(Servlets.getRequest(), Log.TYPE_LOGOUT, "系统登出");
			UserUtils.getSubject().logout();
		}
	   // 如果是手机客户端退出跳转到login，则返回JSON字符串
			String ajax = request.getParameter("__ajax");
			if(	ajax!=null){
				model.addAttribute("success", "1");
				model.addAttribute("msg", "退出成功");
				return renderString(response, model);
			}
//		 return "redirect:" + adminPath+"/login";
			/**
			 * 手机重定向
			 */
			return "redirect:/api/swust/meeting/index";
	}

	/**
	 * 后台管理端跳转到门户
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "${adminPath}/backHomePage")
	public String backHomePage(Model model){
		return "modules/sys/portalPage";
	}
	/**
	 * 登录成功，进入管理首页
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "${adminPath}")
	public String index(HttpServletRequest request, HttpServletResponse response) {
		Principal principal = UserUtils.getPrincipal();
		// 登录成功后，验证码计算器清零
		isValidateCodeLogin(principal.getLoginName(), false, true);
		
		if (logger.isDebugEnabled()){
			logger.debug("show index, active session size: {}", sessionDAO.getActiveSessions(false).size());
		}
		
		// 如果已登录，再次访问主页，则退出原账号。
		if (Global.TRUE.equals(Global.getConfig("notAllowRefreshIndex"))){
			String logined = CookieUtils.getCookie(request, "LOGINED");
			if (StringUtils.isBlank(logined) || "false".equals(logined)){
				CookieUtils.setCookie(response, "LOGINED", "true");
			}else if (StringUtils.equals(logined, "true")){
				LogUtils.saveLog(Servlets.getRequest(), Log.TYPE_LOGOUT, "系统登出");
				UserUtils.getSubject().logout();
				return "redirect:" + adminPath + "/login";
			}
		}
		
		// 如果是手机登录，则返回JSON字符串
		if (principal.isMobileLogin()){
			if (request.getParameter("login") != null){
				return renderString(response, principal);
			}
			if (request.getParameter("index") != null){
				return "modules/sys/sysIndex";
			}
			return "redirect:" + adminPath + "/login";
		}
		String indexStyle = "default";
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			if (cookie == null || StringUtils.isEmpty(cookie.getName())) {
				continue;
			}
			if (cookie.getName().equalsIgnoreCase("theme")) {
				indexStyle = cookie.getValue();
			}
		}
		//系统显示配置
		request.setAttribute("dns", sysConfigService.getConfigValue(Global.CDNS));
		request.setAttribute("cname", sysConfigService.getConfigValue(Global.CNAME));
		request.setAttribute("uname", sysConfigService.getConfigValue(Global.UNAME));
		request.setAttribute("ulogo", sysConfigService.getConfigValue(Global.ULOGO));
		// 要添加自己的风格，复制下面三行即可
		User user = UserUtils.getUser();
		
		List<SysPhotolist> headPhoto = service.initHeadPhoto("1", user.getId());
		if(headPhoto.size()==0){
			headPhoto.add(new SysPhotolist());
		}
		request.setAttribute("headPhoto", headPhoto.get(0));
		if( sysController.getInSystem(user)){
			
			
		if(user.getRoleEnames().contains(Global.ROLESUPERSYSTEM)){
//			return "redirect:" + "/a/swust/appointment";
			return "modules/swust/backstageBooking";
		}else if(user.getRoleEnames().contains(Global.ROLESYSTEM)){
			return "modules/swust/backstageBooking";
		}else{
			return "modules/swust/sysOrder";
		}
		}else{
			return "modules/swust/error";
		}
	}
	
	/**
	 * 获取主题方案
	 */
	@RequestMapping(value = "/theme/{theme}")
	public String getThemeInCookie(@PathVariable String theme, HttpServletRequest request, HttpServletResponse response){
		if (StringUtils.isNotBlank(theme)){
			CookieUtils.setCookie(response, "theme", theme);
		}else{
			theme = CookieUtils.getCookie(request, "theme");
		}
		return "redirect:"+request.getParameter("url");
	}
	
	/**
	 * 是否是验证码登录
	 * @param useruame 用户名
	 * @param isFail 计数加1
	 * @param clean 计数清零
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static boolean isValidateCodeLogin(String useruame, boolean isFail, boolean clean){
		Map<String, Integer> loginFailMap = (Map<String, Integer>)CacheUtils.get("loginFailMap");
		if (loginFailMap==null){
			loginFailMap = Maps.newHashMap();
			CacheUtils.put("loginFailMap", loginFailMap);
		}
		Integer loginFailNum = loginFailMap.get(useruame);
		if (loginFailNum==null){
			loginFailNum = 0;
		}
		if (isFail){
			loginFailNum++;
			loginFailMap.put(useruame, loginFailNum);
		}
		if (clean){
			loginFailMap.remove(useruame);
		}
		return loginFailNum >= 3;
	}
	
	
	/**
	 * 首页
	 * @throws IOException 
	 */
	@RequestMapping(value = "${adminPath}/home")
	public String home(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
		User user = UserUtils.getUser();
		//判断用户角色信息
		return "redirect:" + adminPath + "/identity/general";
//		return "modules/sys/sysHome";
		
	}
	
	
	@RequestMapping(value = "mobile")
	public String mobileIndex(HttpServletRequest request, HttpServletResponse response,String userName) {
		 User user = getSystemService().getUserByLoginName(userName);
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
	        }
//	        PrincipalCollection principalCollection = new SimplePrincipalCollection(new Principal(user, 
//	        		WebUtils.isTrue(request, FormAuthenticationFilter.DEFAULT_MOBILE_PARAM)),
//                    "ww");
//
//            // 这里可以拿到Cas的登录账号信息,加载到对应权限体系信息放到缓存中...
//             new SimpleAuthenticationInfo(principalCollection, user.getName());
	        User shiroUser = (User)SecurityUtils.getSubject().getPrincipal();  
	        PrincipalCollection principalCollection = new SimplePrincipalCollection(new Principal(user, false),
                    "user"); 
	        //修改属性  
	        shiroUser=user;  
	        shiroUser.setId(user.getId());
	        String realmName = principalCollection.getRealmNames().iterator().next();  
	        PrincipalCollection newPrincipalCollection = new SimplePrincipalCollection(shiroUser, realmName);  
	        //重新加载Principal  
	        SecurityUtils.getSubject().runAs(newPrincipalCollection) ;
		if(user.getRoleEnames().contains(Global.ROLESUPERSYSTEM)){
//			return "redirect:" + "/a/swust/appointment";
			return "modules/swust/mobile/backstageBooking";
		}else if(user.getRoleEnames().contains(Global.ROLESYSTEM)){
			return "modules/swust/mobile/backstageBooking";
		}else{
			return "modules/swust/mobile/sysOrder";
		}
	}
	
	 public SystemService getSystemService() {
	        if (systemService == null) {
	            systemService = SpringContextHolder.getBean(SystemService.class);
	        }
	        return systemService;
	    }
	  @ResponseBody
	    @RequestMapping(value = "${adminPath}/mobile/login")
	    public AjaxJson loginTest(HttpServletRequest request, HttpServletResponse response,
	    		String username, String password, String remeberMe, Model model) {
	    	String useragent = request.getHeader("User-Agent");
	    	AjaxJson json = new AjaxJson();
	    	String loginUrl = CasHttpClientUtils.getLoginUrl();
	    	if (StringUtils.isNotBlank(loginUrl)) {
	    		try {
//	    			loginUrl="http://202.115.160.151:8060/cas/login";
					Map<String, String> map = CasHttpClientUtils.validate(username, password, remeberMe, loginUrl, useragent);
					if ("true".equals(map.get("success"))) {
						CookieUtils.setCookie(response, "TGC", map.get("TGC"));
						json.setMsg(map.get("location"));
						String ticktUrl = map.get("location");
						String ticket =StringUtils.substringAfter(ticktUrl, "ticket=");
						logger.info(ticket+"==========="+map.get("location")+"--------------------");
						CookieUtils.setCookie(response, "ticket", ticket);
						return json;
					}
	    		} catch (Exception e) {
					e.printStackTrace();
				}                
	    	}
	    	json.setSuccess(false);
	    	json.setMsg("登录失败！");
	    	return json;
	    }
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
}
