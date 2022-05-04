package com.ysy.jwt.filter;

import java.io.IOException;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ysy.common.YsyUtil;
import com.ysy.jwt.auth.LoginReqData;
import com.ysy.jwt.auth.PrincipalDetails;

import lombok.RequiredArgsConstructor;

/* create by yjh 
 * 2022 05 03
 *  /login 인증 요청시 실행되는 함수.
 *  
 *  
 *  formlogin 을 사용하지 않기 때문에 PrincipalDetailsService의 loadUserByUsername 함수를 호출하기 위해                         
 *  해당 클래스를 만들고 UsernamePasswordAuthenticationFilter를 상속받아서                                                 
 *  security config에 filter로 등록한다.                                                                          
 *  UsernamePasswordAuthenticationFilter 필터는 로그인 진행하는 필터임.                                                  
 *  필터등록시 authenticationManager을 인자로 받아야함.                                                                  
 *  authenticationManager 객체는 WebSecurityConfigurerAdapter 클래스에 있어서 securityConfig에서 등록시 인자로 던질 수 있음.       
 *  @RequiredArgsConstructor이것을 꼭 사용해야 함.,                                                                  
 *  아니면 생성자를 생성해서 받아줘도 됨.                                                                                   
 *  authenticationManager로 로그인 시도를 하면 principalDetailService가 호출이됨. 즉 loadUserByUsername 해당 함수가 호출됨.        
 *  그러면 pricipalDetail을 세션에 담는데 이것은 권한관리를 위한것이기 때문에 권한관리를 안할꺼면 안담아도됨                                        
 *  그런 후 jwt토근을 만들어서 응답해줌.,                                                                                 
 *  
 * */

@RequiredArgsConstructor//해당 클래스를 필터로 등록시 인자로 받기위해 어노테이션 선언
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter{

	
	private final AuthenticationManager authenticationManager;

//	public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
//		this.authenticationManager = authenticationManager;
//	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		
		YsyUtil.log("JwtAuthenticationFilter class -> attemptAuthentication() 진입");
		ObjectMapper om = new ObjectMapper();
		LoginReqData loginReqData = null;
		
		try {
			loginReqData = om.readValue(request.getInputStream() , LoginReqData.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		YsyUtil.log("logdata ===> "+loginReqData);
		
		// 유저네임패스워드 jwt 토큰 생성
		UsernamePasswordAuthenticationToken authenticationToken = 
				new UsernamePasswordAuthenticationToken(
						loginReqData.getUsername(),
						loginReqData.getPassword());
		
		YsyUtil.log("token create  ===> "+ authenticationToken);
		
		Authentication authentication = 
				authenticationManager.authenticate(authenticationToken);
		
		PrincipalDetails principalDetails = (PrincipalDetails)authentication.getPrincipal();
		
		YsyUtil.log("Authentication : "+principalDetails.getUser().getUsername());
		
		return authentication;
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		
		YsyUtil.log("JwtAuthenticationFilter class --->  successfulAuthentication 진입 ");
		
		PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();
		
		String jwtToken = JWT.create()
				.withSubject(principalDetails.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis()+JwtProperties.EXPIRATION_TIME))
				.withClaim("id"      , principalDetails.getUser().getId())
				.withClaim("username", principalDetails.getUser().getUsername())
				.sign(Algorithm.HMAC512(JwtProperties.SECRET));
		
		
		response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX+jwtToken);
	}
	
	

}
