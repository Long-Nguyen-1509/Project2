package com.quanlithi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quanlithi.entity.LopHocEntity;
import com.quanlithi.repository.LopHocRepo;
import com.quanlithi.service.LopHocService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class LopHocServiceImpl implements LopHocService{

	@Autowired
	private LopHocRepo lopHocRepo;

	@Override
	public LopHocEntity get(long id) {
		LopHocEntity lopHoc = lopHocRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException());
		return lopHoc;
	}
}
