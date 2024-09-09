package com.quanlithi.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.quanlithi.entity.SinhVienEntity;


public interface SinhVienRepo extends JpaRepository<SinhVienEntity	, Long> {
	
	@Query(value = "SELECT s FROM SinhVienEntity s "
			+ "WHERE CAST(s.id AS string) LIKE :id% "
			+ "OR s.hoTen LIKE %:hoTen%",
           countQuery = "SELECT COUNT(s) FROM SinhVienEntity s "
           		+ "WHERE CAST(s.id AS string) LIKE %:id% "
           		+ "OR s.hoTen LIKE %:hoTen%")
	Page<SinhVienEntity> search(@Param("id") String id, @Param("hoTen") String hoTen, Pageable pageable);

	Optional<SinhVienEntity> findByEmail(String username);
}
