package com.hospital.service;

import com.hospital.ExceptionHandler.ResourceNotFoundException;
import com.hospital.dto.DoctorInfo;
import com.hospital.entity.Doctor;
import com.hospital.enums.Specialization;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.atLeastOnce;

public class DoctorManagementServiceTest {
    @Mock
    private DoctorRepository doctorRepositoryMock;

    @Mock
    private PatientRepository patientRepositoryMock;

    @Mock
    private AppointmentRepository appointmentRepositoryMock;

    @InjectMocks
    private DoctorManagementService doctorManagementService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    // add doctor test with valid Doctor properties obj....
    @Test
    public void addDoctorWithValidPropertiesTest() {
        DoctorInfo doctorInfo = DoctorInfo.builder().name("Rahul Tewatia").email("rahul23@gmail.com")
                .password("rahul23@").specialization("DENTIST").build();

        Doctor doctor = Doctor.builder().doctorId(1L).name(doctorInfo.getName()).email(doctorInfo.getEmail())
                .password(doctorInfo.getPassword())
                .specialization(Specialization.valueOf(doctorInfo.getSpecialization())).build();

        // stubbing
        when(doctorRepositoryMock.save(any(Doctor.class))).thenReturn(doctor);

        ResponseEntity<String> response = doctorManagementService.registerDoctor(doctorInfo);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Doctor Added Successfully! Your id is: 1", response.getBody());
        verify(doctorRepositoryMock, times(1)).save(any(Doctor.class));
    }

    // add doctor test with Invalid Doctor properties obj
    @Test
    public void addDoctorWithInvalidPropertiesTest() {
        DoctorInfo doctorInfo = DoctorInfo.builder().email("rahul23@gmail.com").specialization("DENTIST").build();

        doThrow(new DataIntegrityViolationException("Invalid properties")).when(doctorRepositoryMock)
                .save(any(Doctor.class));
        doThrow(new NullPointerException("null doctor can't be stored!")).when(doctorRepositoryMock).save(null);

        assertAll("Invalid Properties Test",
                () -> assertThrows(DataIntegrityViolationException.class,
                        () -> doctorManagementService.registerDoctor(doctorInfo)),

                () -> assertThrows(NullPointerException.class, () -> doctorManagementService.registerDoctor(null)));
    }

    // This following test describes, if Doctor obj. has invalid specialization
    // then, It should throw IllegalArgumentException.class
    @Test
    public void addDoctorWithInvalidSpecializationTest() {
        DoctorInfo doctorInfo = DoctorInfo.builder().name("Rahul Tewatia").email("rahul23@gmail.com")
                .password("rahul23@").specialization("Invalid_Specialization").build();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> doctorManagementService.registerDoctor(doctorInfo));
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
        ResponseEntity<DoctorInfo> doctorInfo = doctorManagementService.getDoctorById(2L);
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
                () -> doctorManagementService.getDoctorById(3L));
        assertEquals("Doctor isn't found with id: 3", exception.getMessage());
        verify(doctorRepositoryMock, atLeastOnce()).findById(3L);
    }

}
