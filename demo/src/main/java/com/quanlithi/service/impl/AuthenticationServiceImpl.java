package com.quanlithi.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quanlithi.dto.LoginRequest;
import com.quanlithi.dto.LoginResponse;
import com.quanlithi.dto.RegisterDTO;
import com.quanlithi.entity.RoleEntity;
import com.quanlithi.entity.Token;
import com.quanlithi.entity.UserEntity;
import com.quanlithi.repository.RoleRepo;
import com.quanlithi.repository.TokenRepo;
import com.quanlithi.repository.UserRepo;
import com.quanlithi.service.AuthenticationService;
import com.quanlithi.service.JWTService;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.var;

@Service
public class AuthenticationServiceImpl implements AuthenticationService{

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JWTService jwtService;
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private TokenRepo tokenRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private RoleRepo roleRepo;
	
	@Override
	public LoginResponse register(RegisterDTO request) {
		List<RoleEntity> roles = new ArrayList<>();
		roles.add(roleRepo.findById(request.getRole()).orElseThrow());
	    var user = UserEntity.builder()
	        .fullName(request.getFullname())
	        .username(request.getUsername())
	        .password(passwordEncoder.encode(request.getPassword()))
	        .roles(roles)
	        .build();
	    var savedUser = userRepo.save(user);
	    var jwtToken = jwtService.generateToken(user);
	    var refreshToken = jwtService.generateRefreshToken(user);
	    saveUserToken(savedUser, jwtToken);
	    return LoginResponse
				.builder()
				.accessToken(jwtToken)
				.refreshToken(refreshToken)
				.build();
	}
	
	@Override
	public void saveUser(RegisterDTO request) {
		List<RoleEntity> roles = new ArrayList<>();
		roles.add(roleRepo.findById(request.getRole()).orElseThrow());
	    var user = UserEntity.builder()
	        .fullName(request.getFullname())
	        .username(request.getUsername())
	        .password(passwordEncoder.encode(request.getPassword()))
	        .roles(roles)
	        .build();
	    userRepo.save(user);
	}
	
	@Override
	public LoginResponse authenticate(LoginRequest request) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
				request.getUsername(),
				request.getPassword()
				)
		);
		UserEntity user = userRepo.findByUsernameWithRoles(request.getUsername())
			.orElseThrow();
		var jwtToken = jwtService.generateToken(user);
		var refreshToken = jwtService.generateRefreshToken(user);
		revokeAllUserTokens(user);
		saveUserToken(user, jwtToken);
		return LoginResponse
				.builder()
				.accessToken(jwtToken)
				.refreshToken(refreshToken)
				.build();
    }
	
	private void saveUserToken(UserEntity user, String jwtToken) {
		var token = Token.builder()
				.user(user)
				.token(jwtToken)
				.expired(false)
				.revoked(false)
				.build();
		tokenRepo.save(token);
	}
	
	private void revokeAllUserTokens(UserEntity user) {
	    List<Token> validUserTokens = tokenRepo.findAllValidTokenByUser(user.getId());
	    if (validUserTokens.toString().isEmpty()) {
	    	return;
	    }
	    for(var token: validUserTokens) {
	    	token.setExpired(true);
	    	token.setRevoked(true);
	    	tokenRepo.save(token);
	    }
	}
	
	public void refreshToken(
			HttpServletRequest request,
			HttpServletResponse response
			) throws IOException, StreamWriteException, DatabindException, java.io.IOException {
		
		final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		final String refreshToken;
		final String username;
		if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
			return;
		}
		refreshToken = authHeader.substring(7);
		username = jwtService.extractUsername(refreshToken);
		if (username != null) {
			var user = this.userRepo.findByUsernameWithRoles(username)
					.orElseThrow();
			if (jwtService.isTokenValid(refreshToken, user)) {
				var accessToken = jwtService.generateToken(user);
				revokeAllUserTokens(user);
				saveUserToken(user, accessToken);
				var authResponse = LoginResponse.builder()
						.accessToken(accessToken)
						.refreshToken(refreshToken)
						.build();
				objectMapper.writeValue(response.getOutputStream(), authResponse);
			}
		}
	}
}
