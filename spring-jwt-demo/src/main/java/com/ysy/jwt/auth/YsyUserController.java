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
		
		if( !ysyUtil.isNullAndEmpty(ysyUser.getUserId()) &&
		    !ysyUtil.isNullAndEmpty(ysyUser.getPwd())) 
		{
			ysyUser.setPwd(bCryptPasswordEncoder.encode(ysyUser.getPwd()));
			ysyUser.setRoles("ROLE_USER");
			if(ysyUserService.userRegister(ysyUser ) !=null) {
				return "ok";
			}
			else return "user register error";
		}
		else {
			return "Id or password empty!";
		}
	}
	
	
	
	
	@GetMapping("/test")
	public String test() {
		
		return "권한없는 test page ";
	}
}
