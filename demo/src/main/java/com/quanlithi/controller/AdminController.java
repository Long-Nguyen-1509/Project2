package com.quanlithi.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.poi.EncryptedDocumentException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import com.quanlithi.dto.base.LopThiCompact;
import com.quanlithi.entity.LopHocEntity;
import com.quanlithi.entity.LopThiEntity;
import com.quanlithi.service.ExportService;
import com.quanlithi.service.InputService;
import com.quanlithi.service.LopThiService;
import com.quanlithi.service.SinhVienService;

@RestController
@RequestMapping("/api/auth/admin")
public class AdminController {
	
	@Autowired
	private SinhVienService sinhVienService;
	
	@Autowired
	private LopThiService lopThiService;
	
	@Autowired
	private InputService inputService;
	
	@Autowired
	private ExportService exportService;
	
	@Autowired
	private ModelMapper modelMapper;
	
//	@GetMapping("/search/sv")
//	public String searchSinhVienEntitys(
//	        @RequestParam(value = "query", required = false) String query,
//	        @RequestParam(value = "page", defaultValue = "0") int page,
//	        @RequestParam(value = "size", defaultValue = "10") int size,
//	        Model model
//	) {
//	    Page<SinhVienEntity> sinhVienPage = sinhVienService.search(query, page, size);
//
//	    List<SinhVienDTO> sinhVienDTOs = sinhVienPage.getContent().stream()
//	            .map(sv -> convertToDTOWithLinks(sv))
//	            .collect(Collectors.toList());
//
//	    // Get pagination information
//	    int totalPages = sinhVienPage.getTotalPages();
//	    int currentPage = sinhVienPage.getNumber();
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
//	    model.addAttribute("students", sinhVienDTOs);
//	    model.addAttribute("totalPages", totalPages);
//	    model.addAttribute("currentPage", currentPage);
//	    model.addAttribute("isFirstPage", isFirstPage);
//	    model.addAttribute("isLastPage", isLastPage);
//	    model.addAttribute("previousPage", previousPage);
//	    model.addAttribute("nextPage", nextPage);
//	    model.addAttribute("pageNumbers", pageNumbers);
//
//	    return "student-search";
//	}
	
//	private SinhVienDTO convertToDTOWithLinks(SinhVienEntity sinhVienEntity) {
//        SinhVienDTO sinhVienDTO = modelMapper.map(sinhVienEntity, SinhVienDTO.class);
//        sinhVienDTO.setLink(Link.of("/api/stinfo/" + sinhVienEntity.getId()).withSelfRel());
//        return sinhVienDTO;
//    }
	
	@GetMapping("/exams")
	public String searchLopThi(
	        @RequestParam(value = "idLopThi", required = false) String idLopThi,
	        @RequestParam(value = "idLopHoc", required = false) String idLopHoc,
	        @RequestParam(value = "idHocPhan", required = false) String idHocPhan,
	        @RequestParam(value = "tenHocPhan", required = false) String tenHocPhan,
	        @RequestParam(value = "page", defaultValue = "0") int page,
	        @RequestParam(value = "size", defaultValue = "10") int size,
	        Model model
	) {
	    Page<LopThiEntity> lopThiPage = lopThiService.search(idLopThi, idLopHoc, idHocPhan, tenHocPhan, page - 1, size);

	    List<LopThiCompact> lopThiSearchResults = lopThiPage.getContent().stream()
	            .map(lt -> modelMapper.map(lt, LopThiCompact.class))
	            .collect(Collectors.toList());
	    for (LopThiCompact lopThiCompact : lopThiSearchResults) {
			Link link = WebMvcLinkBuilder.linkTo(InfoAPI.class)
					.slash("lh")
					.slash(lopThiCompact.getId())
					.withSelfRel();
			lopThiCompact.add(link);
		}
	    List<Link> pagesLinks = generatePaginationLinks(lopThiPage, idLopThi, idLopHoc, idHocPhan, tenHocPhan);

	    model.addAttribute("lopthi", lopThiSearchResults);
	    model.addAttribute(pagesLinks);
	    

	    return "lopthi-search";
	}
	
	private List<Link> generatePaginationLinks(Page<LopThiEntity> lopThiPage,
			String idLopThi, 
			String idLopHoc, 
			String idHocPhan,
			String tenHocPhan){
		int currentPage = lopThiPage.getNumber();
	    int totalPages = lopThiPage.getTotalPages();
	    int size = lopThiPage.getSize();

	    List<Link> pageLinks = new ArrayList<>();

	    if (lopThiPage.hasPrevious()) {
	        Link prevLink = buildPageLink(idLopThi, idLopHoc, idHocPhan, tenHocPhan, currentPage - 1, size);
	        pageLinks.add(prevLink);
	    }

	    int startPage = Math.max(currentPage - 2, 0);
	    int endPage = Math.min(currentPage + 2, totalPages - 1);
	    for (int page = startPage; page <= endPage; page++) {
	        Link pageLink = buildPageLink(idLopThi, idLopHoc, idHocPhan, tenHocPhan, page, size);
	        pageLinks.add(pageLink);
	    }

	    if (lopThiPage.hasNext()) {
	        Link nextLink = buildPageLink(idLopThi, idLopHoc, idHocPhan, tenHocPhan, currentPage + 1, size);
	        pageLinks.add(nextLink);
	    }

	    return pageLinks;
	}
	
	private Link buildPageLink(
			String idLopThi, 
			String idLopHoc, 
			String idHocPhan,
			String tenHocPhan,
			int page,
			int size) {
		return Link.of(
				UriComponentsBuilder.fromPath("/api/admin/exams")
                .queryParam("idLopThi", idLopThi)
                .queryParam("idLopHoc", idLopHoc)
                .queryParam("idHocPhan", idHocPhan)
                .queryParam("tenHocPhan", tenHocPhan)
                .queryParam("page", page)
                .queryParam("size", size)
                .toUriString()
                )
				.withSelfRel();
	}
	
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
    
    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadClassDetails(@RequestParam("id") long id) throws IOException {
    	
    	LopThiEntity lopThiEntity = lopThiService.get(id);
    	LopHocEntity lopHocEntity = lopThiEntity.getLopHoc();
        byte[] docxBytes = exportService.exportLopThiInfo(id, 0);

        // Set the response headers
        HttpHeaders headers = new HttpHeaders();
        String fileName = lopHocEntity.getHocPhan().getId() 
        		+ "_" + String.valueOf(lopThiEntity.getId())
        		+ "_" + String.valueOf(lopHocEntity.getKiHoc())
        		+ ".docx";
        headers.setContentDispositionFormData("attachment" , fileName);

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(docxBytes);
    }
}
