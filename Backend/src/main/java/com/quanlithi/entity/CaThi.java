package com.quanlithi.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
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
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ca_thi")
public class CaThi {
	@Id
	private long id;
	
	private int kipThi;
	private String phongThi;
	
	@DateTimeFormat(pattern = "MM/dd/yyyy")
	private LocalDate ngayThi;
	
    @Builder.Default
    @OneToMany(
	        mappedBy = "caThi",
    		cascade = {
    				CascadeType.PERSIST,
    				CascadeType.MERGE
    				},
	        orphanRemoval = true
    )
    private List<LopThiSinhVien> listLTSV = new ArrayList<>();
    
    @Builder.Default
    @ManyToMany(
    		mappedBy = "listCT",
    		cascade = {
    				CascadeType.PERSIST,
    				CascadeType.MERGE
    				}
    )
    private List<LopThiEntity> listLT = new ArrayList<>();
    
    @Transient
    public int getStudentCount() {
        return listLTSV.size();
    }
	
}
