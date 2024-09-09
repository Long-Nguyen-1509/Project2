package com.quanlithi.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LopThiSinhVien{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sinh_vien_id")
    private SinhVienEntity sinhVien;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ca_thi_id")
    private CaThi caThi;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lop_thi_id")
    private LopThiEntity lopThi;
    
    private int stt;

}