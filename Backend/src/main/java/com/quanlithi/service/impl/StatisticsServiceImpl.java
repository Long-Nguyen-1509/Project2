package com.quanlithi.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quanlithi.entity.CaThi;
import com.quanlithi.entity.LopThiEntity;
import com.quanlithi.repository.CaThiRepo;
import com.quanlithi.repository.LopThiRepo;
import com.quanlithi.service.StatisticsService;

@Service
public class StatisticsServiceImpl implements StatisticsService{
	
	@Autowired
	private CaThiRepo caThiRepo;
	
	@Autowired
	private LopThiRepo lopThiRepo;
	
	@Override
	public List<CaThi> getCaThiStatistics(LocalDate date) {
		return caThiRepo.findAllByNgayThiOrderByPhongThiAscKipThiAsc(date)
				.orElseThrow(); 
	}

	@Override
	public List<LopThiEntity> getLopThiStatistics(LocalDate date) {
		return lopThiRepo.findByNgayThi(date)
				.orElseThrow();
	}

	@Override
	public List<Optional<CaThi>> getPhongThiStatistics(LocalDate date) {
		return caThiRepo.findAllByNgayThiOrderByKipThiAscPhongThiAsc(date);
	}
	
	
}
