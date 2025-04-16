package com.hospital.integration;

/*import com.hospital.dto.DoctorInfo;
import com.hospital.dto.PatientInfo;
import com.hospital.dto.PatientAddress;
import com.hospital.entity.Doctor;
import com.hospital.entity.Patient;
import com.hospital.enums.Specialization;
import com.hospital.repositories.DoctorRepository;
import com.hospital.repositories.PatientRepository;
import com.hospital.service.HospitalManagementService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
public class HospitalManagementServiceIntegrationTest {

    @Autowired
    private HospitalManagementService hospitalManagementService;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Test
    public void testAddDoctorIntegration() {
        DoctorInfo doctorInfo = DoctorInfo.builder()
                .name("Dr. John Doe")
                .email("johndoe@example.com")
                .password("password123")
                .specialization("CARDIOLOGIST")
                .build();

        var response = hospitalManagementService.registerDoctor(doctorInfo);

        assertEquals("Doctor Added Successfully! Your id is: 1", response.getBody());
        assertTrue(doctorRepository.findByEmail("johndoe@example.com").isPresent());
    }

    @Test
    public void testAddPatientIntegration() {
        PatientAddress address = PatientAddress.builder()
                .city("New York")
                .state("NY")
                .pincode("10001")
                .build();

        PatientInfo patientInfo = PatientInfo.builder()
                .name("Jane Doe")
                .email("janedoe@example.com")
                .password("password123")
                .address(address)
                .build();

        var response = hospitalManagementService.registerPatient(patientInfo);

        assertEquals("Patient Added Successfully! Your id is: 1", response.getBody());
        assertTrue(patientRepository.findByEmail("janedoe@example.com").isPresent());
    }

    @Test
    public void testGetDoctorByIdIntegration() {
        Doctor doctor = Doctor.builder()
                .name("Dr. Alice")
                .email("alice@example.com")
                .password("password123")
                .specialization(Specialization.DENTIST)
                .build();
        doctor = doctorRepository.save(doctor);

        var response = hospitalManagementService.getDoctorById(doctor.getDoctorId());

        assertEquals("Dr. Alice", response.getBody().getName());
        assertEquals("alice@example.com", response.getBody().getEmail());
        assertEquals("DENTIST", response.getBody().getSpecialization());
    }

    @Test
    public void testGetPatientByIdIntegration() {
        Patient patient = Patient.builder()
                .name("Bob")
                .email("bob@example.com")
                .password("password123")
                .patientAddress(com.hospital.entity.Address.builder()
                        .city("Los Angeles")
                        .state("CA")
                        .pincode("90001")
                        .build())
                .build();
        patient = patientRepository.save(patient);

        var response = hospitalManagementService.getPatient(patient.getPatientId());

        assertEquals("Bob", response.getBody().getName());
        assertEquals("bob@example.com", response.getBody().getEmail());
        assertEquals("Los Angeles", response.getBody().getAddress().getCity());
    }
}*/