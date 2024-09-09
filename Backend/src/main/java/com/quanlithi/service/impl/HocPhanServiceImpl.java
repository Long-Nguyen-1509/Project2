package com.quanlithi.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quanlithi.entity.HocPhanEntity;
import com.quanlithi.repository.HocPhanRepo;
import com.quanlithi.service.HocPhanService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class HocPhanServiceImpl implements HocPhanService{

	@Autowired
	HocPhanRepo hocPhanRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public HocPhanEntity get(String id) {
		HocPhanEntity hocPhan = hocPhanRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException());
		return hocPhan;
	}

}
