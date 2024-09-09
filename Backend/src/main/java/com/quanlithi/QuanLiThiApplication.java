package com.quanlithi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.quanlithi.entity.KipThi;
import com.quanlithi.entity.RoleEntity;
import com.quanlithi.repository.KipThiRepo;
import com.quanlithi.repository.RoleRepo;
import com.quanlithi.repository.SinhVienRepo;

@SpringBootApplication
@ComponentScan("com")
public class QuanLiThiApplication implements CommandLineRunner{
	
	public static void main(String[] args) {

		SpringApplication.run(QuanLiThiApplication.class, args);
		
		
	}
	
	@Autowired
	private SinhVienRepo sinhVienRepo;
	
	@Autowired
	private RoleRepo roleRepo;
	
	@Autowired
	private KipThiRepo kipThiRepo;
	
	@Override
	public void run(String... args) throws Exception {
//		RoleEntity role1 = RoleEntity.builder()
//								.name("ADMIN")
//								.build();
//		RoleEntity role2 = RoleEntity.builder()
//								.name("GV")
//								.build();
//		RoleEntity role3 = RoleEntity.builder()
//								.name("SV")
//								.build();
//		roleRepo.save(role1);
//		roleRepo.save(role2);
//		roleRepo.save(role3);
//		
//		for (int i = 0; i < 10; i++) {
//			KipThi kipThi = KipThi.builder()
//								.gioThi("111")
//								.build();
//			kipThiRepo.save(kipThi);
//		}
	}

}
