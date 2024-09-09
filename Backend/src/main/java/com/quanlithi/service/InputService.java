package com.quanlithi.service;

import java.io.IOException;
import java.util.List;

import org.apache.poi.EncryptedDocumentException;
import org.springframework.web.multipart.MultipartFile;

public interface InputService {
	void inputAllFiles(List<MultipartFile> files) throws EncryptedDocumentException, IOException;
	
}
