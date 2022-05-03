package com.ysy.jwt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

	@Bean
	public CorsFilter corsFilter() {
		// import org.springframework.web.cors.UrlBasedCorsConfigurationSource; 중요
		UrlBasedCorsConfigurationSource source  = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);////내서버가 응답을 할 때 json을 자바스크립트에서 처리할 수 있게 할지 설정하는것 false면 자바스크립트에서 요청 안됨
		config.addAllowedOrigin("*");// 모든 ip의 응답 허용
		config.addAllowedHeader("*");// 모든 header의 응답 허용
		config.addAllowedMethod("*");// 모든 요청 허용 (post , get , put ,fatch)
		
		source.registerCorsConfiguration("/ysy/**", config);
		
		return new CorsFilter(source);
	}
}
