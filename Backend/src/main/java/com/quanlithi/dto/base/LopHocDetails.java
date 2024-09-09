package com.quanlithi.dto.base;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LopHocDetails {
	private Long id;
	private String maHocPhan;
	private String tenHocPhan;
	private String khoaHoc;
	private String kiHoc;
	private Long gvId;
	private String tenGv;
	private Integer soSV;

	private List<SinhVienBar> listSV;
}
