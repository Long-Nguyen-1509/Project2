package com.quanlithi.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.quanlithi.entity.LopThiSinhVien;
import com.quanlithi.entity.SinhVienEntity;
import com.quanlithi.repository.LopThiSinhVienRepo;
import com.quanlithi.repository.SinhVienRepo;
import com.quanlithi.service.SinhVienService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class SinhVienServiceImpl implements SinhVienService{

	@Autowired
	private SinhVienRepo sinhVienRepo;
	
	@Autowired
	private LopThiSinhVienRepo lopThiSinhVienRepo;

	
	@Override
	public SinhVienEntity get(long id) {
		SinhVienEntity sinhVienEntity = sinhVienRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException());
		return sinhVienEntity;
	}
	
	@Override
	public SinhVienEntity getStudentByEmail(String username) {
		SinhVienEntity sinhVienEntity = sinhVienRepo.findByEmail(username)
				.orElseThrow(() -> new EntityNotFoundException());
		return sinhVienEntity;
	}

	@Override
	public Page<SinhVienEntity> search(String query, int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("hoTen").ascending());
		return sinhVienRepo.search(query, query, pageable);
    }
	
	@Override
	public List<LopThiSinhVien> getListLopThi(long id) {
		List<LopThiSinhVien> lopThiSinhVien = lopThiSinhVienRepo.findAllBySinhVienId(id)
				.orElseThrow();
		return lopThiSinhVien;
		
	}

}
