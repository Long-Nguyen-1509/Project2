package com.quanlithi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import com.quanlithi.service.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private AuthenticationFilter jwtAuthFilter;

	@Autowired
	private AuthenticationProvider authenticationProvider;
	
	@Autowired
	private LogoutHandler logoutHandler;
	
	@SuppressWarnings("removal")
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
		http
        	.csrf()
        	.disable()
        	.authorizeHttpRequests()
        	.requestMatchers("/api/auth/**").permitAll()
        	.requestMatchers("/api/admin/**").hasRole("ADMIN")
        	.requestMatchers("/api/sv/**").hasRole("SV")
        	.requestMatchers("/api/gv/**").hasRole("GV")
        	.anyRequest().authenticated()
        		.and()
        	.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        		.and()
        	.authenticationProvider(authenticationProvider)
        	.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
        	.logout()
        	.logoutUrl("/api/auth/logout")
        	.addLogoutHandler(logoutHandler)
        	.logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
        ;
		return http.build();
	}
}
