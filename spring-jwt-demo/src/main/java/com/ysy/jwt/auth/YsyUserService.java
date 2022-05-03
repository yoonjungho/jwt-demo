package com.ysy.jwt.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class YsyUserService {

	@Autowired
	private YsyUserRepository ysyUserRepository;
	
	
	public String userRegister(YsyUser ysyUser ) {
		
		YsyUser tmp = ysyUserRepository.findByuserId(ysyUser.getUserId());
		if(tmp == null) {
			ysyUserRepository.save(ysyUser);
			return "ok";
		}else {
			return "user가 존재합니다.";
		}
	}
}
