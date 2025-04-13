package com.hospital.service;

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

import org.apache.catalina.startup.Tomcat.ExistingStandardWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class HospitalManagementService {

	// logger
	public static final Logger logger = LoggerFactory.getLogger(HospitalManagementService.class);

	private DoctorRepository doctorRepository;

	private PatientRepository patientRepository;

	private AppointmentRepository appointmentRepository;
	
	//constructor injection
	public HospitalManagementService(DoctorRepository doctorRepository, PatientRepository patientRepository,
			AppointmentRepository appointmentRepository) {
		this.doctorRepository = doctorRepository;
		this.patientRepository = patientRepository;
		this.appointmentRepository = appointmentRepository;
	}

	public ResponseEntity<String> addDoctor(DoctorInfo doctorDetails) {
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

	// adding patients
	public ResponseEntity<String> addPatient(PatientInfo patientDetails) {
		
		if(patientDetails == null)
			throw new NullPointerException("Patient can't be null!");
		
		if(patientDetails.getAddress() == null || patientDetails.getEmail() == null || patientDetails.getName()==null || patientDetails.getPassword()== null)
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

	// taking appointment
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
