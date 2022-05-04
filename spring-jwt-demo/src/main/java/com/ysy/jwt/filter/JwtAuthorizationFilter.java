package com.ysy.jwt.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.ysy.common.YsyUtil;
import com.ysy.jwt.auth.PrincipalDetails;
import com.ysy.jwt.auth.YsyUser;
import com.ysy.jwt.auth.YsyUserRepository;
/**
 * 
 * @author yjh 2022 05 03
 *	
 * 로그인 후 token을 들고오면 인증된 user인지 확인
 *
 */
public class JwtAuthorizationFilter extends BasicAuthenticationFilter{

	
	private YsyUserRepository ysyUserRepository;
	
	
	public JwtAuthorizationFilter(AuthenticationManager authenticationManager, YsyUserRepository ysyUserRepository) {
		super(authenticationManager);
		YsyUtil.log("JwtAuthorizationFilter class JwtAuthorizationFilter 생성자 진입");
		this.ysyUserRepository = ysyUserRepository;
	}
	

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		YsyUtil.log("JwtAuthorizationFilter class doFilterInternal  진입");
		
		String header = request.getHeader(JwtProperties.HEADER_STRING);
//		printPostData(request);
		
		
		
		if(header == null || !header.startsWith(JwtProperties.TOKEN_PREFIX)) {//내가 보낸 해더인지 검사
			chain.doFilter(request, response);
			return;
		}
	
		
		YsyUtil.log("header : "+header);
		
		String token = request.getHeader(JwtProperties.HEADER_STRING).replace(JwtProperties.TOKEN_PREFIX, "");
		String username = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET))
				             .build()
				             .verify(token)
				             .getClaim("username")
				             .asString();
		
		if(username != null) {	
			YsyUser user = ysyUserRepository.findByUsername(username);
			
			// 인증은 토큰 검증시 끝. 인증을 하기 위해서가 아닌 스프링 시큐리티가 수행해주는 권한 처리를 위해 
			// 아래와 같이 토큰을 만들어서 Authentication 객체를 강제로 만들고 그걸 세션에 저장!
			PrincipalDetails principalDetails = new PrincipalDetails(user);
			Authentication authentication = new UsernamePasswordAuthenticationToken(
							                                    principalDetails, //나중에 컨트롤러에서 DI해서 쓸 때 사용하기 편함.
							                                    null, // 패스워드는 모르니까 null 처리, 어차피 지금 인증하는게 아니니까!!
							                                    principalDetails.getAuthorities());
			
			// 강제로 시큐리티의 세션에 접근하여 값 저장
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		
	}
	
	

}
