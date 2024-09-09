package com.quanlithi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.quanlithi.entity.RoleEntity;

public interface RoleRepo extends JpaRepository<RoleEntity, Long> {

}
