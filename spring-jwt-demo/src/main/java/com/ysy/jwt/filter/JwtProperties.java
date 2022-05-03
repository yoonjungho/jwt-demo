package com.ysy.jwt.filter;

/**
 * @author clubb
 * jwt token 프로퍼티
 *
 */
public interface JwtProperties {

	public String SECRET          = "ysyFirstPjt";  //jwt token 생성시 서버만 알고있는 비밀 값 , 이 값으로 복호화 함.
	public int    EXPIRATION_TIME = 1000;           //(1/1000초)토근 유효 시간
	public String TOKEN_PREFIX    = "YSYV1 ";       // token 생성 후 client 전송시 token앞에 붙을 값. 인증시에 이 값으로 1차 검사 진행
	public String HEADER_STRING   = "Authorization";// cilent 전송시 token에 대한 key
}
