package com.hospital.service;

import com.hospital.repositories.AppointmentRepository;
import com.hospital.repositories.DoctorRepository;
import com.hospital.repositories.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class AdminManagementServiceTest {
    @Mock
    private DoctorRepository doctorRepositoryMock;

    @Mock
    private PatientRepository patientRepositoryMock;

    @Mock
    private AppointmentRepository appointmentRepositoryMock;

    @InjectMocks
    private AdminManagementService adminManagementService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
}
