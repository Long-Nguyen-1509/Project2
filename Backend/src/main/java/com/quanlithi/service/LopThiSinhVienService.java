package com.quanlithi.service;

import java.util.List;

import com.quanlithi.entity.CaThi;
import com.quanlithi.entity.LopThiEntity;
import com.quanlithi.entity.LopThiSinhVien;

public interface LopThiSinhVienService {
	
	List<LopThiSinhVien> getAllLTSV(long lopThiId, long caThiId);

}
