package com.quanlithi.service;

import org.springframework.data.domain.Page;

import com.quanlithi.entity.LopThiEntity;

public interface LopThiService {

	LopThiEntity get(long id);
	
	Page<LopThiEntity> search(String idLopThi, 
							String idLopHoc, 
							String idHocPhan, 
							String tenHocPhan, 
							int page, 
							int size);
	
}
