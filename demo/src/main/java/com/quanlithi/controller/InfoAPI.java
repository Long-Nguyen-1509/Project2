package com.quanlithi.controller;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quanlithi.dto.base.CaThiDetails;
import com.quanlithi.dto.base.HocPhanBase;
import com.quanlithi.dto.base.LopHocDetails;
import com.quanlithi.dto.base.LopThiDetails;
import com.quanlithi.dto.base.SinhVienBar;
import com.quanlithi.dto.base.SinhVienDetails;
import com.quanlithi.entity.CaThi;
import com.quanlithi.entity.HocPhanEntity;
import com.quanlithi.entity.LopHocEntity;
import com.quanlithi.entity.LopThiEntity;
import com.quanlithi.entity.LopThiSinhVien;
import com.quanlithi.entity.SinhVienEntity;
import com.quanlithi.service.GiangVienService;
import com.quanlithi.service.HocPhanService;
import com.quanlithi.service.LopHocService;
import com.quanlithi.service.LopThiService;
import com.quanlithi.service.LopThiSinhVienService;
import com.quanlithi.service.SinhVienService;

@RestController
@RequestMapping("/api/auth/info")
public class InfoAPI {
	
	@Autowired
	private SinhVienService sinhVienService;
	
	@Autowired
	private HocPhanService hocPhanService;
	 
	@Autowired
	private LopHocService lopHocService;
	
	@Autowired
	private LopThiService lopThiService;
	
	@Autowired
	private LopThiSinhVienService lopThiSinhVienService;
	
	@Autowired
	private GiangVienService giangVienService;
	
	@Autowired
	private SinhVienController sinhVienController;
	
	
	@Autowired
	private ModelMapper modelMapper;
	
	@GetMapping("/sv/{id}")
	public ResponseEntity<SinhVienDetails> getSinhVien(@PathVariable long id) {
        SinhVienEntity sv = sinhVienService.get(id);
        return ResponseEntity.ok(sinhVienController.mapToSinhVienDetails(sv));
    }
	
	@GetMapping("/gv/{id}")
	public ResponseEntity<SinhVienDetails> getGiangVien(@PathVariable long id) {
        SinhVienEntity sv = sinhVienService.get(id);
        return ResponseEntity.ok(sinhVienController.mapToSinhVienDetails(sv));
    }
	
	@GetMapping("/hp/{id}")
	public ResponseEntity<HocPhanBase> getHocPhan(@PathVariable String id){
		HocPhanEntity hp = hocPhanService.get(id);
		return ResponseEntity.ok(modelMapper.map(hp, HocPhanBase.class)) ;
	}
	
	@GetMapping("/lh/{id}")
	public ResponseEntity<LopHocDetails> getLopHoc(@PathVariable long id) {
		LopHocEntity lh = lopHocService.get(id);
		LopHocDetails details = modelMapper.map(lh, LopHocDetails.class);
		details.setSoSV(lh.getStudentCount());
		return ResponseEntity.ok(details);
	}
	
	@GetMapping("/lt/{id}")
	public ResponseEntity<LopThiDetails> getLopThi(@PathVariable long id) {
		LopThiEntity lopThiEntity = lopThiService.get(id);
		LopHocEntity lopHocEntity = lopThiEntity.getLopHoc();
		HocPhanEntity hocPhanEntity = lopHocEntity.getHocPhan();
		List<CaThi> caThis = lopThiEntity.getListCT();
		List<List<LopThiSinhVien>> listLTSV = new ArrayList<>(); 
		for (CaThi caThi: caThis) {
			listLTSV.add(lopThiSinhVienService.getAllLTSV(lopThiEntity.getId(), caThi.getId()));
		}
		LopThiDetails lopThiDetails = LopThiDetails.builder()
				.id(lopThiEntity.getId())
				.maLopHoc(lopHocEntity.getId())
				.maHocPhan(hocPhanEntity.getId())
				.tenHocPhan(hocPhanEntity.getTenHocPhan())
				.nhomThiId(lopThiEntity.getNhomThiId())
				.nhomThi(lopThiEntity.getNhomThi())
				.kiHoc(lopHocEntity.getKiHoc())
				.khoaHoc(lopHocEntity.getKhoaHoc())
				.ngayThi(lopThiEntity.getNgayThi())
				.build();
		for (int i = 0; i < caThis.size(); i++) {
			List<SinhVienBar> sinhViens = new ArrayList<>();
			List<LopThiSinhVien> list = listLTSV.get(i);
			for (LopThiSinhVien ltsv : list) {
				SinhVienBar temp = SinhVienBar.builder()
						.stt(ltsv.getStt())
						.id(ltsv.getSinhVien().getId())
						.hoTen(ltsv.getSinhVien().getHoTen())
						.ngaySinh(ltsv.getSinhVien().getNgaySinh())
						.lopSV(ltsv.getSinhVien().getLopSV())
						.build();
				sinhViens.add(temp);
			}
			CaThiDetails caThi = CaThiDetails.builder()
					.kipThi(caThis.get(i).getKipThi())
					.phongThi(caThis.get(i).getPhongThi())
					.listSV(sinhViens)
					.build();
			lopThiDetails.getListCT().put(caThi, listLTSV.get(i).size());
		}
		return ResponseEntity.ok(lopThiDetails);
	}
}
