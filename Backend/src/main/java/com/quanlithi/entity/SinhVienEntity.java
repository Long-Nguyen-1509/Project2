 package com.quanlithi.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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
@Table(name = "sinh_vien")
public class SinhVienEntity {
	@Id
	private long id;

	private String hoTen;
	@DateTimeFormat(pattern = "MM/dd/yyyy")
	private LocalDate ngaySinh;
	private String gioiTinh;
	private String lopSV;
	private String email;

	// relationship

	// quan he voi lop hoc
	@Builder.Default
	@ManyToMany(
			cascade = {
					CascadeType.PERSIST,
					CascadeType.MERGE
					}
			)
    @JoinTable(name = "lop_hoc_sinh_vien",
        	joinColumns = @JoinColumn(name = "sinh_vien_id"),
        	inverseJoinColumns = @JoinColumn(name = "lop_hoc_id")
    )
	private List<LopHocEntity> listLH = new ArrayList<>();

	// quan he voi lop thi
	@Builder.Default
	@OneToMany(
			mappedBy = "sinhVien",
			cascade = CascadeType.PERSIST,
			orphanRemoval = true
    )
    private List<LopThiSinhVien> listLTSV = new ArrayList<>();

}
