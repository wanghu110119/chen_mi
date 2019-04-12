package com.mht.modules.sys.security;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.web.filter.authc.UserFilter;
import org.apache.shiro.web.util.WebUtils;

import com.mht.common.utils.MobileUtils;
import com.mht.modules.sys.utils.OauthLoginUtils;

/**
 * 
 * @ClassName: MobileFilter
 * @Description: 手机端过滤
 * @author com.mhout.xyb
 * @date 2018年5月3日 下午2:18:37 
 * @version 1.0.0
 */
public class MhtUserFilter extends UserFilter {
	
	public static final String DEFAULT_REDIRECT_URL = "/";
	
	private String redirectUrl = DEFAULT_REDIRECT_URL;

	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		//手机端登录
		HttpServletRequest httpServletRequest = (HttpServletRequest)request;
		if (MobileUtils.judgeIsMoblie(httpServletRequest)
			&& OauthLoginUtils.existCode(httpServletRequest)) {
			saveRequest(request);
	        WebUtils.issueRedirect(request, response, OauthLoginUtils.resetRedirectUrl(redirectUrl, httpServletRequest));
		} else {
			saveRequestAndRedirectToLogin(request, response);
		}
        return false;
    }
	
	public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }
	
	
}
