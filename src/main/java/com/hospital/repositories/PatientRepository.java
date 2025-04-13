package com.hospital.repositories;

import com.hospital.entity.Patient;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {
	Optional<Patient> findByEmail(String emailId);
}
