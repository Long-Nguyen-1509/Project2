package com.quanlithi.service;

import java.time.LocalDate;

public interface ExportService {
	
	byte[] exportLopThiInfo(long id, int caThiNumber);
	
	byte[] exportCaThiStatistics(LocalDate date);
	
	byte[] exportLopThiStatistics(LocalDate date);
	
	byte[] exportPhongThiStatistics(LocalDate date);
}
