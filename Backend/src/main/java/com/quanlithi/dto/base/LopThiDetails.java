package com.quanlithi.dto.base;

import java.time.LocalDate;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LopThiDetails {
	private Long id;
	private Long maLopHoc;
	private String maHocPhan;
	private String tenHocPhan;
	private Integer nhomThiId;
	private String nhomThi;
	private String kiHoc;
	private String khoaHoc;
	private LocalDate ngayThi;
	
	@Builder.Default
	private Map<CaThiDetails, Integer> listCT = new HashMap<>();
	
}
