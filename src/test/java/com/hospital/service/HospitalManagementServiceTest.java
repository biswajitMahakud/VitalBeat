package com.hospital.service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import com.hospital.ExceptionHandler.AppointmentConflictException;
import com.hospital.ExceptionHandler.ResourceExistException;
import com.hospital.ExceptionHandler.ResourceNotFoundException;
import com.hospital.dto.AppointmentRequest;
import com.hospital.dto.DoctorInfo;
import com.hospital.dto.PatientAddress;
import com.hospital.dto.PatientInfo;
import com.hospital.entity.Address;
import com.hospital.entity.Appointments;
import com.hospital.entity.Doctor;
import com.hospital.entity.Patient;
import com.hospital.enums.AppointmentStatus;
import com.hospital.enums.Specialization;
import com.hospital.repositories.AppointmentRepository;
import com.hospital.repositories.DoctorRepository;
import com.hospital.repositories.PatientRepository;

public class HospitalManagementServiceTest {

	@Mock
	private DoctorRepository doctorRepositoryMock;

	@Mock
	private PatientRepository patientRepositoryMock;

	@Mock
	private AppointmentRepository appointmentRepositoryMock;

	@InjectMocks
	private HospitalManagementService hospitalManagementService;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}







}
