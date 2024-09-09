package com.quanlithi.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quanlithi.entity.PhongThi;
import com.quanlithi.repository.PhongThiRepo;
import com.quanlithi.service.PhongThiService;

@Service
public class PhongThiServiceImpl implements PhongThiService{

	@Autowired
	private PhongThiRepo phongThiRepo;
	
	@Override
	public List<String> getAllNameOrdered() {
		List<PhongThi> phongThi = phongThiRepo.findAll();
		List<String> list = phongThi
				.stream()
				.map(c -> c.getId())
				.collect(Collectors.toList());
		Collections.sort(list);
		return list;
		
	}
	
}
