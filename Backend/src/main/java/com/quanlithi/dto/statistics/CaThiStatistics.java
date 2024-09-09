package com.quanlithi.dto.statistics;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CaThiStatistics {
	private long lopHocId;
	private int kipThi;
	private String gioThi;
	Map<Long, Integer> mapLopThi;
	
}
