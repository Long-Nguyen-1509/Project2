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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table (name = "lop_thi")
public class LopThiEntity {
	@Id
	private long id;

	private int nhomThiId;
	private String nhomThi;
	@DateTimeFormat(pattern = "MM/dd/yyyy")
	private LocalDate ngayThi;

	// relationships

	// quan he voi giang vien
	@Builder.Default
    @ManyToMany(
    		cascade = {
    				CascadeType.PERSIST,
    				CascadeType.MERGE
    				}
    )
    @JoinTable(
    		name = "lop_thi_giang_vien",
        	joinColumns = @JoinColumn(name = "lop_thi_id"),
        	inverseJoinColumns = @JoinColumn(name = "giang_vien_id")
    )
    private List<GiangVienEntity> listGV = new ArrayList<>();
	
	@Builder.Default
    @ManyToMany(
    		cascade = {
    				CascadeType.PERSIST,
    				CascadeType.MERGE
    				}
    )
    @JoinTable(
    		name = "lop_thi_ca_thi",
        	joinColumns = @JoinColumn(name = "lop_thi_id"),
        	inverseJoinColumns = @JoinColumn(name = "ca_thi_id")
    )
    private List<CaThi> listCT = new ArrayList<>();
	
    // quan he voi lop hoc
    @ManyToOne
    private LopHocEntity lopHoc;
    
    @Builder.Default
    @OneToMany(mappedBy = "lopThi")
    private List<LopThiSinhVien> listLTSV = new ArrayList<>();
    
    @Transient
    public int getStudentCount() {
        return listLTSV.size();
    }
    
}
