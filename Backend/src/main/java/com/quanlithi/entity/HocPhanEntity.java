package com.quanlithi.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table (name = "hoc_phan")
public class HocPhanEntity{
	@Id
	private String id;

	private String tenHocPhan;
	private String khoiLuong;
	private String donVi;
	private String donViCon;
	private int tinChi;
	private int tinChiHocPhi;
	private String moTa;


	// relationship
	@Builder.Default
	@OneToMany(
	        mappedBy = "hocPhan",
	        cascade = CascadeType.ALL,
	        orphanRemoval = true
    )
    private List<LopHocEntity> listLH = new ArrayList<>();
}

