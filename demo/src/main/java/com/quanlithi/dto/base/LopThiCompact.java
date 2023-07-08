package com.quanlithi.dto.base;

import java.time.LocalDate;

import org.springframework.hateoas.RepresentationModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LopThiCompact extends RepresentationModel<LopThiCompact>{
	private Long id;
	private Long maLopHoc;
	private String maHocPhan;
	private String tenHocPhan;
	private int nhomThiId;
	private String nhomThi;
	private String kiHoc;
	private String khoaHoc;
	private LocalDate ngayThi;
	private Integer soSV;
}
