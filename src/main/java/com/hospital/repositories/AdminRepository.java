package com.hospital.repositories;

import com.hospital.entity.Admin;


import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {
	
}
