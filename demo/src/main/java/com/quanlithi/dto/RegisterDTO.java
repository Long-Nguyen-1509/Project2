package com.quanlithi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDTO{
	private String fullname;
	private String username;
	private String password;
	private final Long role = 3L;
}
