package com.hospital.service;

import com.hospital.ExceptionHandler.AppointmentConflictException;
import com.hospital.ExceptionHandler.ResourceExistException;
import com.hospital.ExceptionHandler.ResourceNotFoundException;
import com.hospital.dto.AppointmentRequest;
import com.hospital.dto.DoctorInfo;
import com.hospital.dto.PatientAddress;
import com.hospital.dto.PatientInfo;
import com.hospital.dto.PatientVitalsHistoryDto;
import com.hospital.dto.SubmitVitalsDto;
import com.hospital.entity.Address;
import com.hospital.entity.Appointments;
import com.hospital.entity.DailyVitalsReport;
import com.hospital.entity.Doctor;
import com.hospital.entity.Patient;
import com.hospital.enums.AppointmentStatus;
import com.hospital.enums.Specialization;
import com.hospital.repositories.AppointmentRepository;
import com.hospital.repositories.DailyVitalsInfoRepository;
import com.hospital.repositories.DoctorRepository;
import com.hospital.repositories.PatientRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class HospitalManagementService {

	// logger
	public static final Logger logger = LoggerFactory.getLogger(HospitalManagementService.class);

	private DoctorRepository doctorRepository;

	private PatientRepository patientRepository;

	private AppointmentRepository appointmentRepository;

	private DailyVitalsInfoRepository vitalsRepository;

	// constructor injection
	public HospitalManagementService(DoctorRepository doctorRepository, PatientRepository patientRepository,
			AppointmentRepository appointmentRepository, DailyVitalsInfoRepository vitalsRepository) {
		this.doctorRepository = doctorRepository;
		this.patientRepository = patientRepository;
		this.appointmentRepository = appointmentRepository;
		this.vitalsRepository = vitalsRepository;
	}

	// for testing purpose....

}
