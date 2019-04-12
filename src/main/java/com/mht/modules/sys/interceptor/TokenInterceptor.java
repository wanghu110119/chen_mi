package com.mht.modules.sys.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.google.gson.Gson;
import com.mht.common.enumutils.ApiCodeEnum;
import com.mht.common.json.AjaxJson;

public class TokenInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			Object handler) throws Exception {
//		String clientTokens = httpServletRequest.getHeader("Authorization");
//		String sessionToken = (String) httpServletRequest.getSession().getAttribute("token");
//		if (clientTokens != null && sessionToken != null && clientTokens.equals(sessionToken)) {
//			return super.preHandle(httpServletRequest, httpServletResponse, handler);
//		} else {
//			String contentType = "application/json;charset=UTF-8";
//			AjaxJson ajaxJson = new AjaxJson();
//			ajaxJson.setCode(String.valueOf(ApiCodeEnum.AOPINVALIDAUTHTOKEN.getCode()));
//			ajaxJson.setSub_code(ApiCodeEnum.AOPINVALIDAPPAUTHTOKEN.getSub_code());
//			ajaxJson.setSub_msg(ApiCodeEnum.AOPINVALIDAPPAUTHTOKEN.getSub_msg());
//			ajaxJson.setMsg(ApiCodeEnum.AOPINVALIDAPPAUTHTOKEN.getMsg());
//			ajaxJson.setSuccess(false);
//			httpServletResponse.reset();
//			httpServletResponse.setContentType(contentType);
//			httpServletResponse.setCharacterEncoding("UTF-8");
//			httpServletResponse.getWriter().write(new Gson().toJson(ajaxJson));// 返回json对象
//			httpServletResponse.getWriter().flush();
//			httpServletResponse.getWriter().close();
//			// httpServletResponse.sendRedirect("/");
//			return false;
//		}
		return super.preHandle(httpServletRequest, httpServletResponse, handler);
	}

}