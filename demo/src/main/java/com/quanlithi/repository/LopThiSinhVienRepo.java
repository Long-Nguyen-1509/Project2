package com.quanlithi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.quanlithi.entity.LopThiSinhVien;

public interface LopThiSinhVienRepo extends JpaRepository<LopThiSinhVien, Long>{
	
	Optional<LopThiSinhVien> findByLopThiIdAndSinhVienId(long lopThiId, long SinhVienId);
	
	List<LopThiSinhVien> findAllByLopThiIdAndCaThiIdOrderByStt(long lopThiId, long caThiId);
	
	Optional<List<LopThiSinhVien>> findAllBySinhVienId(long id);
	
	Optional<List<LopThiSinhVien>> findAllByLopThiId(long id);
}
