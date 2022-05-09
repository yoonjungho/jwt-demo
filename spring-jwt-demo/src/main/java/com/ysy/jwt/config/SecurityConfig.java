package com.ysy.jwt.config;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import com.ysy.jwt.auth.YsyUserRepository;
import com.ysy.jwt.filter.JwtAuthenticationFilter;
import com.ysy.jwt.filter.JwtAuthorizationFilter;

/**
 * @author clubbboy@naver.com
 *  2022. 5. 1.
 *  Desc :spring security를 사용하기 위한 구성파일 
 */
@Configuration
@EnableWebSecurity // 시큐리티 활성화 -> 기본 스프링 필터체인에 등록
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	@Autowired
	private YsyUserRepository ysyUserRepository;
	@Autowired
	private CorsConfig corsConfig;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http
		    .csrf().disable()
//		    .cors()
//		    .and()
		    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)//현재 서버는 stateless(서버에 세션 상태가 없는것) 타입의 session 정책
		    .and()
		    .formLogin().disable() // html form 로그인 사용 x
		    .httpBasic().disable() //
		    
		    .addFilter(corsConfig.corsFilter())
//		    .addFilterBefore(new RequestFilter(), UsernamePasswordAuthenticationFilter.class)
		    .addFilter(new JwtAuthenticationFilter(authenticationManager()))
		    .addFilter(new JwtAuthorizationFilter(authenticationManager() ,ysyUserRepository ))
		    .cors().and()
		    .authorizeRequests()
			.antMatchers("/ysy/v1/account/**")
			.access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
			.antMatchers("/ysy/v1/manager/**")
				.access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
			.antMatchers("/ysy/v1/admin/**")
				.access("hasRole('ROLE_ADMIN')")
			.anyRequest().permitAll()
			;
	}
}
