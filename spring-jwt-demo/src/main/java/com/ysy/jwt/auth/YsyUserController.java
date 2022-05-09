package com.ysy.jwt.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ysy.common.YsyUtil;

/**
 * @author clubbboy@naver.com
 *  2022. 5. 1
 *  Desc : 회원가입관련 컨트롤러
 *         회원가입시 default로 관리되어야할 것들 셋팅.
 *         회원가입시 Role은 default로 USER_ROLE로 셋팅
 */
@RestController
@CrossOrigin
@RequestMapping("/ysy/v1/auth")
public class YsyUserController {

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	@Autowired
	YsyUserService ysyUserService;
	
	
	@PostMapping("/signUp")//회원가입. 무인증.
	@ResponseBody
	public String signUp(@RequestBody YsyUser ysyUser ) {
		
		if( !YsyUtil.isNullAndEmpty(ysyUser.getUsername()) &&
		    !YsyUtil.isNullAndEmpty(ysyUser.getPassword())) 
		{
			
			if(ysyUserService.isUser(ysyUser.getUsername())) {
				return "error : user 존재";
			}
			ysyUser.setPassword(bCryptPasswordEncoder.encode(ysyUser.getPassword()));
			ysyUser.setRoles("ROLE_USER");//기본 룰 셋팅 , 이후 관리자 페이지에서 role 변경
			if(ysyUserService.signUp(ysyUser )) {
				return "ok -> login page move!";
			}
			else return "error : user register error!";
		}
		else {
			return "error : Id or password empty!";
		}
	}
	
	@GetMapping("/home")
	@ResponseBody
	public String home() {
		
		return "home 진입";
	}
	
	
	
	
	@GetMapping("/test")
	public String test() {
		
		return "권한없는 test page ";
	}
}
