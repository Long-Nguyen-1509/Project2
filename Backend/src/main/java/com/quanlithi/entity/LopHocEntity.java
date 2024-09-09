package com.quanlithi.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
@Table (name = "lop_hoc")
public class LopHocEntity {
	@Id
	private long id;

	private String khoaHoc;
	private String kiHoc;

	// relationship
	@Builder.Default
	@ManyToMany(
			mappedBy = "listLH", 
			cascade = {
						CascadeType.PERSIST,
						CascadeType.MERGE
	    			}
	)
    
	private List<SinhVienEntity> listSV = new ArrayList<>();
	
	@Builder.Default
    @OneToMany(
	        mappedBy = "lopHoc",
	        cascade = CascadeType.PERSIST
    )
    private List<LopThiEntity> listLT = new ArrayList<>();

    @ManyToOne
    private HocPhanEntity hocPhan;

    @ManyToOne
    private GiangVienEntity giangVien;
    
    @Transient
    public int getStudentCount() {
        return listSV.size();
    }
}
