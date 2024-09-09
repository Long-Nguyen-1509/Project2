package com.quanlithi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.quanlithi.entity.LopThiEntity;
import com.quanlithi.repository.LopThiRepo;
import com.quanlithi.service.LopThiService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class LopThiServiceImpl implements LopThiService{

	@Autowired
	private LopThiRepo lopThiRepo;

	@Override
	public LopThiEntity get(long id) {
		LopThiEntity lopThi = lopThiRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException());
		return lopThi;	
	}

	@Override
	public Page<LopThiEntity> search(String idLopThi, 
									String idLopHoc, 
									String idHocPhan, 
									String tenHocPhan, 
									int page,
									int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
		return lopThiRepo.search(idLopThi, idLopHoc, idHocPhan, tenHocPhan, pageable);
	}

}
