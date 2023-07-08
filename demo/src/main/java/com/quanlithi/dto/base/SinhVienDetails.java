package com.quanlithi.dto.base;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SinhVienDetails {
	private long id;
	private String hoTen;
	private LocalDate ngaySinh;
	private String gioiTinh;
	private String lopSV;
	private String email;
	
	@Builder.Default
	private List<LopThiBase> lopThiBases = new ArrayList<>();
}
