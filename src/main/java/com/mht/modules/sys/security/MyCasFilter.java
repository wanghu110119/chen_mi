package com.mht.modules.sys.security;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.cas.CasFilter;
import org.apache.shiro.cas.CasToken;

import com.mht.common.utils.MobileUtils;
import com.mht.common.utils.StringUtils;
import com.mht.modules.sys.utils.OauthLoginUtils;

/**
 * 
 * @ClassName: MyCasFilter
 * @Description: 自定义cas过滤
 * @author com.mhout.xyb
 * @date 2018年5月3日 上午10:29:17 
 * @version 1.0.0
 */
public class MyCasFilter extends CasFilter{
	// the name of the parameter service ticket in url
    private static final String TICKET_PARAMETER = "ticket";
    
    
	protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) throws Exception {
		//手机端登录
		HttpServletRequest httpServletRequest = (HttpServletRequest)request;
		if (MobileUtils.judgeIsMoblie(httpServletRequest)
			&& OauthLoginUtils.existCode(httpServletRequest)) {
			return new CasToken("admin");
		} else {
			HttpServletRequest httpRequest = (HttpServletRequest) request;
	        String ticket = httpRequest.getParameter(TICKET_PARAMETER);
	        return new CasToken(ticket);
		}
    }
	
	 protected void saveRequestAndRedirectToLogin(ServletRequest request, ServletResponse response) throws IOException {
	        saveRequest(request);
	        redirectToLogin(request, response);
	    }

}
