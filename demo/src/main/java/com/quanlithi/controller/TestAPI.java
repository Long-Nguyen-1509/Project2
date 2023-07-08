package com.quanlithi.controller;

import java.io.IOException;
import java.util.List;

import org.apache.poi.EncryptedDocumentException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.quanlithi.dto.base.LopHocDetails;
import com.quanlithi.dto.base.LopThiBase;
import com.quanlithi.dto.base.SinhVienDetails;
import com.quanlithi.entity.CaThi;
import com.quanlithi.entity.GiangVienEntity;
import com.quanlithi.entity.HocPhanEntity;
import com.quanlithi.entity.LopHocEntity;
import com.quanlithi.entity.LopThiEntity;
import com.quanlithi.entity.LopThiSinhVien;
import com.quanlithi.entity.SinhVienEntity;
import com.quanlithi.service.CaThiService;
import com.quanlithi.service.ExportService;
import com.quanlithi.service.LopHocService;
import com.quanlithi.service.LopThiService;
import com.quanlithi.service.SinhVienService;
import com.quanlithi.service.StatisticsService;
import com.quanlithi.service.impl.InputServiceImpl;

@RestController
@RequestMapping("/api/auth")
public class TestAPI {
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private LopThiService lopThiService;
	
	@Autowired
	private InputServiceImpl inputService;
	
	@Autowired
	private SinhVienService sinhVienService;
	
	@Autowired
	private CaThiService caThiService;
	
	@Autowired
	private	LopHocService lopHocService;
	
	@Autowired
	private ExportService exportService;
	
	@Autowired
	private StatisticsService statisticsService;
	
//	@GetMapping("/{id}")
//	public SinhVienDTO getSinhVienDTO(@RequestParam long id) {
//		return sinhVienService.get(id);
//	}
	
//	@GetMapping("/{id}")
//	private ResponseEntity<Set<ExamInfo>> name(@PathVariable long id) {
//		return ResponseEntity.ok(sinhVienService.getListLopThi(id));
//	}
	
	@PostMapping("/upload")
    public ResponseEntity<String> uploadExcel(@RequestParam("file") List<MultipartFile> files){
		try {
			inputService.inputAllFiles(files);
		} catch (EncryptedDocumentException e) {
			e.printStackTrace();
			return new ResponseEntity<>("File uploaded successfully.", HttpStatus.BAD_REQUEST);
		} catch (IOException e) {
			e.printStackTrace();
			return new ResponseEntity<>("File uploaded successfully.", HttpStatus.BAD_REQUEST);
		}
        return new ResponseEntity<>("File uploaded successfully.", HttpStatus.OK);
    }
	
	
	
//	@GetMapping("/exams/search")
//	public String searchSinhVienEntitys(
//	        @RequestParam(value = "idLopThi", required = false) String idLopThi,
//	        @RequestParam(value = "idLopHoc", required = false) String idLopHoc,
//	        @RequestParam(value = "idHocPhan", required = false) String idHocPhan,
//	        @RequestParam(value = "tenHocPhan", required = false) String tenHocPhan,
//	        @RequestParam(value = "page", defaultValue = "0") int page,
//	        @RequestParam(value = "size", defaultValue = "10") int size,
//	        Model model
//	) {
//	    Page<LopThiEntity> lopThiPage = lopThiService.search(idLopThi, idLopHoc, idHocPhan, tenHocPhan, page, size);
//
//	    List<LopThiDetails> lopThiDetails = lopThiPage.getContent().stream()
//	            .map(lt -> convertToDTOWithLinks(lt))
//	            .collect(Collectors.toList());
//
//	    // Get pagination information
//	    int totalPages = lopThiPage.getTotalPages();
//	    int currentPage = lopThiPage.getNumber();
//	    boolean isFirstPage = currentPage == 0;
//	    boolean isLastPage = currentPage == totalPages - 1;
//	    int previousPage = isFirstPage ? currentPage : currentPage - 1;
//	    int nextPage = isLastPage ? currentPage : currentPage + 1;
//	    int startPage = Math.max(0, currentPage - 2);
//	    int endPage = Math.min(totalPages - 1, currentPage + 2);
//
//	    List<Integer> pageNumbers = IntStream.rangeClosed(startPage, endPage)
//	            .boxed()
//	            .collect(Collectors.toList());
//
//	    model.addAttribute("lopthi", lopThiDetails);
//	    model.addAttribute("totalPages", totalPages);
//	    model.addAttribute("currentPage", currentPage);
//	    model.addAttribute("isFirstPage", isFirstPage);
//	    model.addAttribute("isLastPage", isLastPage);
//	    model.addAttribute("previousPage", previousPage);
//	    model.addAttribute("nextPage", nextPage);
//	    model.addAttribute("pageNumbers", pageNumbers);
//
//	    return "lopthi-search";
//	}
//	
//	private LopThiSearchResult convertToDTOWithLinks(LopThiEntity lopThiEntity) {
//		LopThiSearchResult lopThiSearchResult = modelMapper.map(lopThiEntity, LopThiSearchResult.class);
//		lopThiSearchResult.getLinks().add(Link.of(""))
//		return null;
//	}
	
	@GetMapping("/stinfos")
    public ResponseEntity<SinhVienDetails> getSinhVienEntityById(Authentication authentication) {
        if (authentication != null) {
			SinhVienEntity sv = sinhVienService.getStudentByEmail(authentication.getName());
			return ResponseEntity.ok(mapToSinhVienDetails(sv));
		}
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        
    }
    
    private SinhVienDetails mapToSinhVienDetails(SinhVienEntity sinhVienEntity) {
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
    						.tenGv(giangVienEntity.getHoTen())
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
    
    @GetMapping("/test/{id}")
    private ResponseEntity<LopHocDetails> get(@PathVariable long id){
    	LopHocEntity lopHocEntity = lopHocService.get(id);
    	LopHocDetails lopHocDetails = modelMapper.map(lopHocEntity, LopHocDetails.class);
		return ResponseEntity.ok(lopHocDetails);
    	
    } 
    
    @GetMapping("/class-details/download")
    public ResponseEntity<byte[]> downloadClassDetails(@RequestParam("id") long id) throws IOException {

        byte[] docxBytes = exportService.exportLopThiInfo(id, 0);

        // Set the response headers
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=class_details.docx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(docxBytes);
    }
    
}

