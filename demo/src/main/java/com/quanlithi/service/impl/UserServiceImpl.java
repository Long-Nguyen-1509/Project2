package com.quanlithi.service.impl;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.quanlithi.entity.RoleEntity;
import com.quanlithi.entity.UserEntity;
import com.quanlithi.repository.RoleRepo;
import com.quanlithi.repository.UserRepo;
import com.quanlithi.service.UserService;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
    private UserRepo userRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private RoleRepo roleRepo;
	
	@Override
    public UserDetails loadUserByUsername(String username) {
		UserEntity userEntity = userRepo.findByUsernameWithRoles(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
		return userEntity;
    }    
	
	@Override
    public void createUser(String username, String password, String fullName, Set<Long> roleIds) {
		UserEntity user = new UserEntity();
	    user.setUsername(username);
	    user.setPassword(passwordEncoder.encode(password));
	    user.setFullName(fullName);

	    Set<RoleEntity> roles = new HashSet<>();
	    for (long roleId : roleIds) {
	        RoleEntity role = roleRepo.findById(roleId)
	                .orElseThrow(() -> new RuntimeException("Role not found"));
	        roles.add(role);
	    }

//	    user.setRoles(roles);

	    userRepo.save(user);
    }

}
