package com.quanlithi.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.quanlithi.dto.statistics.CaThiCompact;
import com.quanlithi.dto.statistics.CaThiStatistics;
import com.quanlithi.entity.CaThi;
import com.quanlithi.entity.LopThiEntity;
import com.quanlithi.service.KipThiService;
import com.quanlithi.service.LopThiSinhVienService;
import com.quanlithi.service.PhongThiService;
import com.quanlithi.service.StatisticsService;

@RestController
@RequestMapping("/api/auth/stats")
public class StatisticsController {
	
	@Autowired
	private LopThiSinhVienService lopThiSinhVienService;
	
	@Autowired
	private StatisticsService statisticsService;
	
	@Autowired
	private KipThiService kipThiService;
	
	@Autowired
	private PhongThiService phongThiService;
	
	@GetMapping("/ct")
	public ResponseEntity<Map<String, List<CaThiStatistics>>> getCaThiStatistics(
			
			@RequestParam("date") @DateTimeFormat(pattern = "MM/dd/yyyy") LocalDate date) {
		Map<String, List<CaThiStatistics>> map = new HashMap<>();
		
		List<CaThi> caThis = statisticsService.getCaThiStatistics(date);
		for (CaThi caThi : caThis) {
			String phongThi = caThi.getPhongThi();
			Map<Long, Integer> mapLopThi = new HashMap<>();
			List<LopThiEntity> listLopThi = caThi.getListLT();
			for (LopThiEntity lopThi : listLopThi) {
				mapLopThi.put(lopThi.getId(), lopThiSinhVienService.getAllLTSV(lopThi.getId(), caThi.getId()).size());
			}
			CaThiStatistics temp = CaThiStatistics.builder()
					.lopHocId(listLopThi.get(0).getLopHoc().getId())
					.kipThi(caThi.getKipThi())
					.gioThi(kipThiService.get(caThi.getKipThi()).getGioThi())
					.mapLopThi(mapLopThi)
					.build();
			if (!map.containsKey(phongThi)) {
				List<CaThiStatistics> newList = new ArrayList<>();
				newList.add(temp);
                map.put(phongThi, newList);
			} else {
				map.get(phongThi).add(temp);
			}
		}
		return ResponseEntity.ok(map);
	}
	
	@GetMapping("/lt")
	public ResponseEntity<Map<String, List<CaThiCompact>>> getLopThiStatistics(
			@RequestParam("date") @DateTimeFormat(pattern = "MM/dd/yyyy") LocalDate date) {
		
    	Map<String, List<CaThiCompact>> map = new HashMap<>();
    	List<LopThiEntity> list = statisticsService.getLopThiStatistics(date);
    	for (LopThiEntity lopThiEntity : list) {
    		List<CaThi> caThis = lopThiEntity.getListCT();
    		List<CaThiCompact> listCompacts = new ArrayList<>(); 
    		for (CaThi caThi : caThis) {
    			CaThiCompact compact = CaThiCompact.builder()
    					.kipThi(caThi.getKipThi())
    					.phongThi(caThi.getPhongThi())
    					.soSV(lopThiSinhVienService.getAllLTSV(lopThiEntity.getId(), caThi.getId()).size())
    					.build();
    			listCompacts.add(compact);
			}
    		map.put(String.valueOf(lopThiEntity.getId()), listCompacts);	
		}
    	return ResponseEntity.ok(map);
    }
	
	@GetMapping("/pt")
	public ResponseEntity<List<List<String>>> getPhongThiStatistics(
			@RequestParam("date") @DateTimeFormat(pattern = "MM/dd/yyyy") LocalDate date) {
		List<List<String>> list = new ArrayList<>();
		list.add(phongThiService.getAllNameOrdered());
		list.add(kipThiService.getAllGioThi());
		List<Optional<CaThi>> caThiList = statisticsService.getPhongThiStatistics(date);
		List<String> soSV = new ArrayList<>();
		for (Optional<CaThi> optional : caThiList) {
			if (optional.isPresent()) {
				soSV.add(String.valueOf(optional.get().getStudentCount()));
			} else {
				soSV.add("Không dùng");
			}
		}
		list.add(soSV);
		return ResponseEntity.ok(list);
	}
}
