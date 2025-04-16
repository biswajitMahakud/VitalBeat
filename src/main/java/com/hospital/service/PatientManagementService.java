package com.hospital.service;

import com.hospital.ExceptionHandler.AppointmentConflictException;
import com.hospital.ExceptionHandler.ResourceExistException;
import com.hospital.ExceptionHandler.ResourceNotFoundException;
import com.hospital.dto.*;
import com.hospital.entity.*;
import com.hospital.enums.AppointmentStatus;
import com.hospital.repositories.AppointmentRepository;
import com.hospital.repositories.DailyVitalsInfoRepository;
import com.hospital.repositories.DoctorRepository;
import com.hospital.repositories.PatientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PatientManagementService {
    public static final Logger logger = LoggerFactory.getLogger(PatientManagementService.class);

    private DoctorRepository doctorRepository;

    private PatientRepository patientRepository;

    private AppointmentRepository appointmentRepository;

    private DailyVitalsInfoRepository vitalsRepository;

    // constructor injection
    public PatientManagementService(DoctorRepository doctorRepository, PatientRepository patientRepository,
                                  AppointmentRepository appointmentRepository, DailyVitalsInfoRepository vitalsRepository) {
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
        this.vitalsRepository = vitalsRepository;
    }

    // adding patients
    public ResponseEntity<String> registerPatient(PatientInfo patientDetails) {

        if (patientDetails == null)
            throw new NullPointerException("Patient can't be null!");

        if (patientDetails.getAddress() == null || patientDetails.getEmail() == null || patientDetails.getName() == null
                || patientDetails.getPassword() == null)
            throw new DataIntegrityViolationException("Address, email, name, password can't be null!");

        Address newPatientAddress = Address.builder().state(patientDetails.getAddress().getState())
                .city(patientDetails.getAddress().getCity()).pincode(patientDetails.getAddress().getPincode()).build();

        Patient patient = Patient.builder().email(patientDetails.getEmail()).name(patientDetails.getName())
                .password(patientDetails.getPassword()).patientAddress(newPatientAddress).build();

        Optional<Patient> existPatient = patientRepository.findByEmail(patientDetails.getEmail());
        System.out.println("Patient emial: " + patientDetails.getEmail());

        if (!existPatient.isEmpty())
            throw new ResourceExistException("Patient already exist with email id: " + existPatient.get().getEmail());

        patient = patientRepository.save(patient);
        System.out.println("patient id: " + patient.getPatientId());

        return ResponseEntity.ok("Patient Added Successfully! Your id is: " + patient.getPatientId());
    }

    // get patient details
    public ResponseEntity<PatientInfo> getPatient(Long id) {

        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient is not found with id: " + id));

        // Address
        PatientAddress address = PatientAddress.builder().city(patient.getPatientAddress().getCity())
                .pincode(patient.getPatientAddress().getPincode()).state(patient.getPatientAddress().getState())
                .build();

        PatientInfo patientInfo = PatientInfo.builder().email(patient.getEmail()).name(patient.getName())
                .patientId(patient.getPatientId()).password(patient.getPassword()).address(address)
                .appointments(patient.getAppointments()).build();

        logger.info("Patient Details: ", patientInfo);

        return ResponseEntity.ok(patientInfo);
    }

    // patient taking appointment to doctor...
    public ResponseEntity<String> takeDoctorAppointment(AppointmentRequest appointmentRequest) {
        Patient existingPatient = patientRepository.findById(appointmentRequest.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Patient isn't found with id: " + appointmentRequest.getPatientId()));

        Doctor existingDoctor = doctorRepository.findById(appointmentRequest.getDoctorId()).orElseThrow(
                () -> new ResourceNotFoundException("Doctor isn't found with id: " + appointmentRequest.getDoctorId()));

        LocalDateTime appointmentTime = appointmentRequest.getAppointmentTime();

        // Check for scheduling conflicts (example: 15-minute buffer)
        LocalDateTime slotStart = appointmentTime.minusMinutes(15);
        LocalDateTime slotEnd = appointmentTime.plusMinutes(15);
        List<Appointments> conflicts = appointmentRepository.findByDoctorAndAppointmentTimeBetween(existingDoctor,
                slotStart, slotEnd);

        if (!conflicts.isEmpty()) {
            throw new AppointmentConflictException("Doctor is not available at the requested time");
        }

        // Create and save the appointment
        Appointments appointment = new Appointments();
        appointment.setPatient(existingPatient);
        appointment.setDoctor(existingDoctor);
        appointment.setAppointmentTime(appointmentTime);
        appointment.setStatus(AppointmentStatus.SCHEDULED);

        Appointments bookedAppointment = appointmentRepository.save(appointment);

        return ResponseEntity
                .ok("Your Appointment is Booked and your Appointment id is: " + bookedAppointment.getAppointmentId());

    }


    // add daily vitals by patient
    public ResponseEntity<String> addDailyVitals(SubmitVitalsDto vitalsInfo) {
        DailyVitalsReport vitalsReport = DailyVitalsReport.builder().patientId(vitalsInfo.getPatientId())
                .bloodGlucose(vitalsInfo.getBloodGlucose()).bloodPressure(vitalsInfo.getBloodPressure())
                .bmi(vitalsInfo.getBmi()).bodyTemperature(vitalsInfo.getBodyTemperature())
                .oxygenSaturation(vitalsInfo.getOxygenSaturation()).pulseRate(vitalsInfo.getPulseRate()).build();

        // check patient is available or not by using given patientId
        Optional<Patient> existedPatient = patientRepository.findById(vitalsInfo.getPatientId());
        if (existedPatient.isEmpty())
            throw new ResourceNotFoundException("Patient isn't found with id: " + vitalsInfo.getPatientId());

        vitalsRepository.save(vitalsReport);

        return ResponseEntity
                .ok("Your vitals is successfully updated in db and your id is: " + vitalsInfo.getPatientId());
    }

    // get all patient vitals history
    public ResponseEntity<List<PatientVitalsHistoryDto>> getAllMyPastVitalsReport(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient isn't exist with the given id: " + id));

        List<DailyVitalsReport> pastVitalsData = vitalsRepository.findAllByPatientId(id);

        List<PatientVitalsHistoryDto> listOfPatientVitalsHistory = new ArrayList<>();

        for (DailyVitalsReport vitalData : pastVitalsData) {
            PatientVitalsHistoryDto paientVitalsDto = PatientVitalsHistoryDto.builder()
                    .bloodGlucose(vitalData.getBloodGlucose()).bloodPressure(vitalData.getBloodPressure())
                    .bmi(vitalData.getBmi()).bodyTemperature(vitalData.getBodyTemperature())
                    .oxygenSaturation(vitalData.getOxygenSaturation()).patientId(vitalData.getPatientId())
                    .patientName(patient.getName()).pulseRate(vitalData.getPulseRate()).vitalId(vitalData.getVitalId())
                    .vitalSubmitDate(vitalData.getVitalSubmitDate()).weight(vitalData.getWeight()).build();

            listOfPatientVitalsHistory.add(paientVitalsDto);
        }

        return ResponseEntity.ok(listOfPatientVitalsHistory);
    }
}
