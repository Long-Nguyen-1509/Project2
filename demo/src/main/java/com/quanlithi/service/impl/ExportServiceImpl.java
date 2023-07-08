package com.quanlithi.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;

import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.TableRowAlign;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFFooter;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageMar;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTabStop;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STSectionMark;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTabJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quanlithi.entity.CaThi;
import com.quanlithi.entity.HocPhanEntity;
import com.quanlithi.entity.KipThi;
import com.quanlithi.entity.LopHocEntity;
import com.quanlithi.entity.LopThiEntity;
import com.quanlithi.entity.LopThiSinhVien;
import com.quanlithi.entity.SinhVienEntity;
import com.quanlithi.repository.KipThiRepo;
import com.quanlithi.repository.LopThiRepo;
import com.quanlithi.repository.LopThiSinhVienRepo;
import com.quanlithi.service.ExportService;

@Service
public class ExportServiceImpl implements ExportService{
	
	@Autowired
	private LopThiRepo lopThiRepo;
	
	@Autowired
	private LopThiSinhVienRepo lopThiSinhVienRepo;
	
	@Autowired
	private KipThiRepo kipThiRepo;

	@Override
	public byte[] exportLopThiInfo(long id, int caThiNumber) {
		LopThiEntity lopThiEntity = lopThiRepo.findById(id)
				.orElseThrow();
		LopHocEntity lopHocEntity = lopThiEntity.getLopHoc();
		HocPhanEntity hocPhanEntity = lopHocEntity.getHocPhan();
		List<CaThi> caThis = lopThiEntity.getListCT();
		
		// .docx
        XWPFDocument document = new XWPFDocument();
        
        CTSectPr sectPr = document.getDocument().getBody().addNewSectPr();
        CTPageMar pageMar = sectPr.addNewPgMar();
        pageMar.setLeft(BigInteger.valueOf(cmToTwips(1)));   
        pageMar.setRight(BigInteger.valueOf(cmToTwips(1)));
        pageMar.setTop(BigInteger.valueOf(cmToTwips(1)));
        pageMar.setBottom(BigInteger.valueOf(cmToTwips(1)));
		for (int c = 0; c < caThis.size(); c++) {
			CaThi caThi = caThis.get(c);
			List<LopThiSinhVien> listLTSV = lopThiSinhVienRepo.findAllByLopThiIdAndCaThiIdOrderByStt(id, caThi.getId());
			

			KipThi kipThi = kipThiRepo.findById(caThi.getKipThi())
					.orElseThrow();
			String gioThi = kipThi.getGioThi();
			
			// bang 1
		    XWPFTable table1 = document.createTable(1, 3);
		    table1.setTableAlignment(TableRowAlign.LEFT);
		    table1.setWidth("100%");
		    table1.removeBorders();
		    float[] table1_width = {4.25f, 10f, 4.25f};
		    setTableColumnsWidth(table1, table1_width);
		    
		    XWPFParagraph para_11 = table1.getRow(0).getCell(0).getParagraphArray(0);
		    para_11.setAlignment(ParagraphAlignment.CENTER);
		    XWPFRun run_11 = para_11.createRun();
		    run_11.setFontSize(13);
		    run_11.setBold(true);
		    run_11.setText("TRƯỜNG ĐHBK HÀ NỘI");
		    run_11.addBreak();
		    run_11.setText("TRƯỜNG CNTT&TT");
		    
		    XWPFParagraph para_12 = table1.getRow(0).getCell(1).getParagraphArray(0);
		    para_12.setAlignment(ParagraphAlignment.CENTER);
		    XWPFRun run_12 = para_12.createRun();
		    run_12.setFontSize(17);
		    run_12.setBold(true);
		    run_12.setText("DANH SÁCH THÍ SINH DỰ THI");
		    run_12.addBreak();
		    run_12.setText("Môn: " + hocPhanEntity.getTenHocPhan() + " " + "(" + hocPhanEntity.getId().toString() + ")");
		    
		    XWPFParagraph para_13 = table1.getRow(0).getCell(2).getParagraphArray(0);
		    para_13.setAlignment(ParagraphAlignment.CENTER);
		    XWPFRun run_13 = para_13.createRun();
		    run_13.setFontSize(13);
		    run_13.setBold(true);
		    run_13.setText("Kíp");
		    run_13.addBreak();
		    run_13.setText("(" + String.valueOf(kipThi.getId()) + ")");
		    
		    document.createParagraph();
		    
		    // bang 2
		    XWPFTable table2 = document.createTable(1, 2);
		    table2.setTableAlignment(TableRowAlign.LEFT);
		    table2.setWidth("100%");
		    table2.removeBorders();
		    float[] table2_width = {9.25f, 9.25f};
		    setTableColumnsWidth(table2, table2_width);
		    
		    XWPFParagraph para_21 = table2.getRow(0).getCell(0).getParagraphArray(0);
		    CTTabStop tabStop_21 = para_21.getCTPPr().addNewTabs().addNewTab();
		    tabStop_21.setVal(STTabJc.LEFT);
		    tabStop_21.setPos(cmToTwips(3f));
		    XWPFRun run_21 = para_21.createRun();
		    run_21.setFontSize(11);
		    run_21.setText("Ngày thi:");
		    run_21.addTab();
		    run_21.setText(caThi.getNgayThi().toString());
		    run_21.addBreak();
		    run_21.setText("Mã lớp thi:");
		    run_21.addTab();
		    run_21.setText(String.valueOf(lopThiEntity.getId()));
		    
		    XWPFParagraph para_22 = table2.getRow(0).getCell(1).getParagraphArray(0);
		    CTTabStop tabStop_22 = para_22.getCTPPr().addNewTabs().addNewTab();
		    tabStop_22.setVal(STTabJc.LEFT);
		    tabStop_22.setPos(cmToTwips(3f));
		    XWPFRun run_22 = para_22.createRun();
		    run_21.setFontSize(11);
		    run_22.setText("Phòng thi:");
		    run_22.addTab();
		    run_22.setText(caThi.getPhongThi());
		    run_22.addBreak();
		    run_22.setText("Kíp thi:");
		    run_22.addTab();
		    run_22.setText(gioThi		);
		    
		    document.createParagraph();
		    
		    // danh sach sinh vien
		    XWPFTable studentTable = document.createTable(listLTSV.size() + 1, 7);	    
		    studentTable.setTableAlignment(TableRowAlign.LEFT);
		    studentTable.setWidth("100%");
		    float[] studentsTable_width = {1.1f, 2.1f, 4.8f, 2.3f, 4.9f, 2.7f, 1.8f};
		    setTableColumnsWidth(studentTable, studentsTable_width);
		   
		    XWPFTableRow headerRow = studentTable.getRow(0);
		    headerRow.getCell(0).setText("PC");
		    headerRow.getCell(1).setText("MSSV");
		    headerRow.getCell(2).setText("Họ và tên");
		    headerRow.getCell(3).setText("Ngày sinh");
		    headerRow.getCell(4).setText("Lớp");
		    headerRow.getCell(5).setText("Điểm / Số câu đúng");
		    headerRow.getCell(6).setText("Ký tên");
		    for (XWPFTableCell cell : headerRow.getTableCells()) {   	
		    	XWPFParagraph tempParagraph = cell.getParagraphArray(0);
		    	tempParagraph.setAlignment(ParagraphAlignment.CENTER);
		    	XWPFRun tempRun = tempParagraph.getRuns().get(0);
		    	tempRun.setBold(true);
		    	tempRun.setFontSize(11);
			}

		    for (int i = 0; i < listLTSV.size(); i++) {	
		        SinhVienEntity sinhVienEntity = listLTSV.get(i).getSinhVien();
		        XWPFTableRow studentRow = studentTable.getRow(i + 1);
		        studentRow.getCell(0).setText(String.valueOf(listLTSV.get(i).getStt()));
		        studentRow.getCell(0).getParagraphArray(0).getRuns().get(0).setFontSize(11);
		        studentRow.getCell(1).setText(String.valueOf(sinhVienEntity.getId()));
		        studentRow.getCell(1).getParagraphArray(0).getRuns().get(0).setFontSize(11);
		        studentRow.getCell(2).setText(sinhVienEntity.getHoTen());
		        studentRow.getCell(2).getParagraphArray(0).getRuns().get(0).setFontSize(11);
		        studentRow.getCell(3).setText(sinhVienEntity.getNgaySinh().toString());
		        studentRow.getCell(3).getParagraphArray(0).getRuns().get(0).setFontSize(11);
		        studentRow.getCell(4).setText(sinhVienEntity.getLopSV());
		        studentRow.getCell(4).getParagraphArray(0).getRuns().get(0).setFontSize(11);
		    }
		    
		    document.createParagraph();
		    
		    // bang 3
		    XWPFTable table3 = document.createTable(1, 2);
		    table3.setTableAlignment(TableRowAlign.LEFT);
		    table3.setWidth("100%");
		    table3.removeBorders();
		    float[] table3_width = {15.25f, 4.34f};
		    setTableColumnsWidth(table3, table3_width);
		    
		    XWPFParagraph para_31 = table3.getRow(0).getCell(0).getParagraphArray(0);
		    XWPFRun run_31 = para_31.createRun();
		    run_31.setFontSize(11);
		    run_31.setText("Tổng số: " 
		    		+ "    " 
		    		+ String.valueOf(listLTSV.size())
		    		+ "    "
		    		+ " Số thí sinh chính thức dự thi"
		    		+ "............." 
		    		+ "Số bài thi: "
		    		+ "......................................");
		    run_31.addBreak();
		    run_31.setText("Các số báo danh vắng: "
		    		+ ".......................................................................................................");
		    
		    XWPFParagraph para_32 = table3.getRow(0).getCell(1).getParagraphArray(0);
		    para_32.setAlignment(ParagraphAlignment.CENTER);
		    XWPFRun run_32 = para_32.createRun();
		    run_32.setFontSize(11);
		    run_32.setText("Cán bộ coi thi");
		    run_32.addBreak();
		    run_32.setText("(Kí, và ghi rõ họ tên)");
		        
		    // tao footer
//	        XWPFHeaderFooterPolicy footerPolicy = document.getHeaderFooterPolicy();
//	        if (footerPolicy == null) {
//	            footerPolicy = document.createHeaderFooterPolicy();
//	        }
//
//	        XWPFFooter footer = footerPolicy.getDefaultFooter();
//	        if (footer == null) {
//	            footer = footerPolicy.createFooter(XWPFHeaderFooterPolicy.DEFAULT);
//	        }
//
//	        XWPFTable footerTable = footer.createTable(1, 3);
//	        footerTable.setTableAlignment(TableRowAlign.CENTER);
//	        footerTable.setWidth("100%");
//	        footerTable.removeBorders();
//	        float[] footerTable_width = {6.5f, 6.5f, 6.5f};
//	        setTableColumnsWidth(footerTable, footerTable_width);
//	        
//	        XWPFParagraph para_foot1 = footerTable.getRow(0).getCell(0).getParagraphArray(0);
//	        para_foot1.setAlignment(ParagraphAlignment.LEFT);
//	        XWPFRun run_footer1 = para_foot1.createRun();
//	        run_footer1.setFontSize(11);
//	        run_footer1.setText("Mã lớp thi: " + String.valueOf(lopThiEntity.getId()));
//	        
//	        XWPFParagraph para_foot2 = footerTable.getRow(0).getCell(1).getParagraphArray(0);
//	        para_foot2.setAlignment(ParagraphAlignment.CENTER);
//	        XWPFRun run_footer2 = para_foot2.createRun();
//	        run_footer2.setFontSize(11);
//	        run_footer2.setText("Phòng thi: " + caThi.getPhongThi());
//	        
//	        XWPFParagraph para_foot3 = footerTable.getRow(0).getCell(2).getParagraphArray(0);
//	        para_foot3.setAlignment(ParagraphAlignment.RIGHT);
//	        XWPFRun run_footer3 = para_foot3.createRun();
//	        run_footer3.setFontSize(11);
//	        run_footer3.setText("Kíp thi: " + gioThi);
	        

		}

        setDefaultFont(document, "Arial");
        setSpacing(document);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			document.write(baos);
			document.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return baos.toByteArray();
	}

	@Override
	public byte[] exportCaThiStatistics(LocalDate date) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] exportLopThiStatistics(LocalDate date) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] exportPhongThiStatistics(LocalDate date) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	public static void setTableColumnsWidth(XWPFTable table, float[] widths) {
//		CTTblGrid grid = table.getCTTbl().addNewTblGrid();
//		for (float w : widths) {
//			grid.addNewGridCol().setW(BigInteger.valueOf(cmToTwips(w)));
//		}    
		for (int i = 0; i < table.getNumberOfRows(); i++) {
            XWPFTableRow row = table.getRow(i);
            for (int j = 0; j < row.getTableCells().size(); j++) {
                XWPFTableCell cell = row.getCell(j);
                CTTblWidth width = cell.getCTTc().addNewTcPr().addNewTcW();
                width.setW(BigInteger.valueOf(cmToTwips(widths[j])));
                width.setType(STTblWidth.DXA);
            }
        }
	}
	
	public static int cmToTwips(float cm) {
	    float twipsFloat = cm * 567f;
	    int twips = Math.round(twipsFloat);
	    return twips;
	}
	
	private static void setDefaultFont(XWPFDocument document, String fontName) {
        for (XWPFParagraph paragraph : document.getParagraphs()) {
            for (XWPFRun run : paragraph.getRuns()) {
				run.setFontFamily(fontName);
			}
        }

        for (XWPFTable table : document.getTables()) {
            for (XWPFTableRow row : table.getRows()) {
                for (XWPFTableCell cell : row.getTableCells()) {
                    for (XWPFParagraph paragraph : cell.getParagraphs()) {
                    	for (XWPFRun run : paragraph.getRuns()) {
            				run.setFontFamily(fontName);
            			}
                    }
                }
            }
        }
    }
	
	public static void setSpacing(XWPFDocument document) {
        for (XWPFParagraph paragraph : document.getParagraphs()) {
            paragraph.setSpacingAfter(0);
        }

        for (XWPFTable table : document.getTables()) {
            for (XWPFTableRow row : table.getRows()) {
                for (XWPFTableCell cell : row.getTableCells()) {
                    for (XWPFParagraph paragraph : cell.getParagraphs()) {
                    	paragraph.setSpacingAfter(0);
                    }
                }
            }
        }  
	}
	
}
