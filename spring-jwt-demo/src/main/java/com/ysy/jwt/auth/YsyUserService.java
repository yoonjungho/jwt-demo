package com.ysy.jwt.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class YsyUserService {

	@Autowired
	private YsyUserRepository ysyUserRepository;
	
	
	/** user 등록 */
	public boolean userRegister(YsyUser ysyUser ) {
		
		if(ysyUserRepository.save(ysyUser)  == null) 
			return false;
		
		return true;
	}
	
	/** user 존재여부 확인 존재 : true */
	public boolean isUser(String username) {
	
		if(ysyUserRepository.findByUser_id(username) == null)
			return false;
		
		return true;
	}
}
