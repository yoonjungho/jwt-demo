package com.ysy.jwt.auth;

import org.springframework.data.jpa.repository.JpaRepository;

public interface YsyUserRepository extends JpaRepository<YsyUser, Long>{

	public YsyUser findByUser_id(String user_id);
}
