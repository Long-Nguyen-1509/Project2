package com.quanlithi.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.quanlithi.entity.CaThi;

public interface CaThiRepo extends JpaRepository<CaThi, Long>{
	
	Optional<List<CaThi>> findAllByNgayThiOrderByPhongThiAscKipThiAsc(LocalDate date); 
	
	List<Optional<CaThi>> findAllByNgayThiOrderByKipThiAscPhongThiAsc(LocalDate date);
}
