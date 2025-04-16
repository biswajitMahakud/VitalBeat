package com.hospital.service;

import com.hospital.ExceptionHandler.AppointmentConflictException;
import com.hospital.ExceptionHandler.ResourceExistException;
import com.hospital.ExceptionHandler.ResourceNotFoundException;
import com.hospital.dto.AppointmentRequest;
import com.hospital.dto.PatientAddress;
import com.hospital.dto.PatientInfo;
import com.hospital.entity.Address;
import com.hospital.entity.Appointments;
import com.hospital.entity.Doctor;
import com.hospital.entity.Patient;
import com.hospital.enums.AppointmentStatus;
import com.hospital.repositories.AppointmentRepository;
import com.hospital.repositories.DoctorRepository;
import com.hospital.repositories.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PatientManagementServiceTest {
    @Mock
    private DoctorRepository doctorRepositoryMock;

    @Mock
    private PatientRepository patientRepositoryMock;

    @Mock
    private AppointmentRepository appointmentRepositoryMock;

    @InjectMocks
    private PatientManagementService patientManagementService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // add patient test with valid patient details
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
        ResponseEntity<String> response = patientManagementService.registerPatient(patientInfo);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Patient Added Successfully! Your id is: 101", response.getBody());
        verify(patientRepositoryMock, atLeastOnce()).save(any(Patient.class));
        verify(patientRepositoryMock, times(1)).findByEmail("harikrishna23@gmail.com");
    }

    // add patient test with Invalid patient details
    @Test
    public void addPatientWithInvalidPatientDetailsTest() {
        PatientAddress patientAddress = PatientAddress.builder().city("New Delhi").pincode("896354").state("Delhi")
                .build();

        Address address = Address.builder().city(patientAddress.getCity()).pincode(patientAddress.getPincode())
                .state(patientAddress.getState()).build();

        PatientInfo patientInfo = PatientInfo.builder().patientId(101L).address(patientAddress).build();

        Patient patient = Patient.builder().patientId(patientInfo.getPatientId()).patientAddress(address).build();

        doThrow(DataIntegrityViolationException.class).when(patientRepositoryMock).save(any(Patient.class));
        DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class,
                () -> patientManagementService.registerPatient(patientInfo));
        assertEquals("Address, email, name, password can't be null!", exception.getMessage());
        verify(patientRepositoryMock, never()).save(any(Patient.class));
    }

    // NPE test with null value
    @Test
    public void addNullPatientTest() {
        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> patientManagementService.registerPatient(null));
        assertEquals("Patient can't be null!", exception.getMessage());
    }

    // Test add patient which is already available their email in db
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

        ResourceExistException exception = assertThrows(ResourceExistException.class,
                () -> patientManagementService.registerPatient(patientInfo));

        assertEquals("Patient already exist with email id: sudhakar34@gmail.com", exception.getMessage());
        verify(patientRepositoryMock, times(1)).findByEmail("sudhakar34@gmail.com");
    }

    // getPatient with valid id test
    @Test
    public void getPatientByValidIdTest() {
        PatientAddress patientAddress = PatientAddress.builder().city("New Delhi").pincode("896354").state("Delhi")
                .build();

        Address address = Address.builder().city(patientAddress.getCity()).pincode(patientAddress.getPincode())
                .state(patientAddress.getState()).build();

        Patient patient = Patient.builder().patientId(101L).name("Mr. Sudhakar Sharma").email("sudhakar34@gmail.com")
                .password("1234").patientAddress(address).build();

        PatientInfo patientInfo = PatientInfo.builder().patientId(patient.getPatientId()).name(patient.getName())
                .email(patient.getEmail()).password(patient.getPassword()).address(patientAddress).build();

        when(patientRepositoryMock.findById(101L)).thenReturn(Optional.of(patient));
        ResponseEntity<PatientInfo> response = patientManagementService.getPatient(101L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Mr. Sudhakar Sharma", response.getBody().getName());
        assertEquals("sudhakar34@gmail.com", response.getBody().getEmail());
        assertEquals("New Delhi", response.getBody().getAddress().getCity());
        assertEquals("896354", response.getBody().getAddress().getPincode());
        assertEquals("Delhi", response.getBody().getAddress().getState());
        verify(patientRepositoryMock, times(1)).findById(101L);

    }

    // exception test for invalid patientId...
    @Test
    public void getPatientByInvalidIdTest() {
        doThrow(new ResourceNotFoundException("Patient is not found with id: 2")).when(patientRepositoryMock)
                .findById(2L);
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> patientManagementService.getPatient(2L));
        assertEquals("Patient is not found with id: 2", exception.getMessage());
        verify(patientRepositoryMock, times(1)).findById(2L);
    }

    // valid appointment time slot test...
    @Test
    public void shouldBookAppointmentWhenTimeSlotIsAvailableTest() {
        LocalDateTime fixedNow = LocalDateTime.of(2025, 4, 16, 10, 0);

        AppointmentRequest appointmentRequest = AppointmentRequest.builder().patientId(5L).appointmentTime(fixedNow)
                .doctorId(102L).build();

        Patient patient = Patient.builder().build();
        Doctor doctor = Doctor.builder().build();
        when(patientRepositoryMock.findById(appointmentRequest.getPatientId())).thenReturn(Optional.of(patient));
        when(doctorRepositoryMock.findById(appointmentRequest.getDoctorId())).thenReturn(Optional.of(doctor));

        LocalDateTime slotStart = appointmentRequest.getAppointmentTime().minusMinutes(15L);
        LocalDateTime slotEnd = appointmentRequest.getAppointmentTime().plusMinutes(15L);

        when(appointmentRepositoryMock.findByDoctorAndAppointmentTimeBetween(doctor, slotStart, slotEnd))
                .thenReturn(List.of());

        Appointments appointment = Appointments.builder().appointmentId(10L)
                .appointmentTime(appointmentRequest.getAppointmentTime()).doctor(doctor).patient(patient)
                .status(AppointmentStatus.valueOf("SCHEDULED")).build();

        when(appointmentRepositoryMock.save(any(Appointments.class))).thenReturn(appointment);

        ResponseEntity<String> response = patientManagementService.takeDoctorAppointment(appointmentRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Your Appointment is Booked and your Appointment id is: 10", response.getBody());
        verify(patientRepositoryMock, times(1)).findById(appointmentRequest.getPatientId());
        verify(doctorRepositoryMock, times(1)).findById(appointmentRequest.getDoctorId());
        verify(appointmentRepositoryMock, times(1)).findByDoctorAndAppointmentTimeBetween(doctor, slotStart, slotEnd);
        verify(appointmentRepositoryMock, times(1)).save(any(Appointments.class));
    }

    @Test
    // when patient is not Present in db...
    public void shouldReturnExceptionWhenPatientIsNotFoundTest() {
        LocalDateTime fixedNow = LocalDateTime.of(2025, 4, 16, 10, 0);

        AppointmentRequest appointmentRequest = AppointmentRequest.builder().patientId(2L).appointmentTime(fixedNow)
                .doctorId(102L).build();

        when(patientRepositoryMock.findById(appointmentRequest.getPatientId())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> patientManagementService.takeDoctorAppointment(appointmentRequest));
        assertEquals("Patient isn't found with id: 2", exception.getMessage());
        verify(patientRepositoryMock, atLeastOnce()).findById(2L);

    }

    // when patient is not available...
    @Test
    public void shouldReturnExceptionWhenDoctorIsNotFoundTest() {
        LocalDateTime fixedNow = LocalDateTime.of(2025, 4, 16, 10, 0);

        AppointmentRequest appointmentRequest = AppointmentRequest.builder().patientId(4L).appointmentTime(fixedNow)
                .doctorId(102L).build();

        when(patientRepositoryMock.findById(appointmentRequest.getPatientId()))
                .thenReturn(Optional.of(Patient.builder().build()));

        when(doctorRepositoryMock.findById(appointmentRequest.getDoctorId())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> patientManagementService.takeDoctorAppointment(appointmentRequest));
        assertEquals("Doctor isn't found with id: 102", exception.getMessage());
        verify(patientRepositoryMock, atLeastOnce()).findById(4L);
        verify(doctorRepositoryMock, atLeastOnce()).findById(102L);
    }

    @Test
    public void shouldThrowConflictExceptionWhenDoctorIsnotAvailableTest() {
        LocalDateTime fixedNow = LocalDateTime.of(2025, 4, 16, 10, 0);

        AppointmentRequest appointmentRequest = AppointmentRequest.builder().patientId(5L).appointmentTime(fixedNow)
                .doctorId(102L).build();

        Patient patient = Patient.builder().build();
        Doctor doctor = Doctor.builder().build();

        LocalDateTime slotStart = fixedNow.minusMinutes(15);
        LocalDateTime slotEnd = fixedNow.plusMinutes(15);

        when(patientRepositoryMock.findById(appointmentRequest.getPatientId())).thenReturn(Optional.of(patient));
        when(doctorRepositoryMock.findById(appointmentRequest.getDoctorId())).thenReturn(Optional.of(doctor));
        when(appointmentRepositoryMock.findByDoctorAndAppointmentTimeBetween(doctor, slotStart, slotEnd))
                .thenReturn(List.of(new Appointments()));

        AppointmentConflictException exception = assertThrows(AppointmentConflictException.class,
                () -> patientManagementService.takeDoctorAppointment(appointmentRequest));

        assertEquals("Doctor is not available at the requested time", exception.getMessage());
        verify(patientRepositoryMock, times(1)).findById(5L);
        verify(doctorRepositoryMock, times(1)).findById(102L);
        verify(appointmentRepositoryMock, times(1)).findByDoctorAndAppointmentTimeBetween(doctor, slotStart, slotEnd);

    }
}
