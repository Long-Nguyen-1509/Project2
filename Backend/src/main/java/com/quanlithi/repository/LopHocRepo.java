package com.quanlithi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.quanlithi.entity.LopHocEntity;

public interface LopHocRepo extends JpaRepository<LopHocEntity, Long> {

	LopHocEntity findOneById(Long long1);

	List<LopHocEntity> findByIdContaining(String id);

}
