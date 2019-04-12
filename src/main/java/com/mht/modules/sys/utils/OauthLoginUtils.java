package com.mht.modules.sys.utils;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * @ClassName: OauthLoginUtils
 * @Description: oauth2.0登录
 * @author com.mhout.xyb
 * @date 2018年5月3日 下午2:47:31 
 * @version 1.0.0
 */
public class OauthLoginUtils {
	
	private static final String CODE = "code";
	
	/**
	 * 
	 * @Title: existCode 
	 * @Description: 判断code是否存在
	 * @param request
	 * @return
	 * @author com.mhout.xyb
	 */
	public static boolean existCode(HttpServletRequest request) {
		String code = request.getParameter(CODE);
		return StringUtils.isNotBlank(code) ? true : false;
	}
	
	/**
	 * 
	 * @Title: resetRedirectUrl 
	 * @Description: 重置跳转地址
	 * @param redirectUrl
	 * @param request
	 * @return
	 * @author com.mhout.xyb
	 */
	public static String resetRedirectUrl(String redirectUrl, HttpServletRequest request) {
		String code = request.getParameter(CODE);
		return redirectUrl + "?" + CODE + "=" + code;
	}

}
