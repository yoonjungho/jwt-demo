package com.ysy.jwt.auth;

import lombok.Data;

/**
 * 
 * @author clubbboy@naver.com
 * 2022 05 05
 * 로그인 시도시 request의 로그인 정보 임시로 담아서 인증해줄 클래스
 */
@Data
public class LoginReqData {

	private String username;
	private String password;
}
