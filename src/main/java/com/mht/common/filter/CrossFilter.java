package com.mht.common.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class CrossFilter extends HandlerInterceptorAdapter {

		@Override
		public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
				throws Exception {
			response.addHeader("Access-Control-Allow-Origin", "*");
			response.addHeader("Access-Control-Allow-Methods", "*");
			response.addHeader("Access-Control-Max-Age", "864000");
			response.addHeader("Access-Control-Allow-Headers", "*");
			response.addHeader("Access-Control-Allow-Credentials", "true");
			return super.preHandle(request, response, handler);
		}
}
