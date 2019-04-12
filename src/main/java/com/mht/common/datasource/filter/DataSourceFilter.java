package com.mht.common.datasource.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mht.common.datasource.DynamicDataSource;

public class DataSourceFilter implements Filter {

	private HttpServletRequest request;
	private HttpServletResponse response;
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		try{
			this.request = (HttpServletRequest)servletRequest;
			String[] url = request.getRequestURI().split("/");
			if(url.length>=4){
				String requestUrl = url[3];
				if("bdc".equals(requestUrl)){
					DynamicDataSource.setCurrentLookupKey(DynamicDataSource.DATA_SOURCE_BDC);
				}else if("sdep".equals(requestUrl)){
					DynamicDataSource.setCurrentLookupKey(DynamicDataSource.DATA_SOURCE_SDEP);
				}else if("portal".equals(requestUrl)){
					DynamicDataSource.setCurrentLookupKey(DynamicDataSource.DATA_SOURCE_PORTAL);
				}else if("bi".equals(requestUrl)){
					DynamicDataSource.setCurrentLookupKey(DynamicDataSource.DATA_SOURCE_BI);
				}else if("service".equals(requestUrl)){
					DynamicDataSource.setCurrentLookupKey(DynamicDataSource.DATA_SOURCE_SERVICE);
				}
			}
			filterChain.doFilter(servletRequest, servletResponse);
		}finally {
			//清除当前线程保存的数据源
			DynamicDataSource.clearDataSource();
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}

}
