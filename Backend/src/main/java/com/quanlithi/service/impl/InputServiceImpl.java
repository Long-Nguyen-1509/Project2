package com.quanlithi.service.impl;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.quanlithi.entity.CaThi;
import com.quanlithi.entity.HocPhanEntity;
import com.quanlithi.entity.LopHocEntity;
import com.quanlithi.entity.LopThiEntity;
import com.quanlithi.entity.LopThiSinhVien;
import com.quanlithi.entity.SinhVienEntity;
import com.quanlithi.entity.UserEntity;
import com.quanlithi.repository.CaThiRepo;
import com.quanlithi.repository.HocPhanRepo;
import com.quanlithi.repository.LopHocRepo;
import com.quanlithi.repository.LopThiRepo;
import com.quanlithi.repository.LopThiSinhVienRepo;
import com.quanlithi.repository.RoleRepo;
import com.quanlithi.repository.SinhVienRepo;
import com.quanlithi.repository.UserRepo;
import com.quanlithi.service.InputService;

@Service
public class InputServiceImpl implements InputService{
	
	@Autowired
	private HocPhanRepo hocPhanRepo;
	
	@Autowired
	private LopHocRepo lopHocRepo;
	
	@Autowired
	private LopThiRepo lopThiRepo;
	
	@Autowired
	private SinhVienRepo sinhVienRepo;
	
	@Autowired
	private CaThiRepo caThiRepo;
	
	@Autowired
	private LopThiSinhVienRepo lopThiSinhVienRepo;
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private RoleRepo roleRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	private final DateTimeFormatter dateTimeFormatter  = DateTimeFormatter.ofPattern("MM/dd/yyyy");
	
	@Override
	public void inputAllFiles(List<MultipartFile> files) throws EncryptedDocumentException, IOException {
		for (MultipartFile file : files) {
			List<HocPhanEntity> hocPhanEntities = new ArrayList<>();
			List<LopHocEntity> lopHocEntities = new ArrayList<>();
			List<LopThiEntity> lopThiEntities = new ArrayList<>();
			List<SinhVienEntity> sinhVienEntities = new ArrayList<>();
			List<CaThi> caThis = new ArrayList<>();
			List<LopThiSinhVien> lopThiSinhViens = new ArrayList<>();
			List<UserEntity> userEntities = new ArrayList<>();
			
			List<List<String>> dataLists = readExcelFile(file);
			int batchSize = 200;
			HocPhanEntity lastHP = null;
			LopHocEntity lastLH = null;
			LopThiEntity lastLT = null; 
			CaThi lastCT = null;
			for (int i = 0; i < dataLists.size(); i++) {
				List<String> data = dataLists.get(i);
				
				if (lastHP == null || lastHP.getId() != data.get(3)) {
					HocPhanEntity tempHP = HocPhanEntity.builder()
											.id(data.get(3))
											.tenHocPhan(data.get(4))
											.build();
					lastHP = tempHP;
					hocPhanEntities.add(lastHP);
				}
				
				if (lastLH == null || lastLH.getId() != Long.parseLong(data.get(2))) {
					LopHocEntity tempLH = LopHocEntity.builder()
											.id(Long.parseLong(data.get(2)))
											.hocPhan(lastHP)
											.khoaHoc(data.get(6))
											.kiHoc(data.get(12))
											.build();
					lastLH = tempLH;
					lopHocEntities.add(lastLH);
				}
				
				if (lastLT == null || lastLT.getId() != Long.parseLong(data.get(0))) {
					LopThiEntity tempLT = LopThiEntity.builder()
											.id(Long.parseLong(data.get(0)))
											.lopHoc(lastLH)
											.nhomThiId(Integer.parseInt(data.get(15)))
											.nhomThi(data.get(14))
											.ngayThi(LocalDate.parse(data.get(17), dateTimeFormatter))
											.build();
					lastLT = tempLT;
				}
				
				SinhVienEntity tempSV = SinhVienEntity.builder()
											.id(Long.parseLong(data.get(8)))
											.hoTen(data.get(9))
											.ngaySinh(LocalDate.parse(data.get(10), dateTimeFormatter))
											.lopSV(data.get(11))
											.email(data.get(23))
											.build();
				
				if (lastCT == null || lastCT.getId() != Long.parseLong(data.get(27))) {
					CaThi tempCT = CaThi.builder()
									.id(Long.parseLong(data.get(27)))
									.kipThi(Integer.parseInt(data.get(26)))
									.phongThi(data.get(18))
									.ngayThi(LocalDate.parse(data.get(17), dateTimeFormatter))
									.build();
					lastCT = tempCT;
					caThis.add(lastCT);
				}
				
				LopThiSinhVien tempLTSV = LopThiSinhVien.builder()
											.caThi(lastCT)
											.lopThi(lastLT)
											.sinhVien(tempSV)
											.stt(Integer.parseInt(data.get(7)))
											.build();
				
				UserEntity tempUser = UserEntity.builder()
											.id(Integer.parseInt(data.get(8)))
											.fullName(data.get(9))
											.username(data.get(23))
											.password(passwordEncoder.encode(data.get(8)))
											.build();
				
				tempUser.getRoles().add(roleRepo.findById(3L).orElseThrow());
				Optional<SinhVienEntity> sinhVienOptional = sinhVienRepo.findById(tempSV.getId());
				if (sinhVienOptional.isPresent()) {
					tempSV = sinhVienOptional.get();
					tempSV.getListLH().add(lastLH);
				}else {
					tempSV.getListLH().add(lastLH);
				}	
				
				if (lastLT.getListCT().size() == 0) {
					lastLT.getListCT().add(lastCT);
					lopThiEntities.add(lastLT);
				}else {
					int j = lastLT.getListCT().size() - 1;
					if (j >= 0) {
						if (lastLT.getListCT().get(j).getId() != lastCT.getId()) {
							lopThiEntities.remove(lastLT);
							lastLT.getListCT().add(lastCT);
							lopThiEntities.add(lastLT);
						}
					}
				}
				
//				if (lastLT.getListCT().size() == 0) {
//					lastCT.getListLT().add(lastLT);
//					caThis.add(lastCT);
//				}else {
//					int j = lastCT.getListLT().size() - 1;
//					if (j > 0) {
//						if (lastCT.getListLT().get(j).getId() != lastLT.getId()) {
//							caThis.remove(lastCT);
//							lastCT.getListLT().add(lastLT);
//						}
//					}
//				}
				
				sinhVienEntities.add(tempSV);
				lopThiSinhViens.add(tempLTSV);
				userEntities.add(tempUser);		
				if (i % batchSize == batchSize-1) {
					save(hocPhanEntities, lopHocEntities, lopThiEntities, sinhVienEntities, caThis, lopThiSinhViens, userEntities);
					sinhVienEntities.clear();
					hocPhanEntities.clear();
					lopHocEntities.clear();
					lopThiEntities.clear();
					caThis.clear();
					lopThiSinhViens.clear();
					userEntities.clear();
				}
			}
			
			if (!sinhVienEntities.isEmpty()) {
				save(hocPhanEntities, lopHocEntities, lopThiEntities, sinhVienEntities, caThis, lopThiSinhViens, userEntities);
				sinhVienEntities.clear();
				hocPhanEntities.clear();
				lopHocEntities.clear();
				lopThiEntities.clear();
				caThis.clear();
				lopThiSinhViens.clear();
				userEntities.clear();
			}
		}
	}

	public List<List<String>> readExcelFile(MultipartFile file) throws EncryptedDocumentException, IOException {	
		XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);
        List<List<String>> dataList = new ArrayList<>();
        for (Row row : sheet) {
        	if (row.getRowNum() == 0) {
				continue;
			}
            List<String> rowData = new ArrayList<>();
            for (Cell cell : row) {
            	if (cell != null) {
                    switch (cell.getCellType()) {
                        case STRING:
                            rowData.add(cell.getStringCellValue()) ;
                            break;
                        case NUMERIC:
                            if (DateUtil.isCellDateFormatted(cell)) {
                            	Date dateValue = cell.getDateCellValue();
                                LocalDate localDateValue = dateValue.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                                rowData.add(localDateValue.format(dateTimeFormatter));
                            } else {
                                rowData.add(Integer.toString((int) cell.getNumericCellValue()));
                            }
                            break;
                        default:
                            rowData.add("");
                    }
                }
            }
            if (!rowData.isEmpty()) {
                dataList.add(rowData);
            }
        }
        workbook.close();
        return dataList;
	}
	
	public void save(List<HocPhanEntity> hocPhanEntities,
					List<LopHocEntity> lopHocEntities,
					List<LopThiEntity> lopThiEntities,
					List<SinhVienEntity> sinhVienEntities,
					List<CaThi> caThis,
					List<LopThiSinhVien> lopThiSinhViens,
					List<UserEntity> userEntities) {
		hocPhanRepo.saveAll(hocPhanEntities);
		lopHocRepo.saveAll(lopHocEntities);
		lopThiRepo.saveAll(lopThiEntities);
		sinhVienRepo.saveAll(sinhVienEntities);
		caThiRepo.saveAll(caThis);
		lopThiSinhVienRepo.saveAll(lopThiSinhViens);
		userRepo.saveAll(userEntities);
	}
}
