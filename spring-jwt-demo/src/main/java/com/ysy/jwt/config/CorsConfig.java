package com.ysy.jwt.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @author clubbboy@naver.com
 *  2022. 5. 5.
 *  Desc : cors란 cross origin resources sharing -> 다른 출처의 리소스 공유하는 방법
 *  
 *  해당 서버에서 허용할 수 있는 범위를 잡아서 jwt필터에 등록해줌.
 *  
 */
@Configuration
public class CorsConfig {

	@Bean
	public CorsFilter corsFilter() {
		// import org.springframework.web.cors.UrlBasedCorsConfigurationSource; 중요
		UrlBasedCorsConfigurationSource source  = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);////내서버가 응답을 할 때 json을 자바스크립트에서 처리할 수 있게 할지 설정하는것 false면 자바스크립트에서 요청 안됨
		config.addAllowedOriginPattern("http://localhost:8080");// ip의 응답 허용 , 모든 ip :*
		config.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT"));
		config.addAllowedHeader("*");// 모든 header의 응답 허용
		config.addAllowedMethod("*");// 모든 요청 허용 (post , get , put ,fatch)
		
		source.registerCorsConfiguration("/**", config); 
		return new CorsFilter(source);
	}
	
	
}
