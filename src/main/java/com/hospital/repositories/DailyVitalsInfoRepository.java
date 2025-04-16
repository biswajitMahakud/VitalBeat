package com.hospital.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hospital.entity.DailyVitalsReport;

@Repository
public interface DailyVitalsInfoRepository extends JpaRepository<DailyVitalsReport, Long>{
	List<DailyVitalsReport> findAllByPatientId(Long patientId);
}
