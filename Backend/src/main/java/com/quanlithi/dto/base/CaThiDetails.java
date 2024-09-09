package com.quanlithi.dto.base;

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
public class CaThiDetails {
	private int kipThi;
	private String phongThi;
	
	@Builder.Default
	private List<SinhVienBar> listSV = new ArrayList<>();
}
