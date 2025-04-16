package com.hospital.controllers;

import java.util.List;

import com.hospital.dto.*;
import com.hospital.service.AdminManagementService;
import com.hospital.service.DoctorManagementService;
import com.hospital.service.PatientManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hospital.service.HospitalManagementService;

@RestController
@RequestMapping("/evital/user")
public class PatientController {

	private PatientManagementService patientManagementService;

	public PatientController(PatientManagementService patientManagementService){
		this.patientManagementService = patientManagementService;
	}

	// register patient
	@PostMapping("/register/patient")
	public ResponseEntity<String> registerPatient(@RequestBody PatientInfo patientDetails) {
		return patientManagementService.registerPatient(patientDetails);
	}

	//login functionality
	@PostMapping("/patient/signin")
	public void patientLogin(@RequestBody PatientLogin patientLoginData){

	}

	// get patient details
	@PostMapping("/getPatient/{paitentId}")
	public ResponseEntity<PatientInfo> getPatient(@PathVariable(name = "paitentId") Long id) {
		return patientManagementService.getPatient(id);
	}

	//take appointments
	@PostMapping("/book/doctor/appointment")
	public ResponseEntity<String> takeDoctorAppointment(@RequestBody AppointmentRequest appointmentRequest) {
		return patientManagementService.takeDoctorAppointment(appointmentRequest);
	}
	
	//rest api meant for adding patient daily vitals on server
	@PostMapping("/add/patient/daily/vitals")
    public ResponseEntity<String> addDailyVitals(@RequestBody SubmitVitalsDto dailyVitalsInfo){
    	return patientManagementService.addDailyVitals(dailyVitalsInfo);
    }
	
	//get past vitals history of patient
	@GetMapping("/get/vitals/history/{patientId}")
	public ResponseEntity<List<PatientVitalsHistoryDto>> getAllMyPastVitalsReport(@PathVariable(name = "patientId") Long id) {
		return patientManagementService.getAllMyPastVitalsReport(id);
	}
}
