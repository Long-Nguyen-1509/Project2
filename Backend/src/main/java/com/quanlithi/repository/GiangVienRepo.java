package com.quanlithi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.quanlithi.entity.GiangVienEntity;

public interface GiangVienRepo extends JpaRepository<GiangVienEntity, Long>{
	
	List<GiangVienEntity> findByIdContainingOrHoTenContaining(String id, String name);
}
