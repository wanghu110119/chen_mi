package com.mht.modules.sys.security;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.subject.WebSubject;
import org.jasig.cas.client.session.SessionMappingStorage;
import org.jasig.cas.client.session.SingleSignOutHandler;
import org.jasig.cas.client.util.AbstractConfigurationFilter;
import org.jasig.cas.client.util.CommonUtils;
import org.jasig.cas.client.util.XmlUtils;

import com.mht.modules.sys.security.SystemAuthorizingRealm.Principal;
import com.mht.modules.sys.utils.UserUtils;

public class MySingleSignOutFilter extends AbstractConfigurationFilter{

    private static final SingleSignOutHandler handler = new SingleSignOutHandler();

    public void init(final FilterConfig filterConfig) throws ServletException {
        if (!isIgnoreInitConfiguration()) {
            handler.setArtifactParameterName(getPropertyFromInitParams(filterConfig, "artifactParameterName", "ticket"));
            handler.setLogoutParameterName(getPropertyFromInitParams(filterConfig, "logoutParameterName", "logoutRequest"));
        }
        handler.init();
    }

    public void setArtifactParameterName(final String name) {
        handler.setArtifactParameterName(name);
    }
    
    public void setLogoutParameterName(final String name) {
        handler.setLogoutParameterName(name);
    }

    public void setSessionMappingStorage(final SessionMappingStorage storage) {
        handler.setSessionMappingStorage(storage);
    }
    
    public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) servletRequest;

        if (handler.isTokenRequest(request)) {
            handler.recordSession(request);
        } else if (handler.isLogoutRequest(request)) {
        	final String logoutMessage = CommonUtils.safeGetParameter(request, "logoutRequest");
            final String token = XmlUtils.getTextForElement(logoutMessage, "SessionIndex");
            if (CommonUtils.isNotBlank(token)) {
                HttpSession session = null;
                try {
                    Field msField = handler.getSessionMappingStorage().getClass().getDeclaredField("MANAGED_SESSIONS");
                    msField.setAccessible(true);
                    Map<String,HttpSession> MANAGED_SESSIONS = (Map)msField.get(handler.getSessionMappingStorage());
                    session = MANAGED_SESSIONS.get(token);
                } catch (Exception e) {
                }

                if (session != null) {
                    Subject subject = UserUtils.getSubject();
                    Principal shiroUser = (Principal)(((SimplePrincipalCollection)(session.getAttribute("org.apache.shiro.subject.support.DefaultSubjectContext_PRINCIPALS_SESSION_KEY"))).getPrimaryPrincipal());
                    SimplePrincipalCollection pc = new SimplePrincipalCollection(shiroUser, shiroUser.getName());
                    try {
                        Field principalsField = subject.getClass().getSuperclass().getDeclaredField("principals");
                        principalsField.setAccessible(true);
                        principalsField.set(subject, pc);
                    } catch (Exception e) {
                    }


                    try {
                        subject.logout();
                    } catch (SessionException ise) {
                    }

                }
            }
            handler.destroySession(request);
            // Do not continue up filter chain
            return;
        } else {
            log.trace("Ignoring URI " + request.getRequestURI());
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    public void destroy() {
        // nothing to do
    }
    
    protected static SingleSignOutHandler getSingleSignOutHandler() {
        return handler;
    }
}
