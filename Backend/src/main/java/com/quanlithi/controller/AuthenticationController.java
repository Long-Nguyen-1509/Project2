package com.quanlithi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quanlithi.dto.LoginRequest;
import com.quanlithi.dto.LoginResponse;
import com.quanlithi.dto.RegisterDTO;
import com.quanlithi.service.impl.AuthenticationServiceImpl;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
	
	@Autowired
	private AuthenticationServiceImpl service;
	
	@PostMapping("/register")
	public ResponseEntity<LoginResponse> register(@RequestBody RegisterDTO request) {
		return ResponseEntity.ok(service.register(request));
	}
	
	@PostMapping("/authenticate")
	public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginRequest request) {
		return ResponseEntity.ok(service.authenticate(request));
	}
	
	@GetMapping
	public String sayHello() {
        return "Hello, World!";	
	}
}
