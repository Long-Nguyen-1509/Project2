package com.quanlithi.service;

import com.quanlithi.dto.LoginRequest;
import com.quanlithi.dto.LoginResponse;
import com.quanlithi.dto.RegisterDTO;

public interface AuthenticationService {
	
	public LoginResponse register(RegisterDTO request);
	
	public void saveUser(RegisterDTO request);
	
	public LoginResponse authenticate(LoginRequest request);
	
}
