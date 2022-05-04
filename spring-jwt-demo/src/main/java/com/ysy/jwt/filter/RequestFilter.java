package com.ysy.jwt.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;


public class RequestFilter implements javax.servlet.Filter{

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		System.out.println("RequestFilter dofilter 진입");
		RereadableRequestWrapper rereadableRequestWrapper 
								= new RereadableRequestWrapper((HttpServletRequest)request);
        
		chain.doFilter(rereadableRequestWrapper, response);
	}

}