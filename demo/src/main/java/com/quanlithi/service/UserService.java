package com.quanlithi.service;

import java.util.Set;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService{

	UserDetails loadUserByUsername(String username);

	void createUser(String username, String password, String fullName, Set<Long> roleIds);
}
