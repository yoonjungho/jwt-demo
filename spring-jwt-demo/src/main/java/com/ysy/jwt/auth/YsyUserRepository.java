package com.ysy.jwt.auth;

import org.springframework.data.jpa.repository.JpaRepository;

public interface YsyUserRepository extends JpaRepository<YsyUser, Long>{

	public YsyUser findByUsername(String username);
}
