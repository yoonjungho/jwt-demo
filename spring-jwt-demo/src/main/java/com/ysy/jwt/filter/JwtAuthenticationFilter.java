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

/**
 * @author clubbboy@naver.com
 *  2022. 5. 5.
 *  Desc : spring boot login시 PrincipalDetailsService의 서비스가 자동으로 실행됨 
 *         그럼 해당클래스의 loadUserByUsername 함수가 실행됨.
 *         그러나 formLogin을 안쓴다고 해놔서 로그인시 해당 메서드를 강제로 실행해 줘야함.
 *         jwt에 UsernamePasswordAuthenticationFilter 필턱가 /login 으로 호출시  실행됨.
 *         그러면 attemptAuthentication() 함수 호출해서 이 함수에서 로그인 인증 진행하면 됨.
 *         로그인 인증이 완료 되면 successfulAuthentication() 함수를 호출해주므로 
 *         해당 지점에서 token생성 후 response -> header에 담아주면 끝
 *         
 *         ** 추가설명 
 *         security config에 해당 class filter로 등록한다.                                                                          
 *         UsernamePasswordAuthenticationFilter 필터는 로그인 진행하는 필터임.                                                  
 *         필터등록시 authenticationManager을 인자로 받아야함.                                                                  
 *         authenticationManager 객체는 WebSecurityConfigurerAdapter 클래스에 있어서 
 *         securityConfig에서 등록시 인자로 던질 수 있음.       
 *         @RequiredArgsConstructor이것을 사용하면 생성자를 만들어서 받을 필요 없음                                                                 
 *         authenticationManager로 로그인 시도를 하면 principalDetailService가 호출이됨. 
 *         즉 loadUserByUsername 해당 함수가 호출됨.        
 *         그러면 pricipalDetail을 세션에 담는데 이것은 권한관리를 위한것이기 때문에 권한관리를 안할꺼면 안담아도됨                                        
 *         
 *         ** 처리안된것
 *         - token만료시간 처리로 인해서 2개의 토근을 생성하고 하나는 인증용으로 만료시간을 짧게 생성
 *           하나는 토근 갱신용으로 인증시간을 길게 생성해서 클라인언트에서 요청시 토근 갱신해서 넘겨주면 댐. 
 */
@RequiredArgsConstructor//해당 클래스를 필터로 등록시 인자로 받기위해 어노테이션 선언
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter{

	
	private final AuthenticationManager authenticationManager;

//	public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
//		this.authenticationManager = authenticationManager;
//	}
	
	/*/login 요청시 실행되는 함수 */
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		
		YsyUtil.log("JwtAuthenticationFilter class -> attemptAuthentication() 진입");
		ObjectMapper om = new ObjectMapper();
		LoginReqData loginReqData = null;
		
		try {
			loginReqData = om.readValue(request.getInputStream() , LoginReqData.class);
		
		YsyUtil.log("logdata ===> "+loginReqData);
		
		UsernamePasswordAuthenticationToken authenticationToken = 
				new UsernamePasswordAuthenticationToken(
						loginReqData.getUsername(),
						loginReqData.getPassword()); 
		
		YsyUtil.log("token create  ===> "+ authenticationToken);
		
		//loadUserByUsername 함수 호출
		Authentication authentication = 
				authenticationManager.authenticate(authenticationToken);
		
		//권한 처리때문에 생성함. session에 등록됨. 등록 안하면 권한 처리 할 수 없음. 
		PrincipalDetails principalDetails = (PrincipalDetails)authentication.getPrincipal();
		
		YsyUtil.log("Authentication : "+principalDetails.getUser().getUsername());
		
		return authentication;
		} catch (IOException e) {
			System.out.println("123456");
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		
		YsyUtil.log("JwtAuthenticationFilter class --->  successfulAuthentication 진입 ");
		
		PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();
		
		String jwtToken = JWT.create()
				.withSubject("jwtToken")
				.withExpiresAt(new Date(System.currentTimeMillis()+JwtProperties.EXPIRATION_TIME))
				.withClaim("name"    , principalDetails.getUser().getName())
				.withClaim("username", principalDetails.getUser().getUsername())
				.sign(Algorithm.HMAC512(JwtProperties.SECRET));
		
		String jwtTokenRe = JWT.create()
				.withSubject("jwtToken")
				.withExpiresAt(new Date(System.currentTimeMillis()+JwtProperties.EXPIRATION_TIME_RE))
				.withClaim("name"    , principalDetails.getUser().getName())
				.withClaim("username", principalDetails.getUser().getUsername())
				.sign(Algorithm.HMAC512(JwtProperties.SECRET+"refresh"));
		
		
		response.addHeader(JwtProperties.HEADER_STRING  , JwtProperties.TOKEN_PREFIX+jwtToken);
		response.addHeader(JwtProperties.HEADER_REFRESH , JwtProperties.TOKEN_PREFIX+jwtTokenRe);
		
		System.out.println("header key ["+JwtProperties.HEADER_STRING+"] \njwt token = " + jwtToken);
	}
	
	

}
