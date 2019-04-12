package com.mht.modules.sys.security;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ctc.wstx.util.StringUtil;
import com.mht.common.utils.StringUtils;
import com.mht.common.web.Servlets;
import com.mht.modules.sys.entity.Log;
import com.mht.modules.sys.utils.LogUtils;

/**
 * @ClassName: MhtLogoutFilter
 * @Description: 自定义登出
 * @author com.mhout.xyb
 * @date 2017年8月2日 上午9:04:44 
 * @version 1.0.0
 */
public class MhtLogoutFilter extends LogoutFilter {
	
	private static final Logger log = LoggerFactory.getLogger(LogoutFilter.class);
	
	private String redirectPortalUrl = "/";

	@Override
	protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
		String type = request.getParameter("type");
		Subject subject = getSubject(request, response);
        String redirectUrl = getRedirectUrl(request, response, subject);
        LogUtils.saveLog(Servlets.getRequest(), Log.TYPE_LOGOUT, "系统登出");
        try {
            subject.logout();
        } catch (SessionException ise) {
            log.debug("Encountered session exception during logout.  This can generally safely be ignored.", ise);
        }
		if (StringUtils.isNotBlank(type) && "1".equals(type)) {
			redirectUrl = redirectPortalUrl;
			Cookie killMyCookie1 = new Cookie("TGC", null);
            killMyCookie1.setMaxAge(0);
            killMyCookie1.setPath("/");
            HttpServletResponse resp = (HttpServletResponse) response;
            resp.addCookie(killMyCookie1);
		}
		
        issueRedirect(request, response, redirectUrl);
        return false;
	}

	public String getRedirectPortalUrl() {
		return redirectPortalUrl;
	}

	public void setRedirectPortalUrl(String redirectPortalUrl) {
		this.redirectPortalUrl = redirectPortalUrl;
	}
	
	
	
   
}
