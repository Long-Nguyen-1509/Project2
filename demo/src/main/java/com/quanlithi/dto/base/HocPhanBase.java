package com.quanlithi.dto.base;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HocPhanBase {
	private String id;
	private String hocPhanId;
	private String tenHocPhan;
	private String khoiLuong;
	private String donVi;
	private String donViCon;
	private Integer tinChi;
	private Integer tinChiHocPhi;
	private String moTa;
}
