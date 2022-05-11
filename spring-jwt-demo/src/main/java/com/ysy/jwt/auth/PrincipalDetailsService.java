package com.ysy.jwt.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ysy.common.YsyUtil;

import lombok.RequiredArgsConstructor;

/**
 * @author clubbboy@naver.com
 *  2022. 5. 3.
 *  Desc : 시큐리티 설정에서 formLogin.loginProcessingUrl("/login") 요청이 오면 
 *         자동으로 userDetailsService의 loalUserByUsername 함수 실행
 *         그러나 jwt 사용시 formLogin.disable() 시켜놓았기 때문에 
 *         UsernamePasswordAuthenticationFilter 클래스를 상속받아 강재로 authorization을 만들어주면
 *         로그인시 해당함수 자동으로 실행되게 할 수 있음.
 *         
 *         username을 바꾸고 싶으면 securityConfig에서 usernameParameter("바꾸고싶은 id를 넣어줘야함") 
 *         그러나 formLogin.disable()을 했기때문에 어케 할지 아직 모름.
 *         그래서 repogitory에 함수 생성할때 findByUser_id(String user_id) 이렇게 했는데 
 *         이렇게 하면 spring가 Invalid derived query 발생시켜
 *         Window - Preference - Spring - Validation - Data Validator - 
 *         Invalid Derived Query [check off 체크해제] 해줌
 */
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService{

	@Autowired
	private YsyUserRepository ysyUserRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		YsyUtil.log("PrincipalDetailsService : 진입");
		
		YsyUser user = ysyUserRepository.findByUsername(username);
		
		return new PrincipalDetails(user);
	}

}
