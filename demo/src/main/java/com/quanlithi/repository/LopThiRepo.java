package com.quanlithi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.quanlithi.entity.LopThiEntity;
import java.time.LocalDate;


public interface LopThiRepo extends JpaRepository<LopThiEntity, Long> {

	@Query("SELECT t FROM LopThiEntity t "
			+ "JOIN t.lopHoc h "
			+ "JOIN h.hocPhan p "
			+ "WHERE CAST(t.id AS string) LIKE %:idLopThi% "
			+ "AND CAST(h.id AS string) LIKE %:idLopHoc% "
			+ "AND p.id LIKE %:idHocPhan% "
			+ "AND p.tenHocPhan LIKE %:tenHocPhan%")
	Page<LopThiEntity> search(@Param("idLopThi") String idLopThi, 
							@Param("idLopHoc") String idLopHoc, 
							@Param("idHocPhan") String idHocPhan, 
							@Param("tenHocPhan") String tenHocPhan,
							Pageable pageable);
	
	List<LopThiEntity> findByIdContaining(String query);;
	
	Optional<List<LopThiEntity>>  findByNgayThi(LocalDate ngayThi);
}
