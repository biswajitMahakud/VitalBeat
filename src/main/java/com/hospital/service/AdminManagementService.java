package com.hospital.service;

import com.hospital.ExceptionHandler.ResourceNotFoundException;
import com.hospital.entity.Appointments;
import com.hospital.repositories.AppointmentRepository;
import com.hospital.repositories.DailyVitalsInfoRepository;
import com.hospital.repositories.DoctorRepository;
import com.hospital.repositories.PatientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminManagementService {
    public static final Logger logger = LoggerFactory.getLogger(AdminManagementService.class);

    private DoctorRepository doctorRepository;

    private PatientRepository patientRepository;

    private AppointmentRepository appointmentRepository;

    private DailyVitalsInfoRepository vitalsRepository;

    // constructor injection
    public AdminManagementService(DoctorRepository doctorRepository, PatientRepository patientRepository,
                                     AppointmentRepository appointmentRepository, DailyVitalsInfoRepository vitalsRepository) {
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
        this.vitalsRepository = vitalsRepository;
    }

    // get appointment by id
    public ResponseEntity<Appointments> getAppointmentsById(Long id) {
        Appointments exstingAppointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment isn't schedule"));
        return ResponseEntity.ok(exstingAppointment);
    }

    // get all appointments
    public ResponseEntity<List<Appointments>> getAllAppointments() {
        List<Appointments> exstingAppointment = appointmentRepository.findAll();
        return ResponseEntity.ok(exstingAppointment);
    }
}
