package com.ysy.common;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.stereotype.Component;

@Component
public class YsyUtil {
	
	
	private boolean isLog = true;
	
	public void log(Object obj) {
		if(isLog) System.out.println(obj.getClass().getName());
	}
	public void log(String msg) {
		if(isLog) System.out.println(getTime() + " : " + msg);
	}
	
	public String getTime() {
		
		LocalDate date = LocalDate.now();
		LocalTime time = LocalTime.now();
		return date + " " + time;
	}

	//널 ,빈값 체크
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
