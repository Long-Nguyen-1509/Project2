package com.quanlithi.controller;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quanlithi.dto.base.LopThiBase;
import com.quanlithi.dto.base.SinhVienDetails;
import com.quanlithi.entity.CaThi;
import com.quanlithi.entity.GiangVienEntity;
import com.quanlithi.entity.HocPhanEntity;
import com.quanlithi.entity.LopHocEntity;
import com.quanlithi.entity.LopThiEntity;
import com.quanlithi.entity.LopThiSinhVien;
import com.quanlithi.entity.SinhVienEntity;
import com.quanlithi.service.SinhVienService;

@RestController
@RequestMapping("/api/sv")
public class SinhVienController {

	@Autowired
    private SinhVienService sinhVienService;
	
	@Autowired
	private ModelMapper modelMapper;
    
    @GetMapping("/self")
    public ResponseEntity<SinhVienDetails> getSinhVienEntityById(Authentication authentication) {
        
        
        String username = authentication.getName();
        if (username != null) {
			SinhVienEntity sv = sinhVienService.getStudentByEmail(username);
			return ResponseEntity.ok(mapToSinhVienDetails(sv));
		}
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        
    }
    
    protected SinhVienDetails mapToSinhVienDetails(SinhVienEntity sinhVienEntity) {
    	SinhVienDetails sinhVienDetails = modelMapper.map(sinhVienEntity, SinhVienDetails.class);
    	List<LopThiSinhVien> lopThiSinhViens = sinhVienEntity.getListLTSV();
    	for (LopThiSinhVien lopThiSinhVien : lopThiSinhViens) {
    		CaThi caThi = lopThiSinhVien.getCaThi();
    		LopThiEntity lopThiEntity = lopThiSinhVien.getLopThi();
    		LopHocEntity lopHocEntity = lopThiEntity.getLopHoc();
    		HocPhanEntity hocPhanEntity = lopHocEntity.getHocPhan();
    		GiangVienEntity giangVienEntity = lopHocEntity.getGiangVien();
    		LopThiBase temp = LopThiBase.builder()
    						.id(lopThiEntity.getId())
    						.lopHocId(lopHocEntity.getId())
    						.hocPhanId(hocPhanEntity.getId())
    						.tenHocPhan(hocPhanEntity.getTenHocPhan())
    						.khoaHoc(lopHocEntity.getKhoaHoc())
    						.kiHoc(lopHocEntity.getKiHoc())
//    						.tenGv(giangVienEntity.getHoTen())
    						.ngayThi(lopThiEntity.getNgayThi())
    						.nhomThi(lopThiEntity.getNhomThi())
    						.kipThiId(caThi.getKipThi())
    						.phongThi(caThi.getPhongThi())
    						.stt(lopThiSinhVien.getStt())
    						.build();
			sinhVienDetails.getLopThiBases().add(temp);
		}
    	return sinhVienDetails;
    }
    
    
    
}
