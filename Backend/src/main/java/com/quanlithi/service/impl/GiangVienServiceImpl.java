package com.quanlithi.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quanlithi.entity.GiangVienEntity;
import com.quanlithi.repository.GiangVienRepo;
import com.quanlithi.service.GiangVienService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class GiangVienServiceImpl implements GiangVienService{

	@Autowired
	private GiangVienRepo giangVienRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public GiangVienEntity get(long id) {
		GiangVienEntity giangVienEntity = giangVienRepo.findById(id)
				.orElseThrow(() -> new EntityNotFoundException());
		return giangVienEntity;
	}

}
