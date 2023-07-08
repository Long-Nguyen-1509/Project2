package com.quanlithi.dto.base;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SinhVienBar {
	private int stt;
	private long id;
	private String hoTen;
	private LocalDate ngaySinh;
	private String lopSV;
}
