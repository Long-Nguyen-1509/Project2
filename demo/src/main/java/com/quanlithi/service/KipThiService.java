package com.quanlithi.service;

import java.util.List;

import com.quanlithi.entity.KipThi;

public interface KipThiService {
	KipThi get(int id);
	
	List<String> getAllGioThi();
}
