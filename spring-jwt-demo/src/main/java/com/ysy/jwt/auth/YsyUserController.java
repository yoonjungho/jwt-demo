package com.ysy.jwt.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ysy.common.YsyUtil;

@RestController
@RequestMapping("/ysy/v1/auth")
public class YsyUserController {

	
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	@Autowired
	YsyUserService ysyUserService;
	
	@Autowired
	YsyUtil ysyUtil;
	
	@PostMapping("/userRegister")//회원가입. 무인증.
	@ResponseBody
	public String userRegister(@RequestBody YsyUser ysyUser ) {
		
		if( !ysyUtil.isNullAndEmpty(ysyUser.getUsername()) &&
		    !ysyUtil.isNullAndEmpty(ysyUser.getPassword())) 
		{
			
			if(ysyUserService.isUser(ysyUser.getUsername())) {
				return "user 존재";
			}
			ysyUser.setPassword(bCryptPasswordEncoder.encode(ysyUser.getPassword()));
			ysyUser.setRoles("ROLE_USER");//기본 룰 셋팅 , 이후 관리자 페이지에서 role 변경
			if(ysyUserService.userRegister(ysyUser )) {
				return "ok -> login page move!";
			}
			else return "user register error";
		}
		else {
			return "Id or password empty!";
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
