package com.quanlithi.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quanlithi.entity.KipThi;
import com.quanlithi.repository.KipThiRepo;
import com.quanlithi.service.KipThiService;

@Service
public class KipThiServiceImpl implements KipThiService{

	@Autowired
	private KipThiRepo kipThiRepo;
	
	@Override
	public KipThi get(int id) {
		KipThi kipThi = kipThiRepo.findById(id)
				.orElseThrow();
		return kipThi;
	}

	@Override
	public List<String> getAllGioThi() {
		return kipThiRepo.findAll()
				.stream()
				.map(c -> c.getGioThi())
				.collect(Collectors.toList());
	}
	
}
