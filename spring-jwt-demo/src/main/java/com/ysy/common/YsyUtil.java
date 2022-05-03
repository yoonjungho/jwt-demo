package com.ysy.common;

import org.springframework.stereotype.Component;

@Component
public class YsyUtil {

	
	public boolean isNullAndEmpty(String str) {
		boolean flag = false;
		
		if(str == null)               flag = true;
		else if(str.trim().isEmpty()) flag = true;
		return flag;
	}
	public boolean isNull(String str) {
		if(str== null) return true;
		return false;
	}
}
