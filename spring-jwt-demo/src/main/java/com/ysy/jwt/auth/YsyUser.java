package com.ysy.jwt.auth;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/** create by yjh
 *  user 기본 정보 data
 * 
 * */

@Getter
@Setter
@Data
@Entity
@Table(name="ysy_user_mst")
public class YsyUser {

	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column
	private String userName;
	@Column(length = 255 , nullable = false , unique = true)
	private String userId;
	@Column
	private String pwd;
	@Column
	private String email;
	@Column
	private String roles;
}
