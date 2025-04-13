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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import com.hospital.ExceptionHandler.ResourceExistException;
import com.hospital.ExceptionHandler.ResourceNotFoundException;
import com.hospital.dto.DoctorInfo;
import com.hospital.dto.PatientAddress;
import com.hospital.dto.PatientInfo;
import com.hospital.entity.Address;
import com.hospital.entity.Doctor;
import com.hospital.entity.Patient;
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

	@Test
	public void addDoctorWithValidPropertiesTest() {
		DoctorInfo doctorInfo = DoctorInfo.builder().name("Rahul Tewatia").email("rahul23@gmail.com")
				.password("rahul23@").specialization("DENTIST").build();

		Doctor doctor = Doctor.builder().doctorId(1L).name(doctorInfo.getName()).email(doctorInfo.getEmail())
				.password(doctorInfo.getPassword())
				.specialization(Specialization.valueOf(doctorInfo.getSpecialization())).build();

		// stubbing
		when(doctorRepositoryMock.save(any(Doctor.class))).thenReturn(doctor);

		ResponseEntity<String> response = hospitalManagementService.addDoctor(doctorInfo);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Doctor Added Successfully! Your id is: 1", response.getBody());
		verify(doctorRepositoryMock, times(1)).save(any(Doctor.class));
	}

	@Test
	public void addDoctorWithInvalidPropertiesTest() {
		DoctorInfo doctorInfo = DoctorInfo.builder().email("rahul23@gmail.com").specialization("DENTIST").build();

		doThrow(new DataIntegrityViolationException("Invalid properties")).when(doctorRepositoryMock)
				.save(any(Doctor.class));
		doThrow(new NullPointerException("null doctor can't be stored!")).when(doctorRepositoryMock).save(null);

		assertAll("Invalid Properties Test",
				() -> assertThrows(DataIntegrityViolationException.class,
						() -> hospitalManagementService.addDoctor(doctorInfo)),

				() -> assertThrows(NullPointerException.class, () -> hospitalManagementService.addDoctor(null)));
	}

	// This following test describes, if Doctor obj. has invalid specialization
	// then, It should throw IllegalArgumentException.class
	@Test
	public void addDoctorWithInvalidSpecializationTest() {
		DoctorInfo doctorInfo = DoctorInfo.builder().name("Rahul Tewatia").email("rahul23@gmail.com")
				.password("rahul23@").specialization("Invalid_Specialization").build();

		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> hospitalManagementService.addDoctor(doctorInfo));
		assertEquals("Input specialization isn't matched with declared specialization: Invalid_Specialization",
				exception.getMessage());
		verify(doctorRepositoryMock, never()).save(any(Doctor.class));
	}

	// getting doctors details with valid id test
	@Test
	public void getDoctorByValidIdTest() {
		Doctor doctor = Doctor.builder().name("Mr. Srujan Singh").password("1234").doctorId(2L)
				.email("srujan23@gmail.com").specialization(Specialization.valueOf("DENTIST")).build();

		when(doctorRepositoryMock.findById(2L)).thenReturn(Optional.of(doctor));
		ResponseEntity<DoctorInfo> doctorInfo = hospitalManagementService.getDoctorById(2L);
		assertEquals(HttpStatus.OK, doctorInfo.getStatusCode());
		assertEquals("Mr. Srujan Singh", doctorInfo.getBody().getName());
		assertEquals("srujan23@gmail.com", doctorInfo.getBody().getEmail());
		assertEquals("DENTIST", doctorInfo.getBody().getSpecialization());
		verify(doctorRepositoryMock, atLeastOnce()).findById(2L);
	}

	// getting ResourceNotFoundException when getDoctorById(wrong_id) is called test
	// method
	@Test
	public void exceptionThrowsGetDoctorByInvalidIdTest() {
		when(doctorRepositoryMock.findById(3L)).thenReturn(Optional.empty());
		ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
				() -> hospitalManagementService.getDoctorById(3L));
		assertEquals("Doctor isn't found with id: 3", exception.getMessage());
		verify(doctorRepositoryMock, atLeastOnce()).findById(3L);
	}

	@Test
	public void addPatientWithvalidPatientDetailsTest() {
		PatientAddress patientAddress = PatientAddress.builder().city("New Delhi").pincode("896354").state("Delhi")
				.build();

		Address address = Address.builder().city(patientAddress.getCity()).pincode(patientAddress.getPincode())
				.state(patientAddress.getState()).build();

		PatientInfo patientInfo = PatientInfo.builder().patientId(101L).name("Mr. Harikrishna Pradhan")
				.email("harikrishna23@gmail.com").password("1234").address(patientAddress).build();

		Patient patient = Patient.builder().patientId(patientInfo.getPatientId()).name(patientInfo.getName())
				.email(patientInfo.getEmail()).password(patientInfo.getPassword()).patientAddress(address).build();

		when(patientRepositoryMock.save(any(Patient.class))).thenReturn(patient);
		when(patientRepositoryMock.findByEmail("harikrishna23@gmail.com")).thenReturn(Optional.empty());
		ResponseEntity<String> response = hospitalManagementService.addPatient(patientInfo);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Patient Added Successfully! Your id is: 101", response.getBody());
		verify(patientRepositoryMock, atLeastOnce()).save(any(Patient.class));
		verify(patientRepositoryMock, times(1)).findByEmail("harikrishna23@gmail.com");
	}

	@Test
	public void addPatientWithInvalidPatientDetailsTest() {
		PatientAddress patientAddress = PatientAddress.builder().city("New Delhi").pincode("896354").state("Delhi")
				.build();

		Address address = Address.builder().city(patientAddress.getCity()).pincode(patientAddress.getPincode())
				.state(patientAddress.getState()).build();

		PatientInfo patientInfo = PatientInfo.builder().patientId(101L).address(patientAddress).build();
		
		Patient patient = Patient.builder().patientId(patientInfo.getPatientId()).patientAddress(address).build();

		doThrow(DataIntegrityViolationException.class).when(patientRepositoryMock).save(any(Patient.class));
		DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class, () -> hospitalManagementService.addPatient(patientInfo));
		assertEquals("Address, email, name, password can't be null!", exception.getMessage());
		verify(patientRepositoryMock, never()).save(any(Patient.class));
	}

	@Test
	public void addNullPatientTest() {
		NullPointerException exception = assertThrows(NullPointerException.class,
				() -> hospitalManagementService.addPatient(null));
		assertEquals("Patient can't be null!", exception.getMessage());
	}
	
	@Test
	public void addPatientWhichIsAlreadyAvailableExceptionTest() {
		PatientAddress patientAddress = PatientAddress.builder().city("New Delhi").pincode("896354").state("Delhi")
				.build();

		Address address = Address.builder().city(patientAddress.getCity()).pincode(patientAddress.getPincode())
				.state(patientAddress.getState()).build();

		PatientInfo patientInfo = PatientInfo.builder().patientId(101L).name("Mr. Sudhakar Sharama")
				.email("sudhakar34@gmail.com").password("1234").address(patientAddress).build();

		Patient patient = Patient.builder().patientId(patientInfo.getPatientId()).name(patientInfo.getName())
				.email(patientInfo.getEmail()).password(patientInfo.getPassword()).patientAddress(address).build();
		
		when(patientRepositoryMock.findByEmail("sudhakar34@gmail.com")).thenReturn(Optional.of(patient));
		
		ResourceExistException exception = assertThrows(ResourceExistException.class, ()-> hospitalManagementService.addPatient(patientInfo));
		
		assertEquals("Patient already exist with email id: sudhakar34@gmail.com", exception.getMessage());
		verify(patientRepositoryMock, times(1)).findByEmail("sudhakar34@gmail.com");
	}
	
	
	

}
