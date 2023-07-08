package com.quanlithi.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.quanlithi.entity.CaThi;
import com.quanlithi.entity.LopThiEntity;

public interface StatisticsService {
	
	List<CaThi> getCaThiStatistics(LocalDate date);
	
	List<LopThiEntity> getLopThiStatistics(LocalDate date);
	
	List<Optional<CaThi>> getPhongThiStatistics(LocalDate date);
}
