package com.quanlithi.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
@Table (name = "giang_vien")
public class GiangVienEntity {
	@Id
	private long id;
	
	private String hoTen;
	private String email;
	private String createdBy;
	private String modifiedBy;
	// relationship
	@OneToMany(
	        mappedBy = "giangVien",
	        cascade = CascadeType.ALL,
	        orphanRemoval = true
    )
	@Builder.Default
    private List<LopHocEntity> listLH = new ArrayList<>();
	
	@Builder.Default
    @ManyToMany(mappedBy = "listGV")
    private List<LopThiEntity> listLT = new ArrayList<>();
}
