package com.ysy.jwt.auth;

import lombok.Data;

@Data
public class LoginReqData {

	private String username;
	private String password;
}
