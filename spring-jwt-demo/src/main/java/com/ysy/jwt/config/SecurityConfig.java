package com.ysy.jwt.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

/** create by yjh 
 *  2022 05 01
 *  spring security를 사용하기 위한 구성파일 
 * */

@Configuration
@EnableWebSecurity // 시큐리티 활성화 -> 기본 스프링 필터체인에 등록
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	
	@Autowired
	CorsConfig corsConfig;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.csrf().disable()
		    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		    .and()
		    .formLogin().disable() // html form 로그인 사용 x
		    .httpBasic().disable() //
		    .addFilter(corsConfig.corsFilter())
		    .authorizeRequests()
			.antMatchers("/api/v1/user/**")
			.access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
			.antMatchers("/api/v1/manager/**")
				.access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
			.antMatchers("/api/v1/admin/**")
				.access("hasRole('ROLE_ADMIN')")
			.anyRequest().permitAll();
		;
	
	}

}
