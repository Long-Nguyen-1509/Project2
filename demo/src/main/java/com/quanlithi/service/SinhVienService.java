package com.quanlithi.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.quanlithi.entity.LopThiEntity;
import com.quanlithi.entity.LopThiSinhVien;
import com.quanlithi.entity.SinhVienEntity;

public interface SinhVienService {

	SinhVienEntity get(long id);
	
	SinhVienEntity getStudentByEmail(String username);
	
	Page<SinhVienEntity> search(String query, int page, int size);
	
	List<LopThiSinhVien> getListLopThi(long id);

}
