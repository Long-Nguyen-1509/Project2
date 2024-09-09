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
public class LopThiBase {
	private long id;
	private long lopHocId;
	private String hocPhanId;
	private String tenHocPhan;
	private String khoaHoc;
	private String kiHoc;
	private String tenGv;
	private LocalDate ngayThi;
	private String nhomThi;
	private int kipThiId;
	private String phongThi;
	private int stt;
}
