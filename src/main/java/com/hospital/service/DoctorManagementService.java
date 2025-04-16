package com.hospital.service;

import com.hospital.ExceptionHandler.ResourceNotFoundException;
import com.hospital.dto.DoctorInfo;
import com.hospital.entity.Doctor;
import com.hospital.enums.Specialization;
import com.hospital.repositories.AppointmentRepository;
import com.hospital.repositories.DailyVitalsInfoRepository;
import com.hospital.repositories.DoctorRepository;
import com.hospital.repositories.PatientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class DoctorManagementService {
    public static final Logger logger = LoggerFactory.getLogger(DoctorManagementService.class);

    private DoctorRepository doctorRepository;

    private PatientRepository patientRepository;

    private AppointmentRepository appointmentRepository;

    private DailyVitalsInfoRepository vitalsRepository;

    // constructor injection
    public DoctorManagementService(DoctorRepository doctorRepository, PatientRepository patientRepository,
                                  AppointmentRepository appointmentRepository, DailyVitalsInfoRepository vitalsRepository) {
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
        this.vitalsRepository = vitalsRepository;
    }

    public ResponseEntity<String> registerDoctor(DoctorInfo doctorDetails) {
        // If doctor specialization string isn't matched with our enum constant then,
        // It automatically throws IllegalArgumentException

        Specialization specialization = null;

        if (doctorDetails.getSpecialization() != null)
            try {
                specialization = Specialization.valueOf(doctorDetails.getSpecialization().toUpperCase());
            } catch (IllegalArgumentException ex) {
                throw new IllegalArgumentException("Input specialization isn't matched with declared specialization: "
                        + doctorDetails.getSpecialization().toString());
            }

        Doctor doctor = Doctor.builder().email(doctorDetails.getEmail()).name(doctorDetails.getName())
                .password(doctorDetails.getPassword()).specialization(specialization).build();

        Doctor addedDoctor = doctorRepository.save(doctor);

        return new ResponseEntity<String>("Doctor Added Successfully! Your id is: " + addedDoctor.getDoctorId(),
                HttpStatus.OK);
    }

    // get doctor details by id

    public ResponseEntity<DoctorInfo> getDoctorById(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor isn't found with id: " + doctorId));

        DoctorInfo doctorDetails = DoctorInfo.builder().name(doctor.getName()).email(doctor.getEmail())
                .specialization(doctor.getSpecialization().name()).appointmentsList(doctor.getAppointmentsList())
                .build();

        return ResponseEntity.ok(doctorDetails);
    }
}
