package com.quanlithi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.quanlithi.entity.HocPhanEntity;

public interface HocPhanRepo extends JpaRepository<HocPhanEntity, String> {
	List<HocPhanEntity> findByIdContainingOrTenHocPhanContaining(String id, String name);
}
