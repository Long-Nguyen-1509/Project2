package com.quanlithi.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quanlithi.entity.LopThiSinhVien;
import com.quanlithi.repository.LopThiSinhVienRepo;
import com.quanlithi.service.LopThiSinhVienService;

@Service
public class LopThiSinhVienServiceImpl implements LopThiSinhVienService{
	
	@Autowired
	private LopThiSinhVienRepo lopThiSinhVienRepo;
	
	@Override
	public List<LopThiSinhVien> getAllLTSV(long lopThiId, long caThiId) {
		List<LopThiSinhVien> lopThiSinhViens = lopThiSinhVienRepo
				.findAllByLopThiIdAndCaThiIdOrderByStt(lopThiId, caThiId);
		return lopThiSinhViens;
	}
}
