package com.hospital.controllers;

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

import com.hospital.dto.DoctorInfo;
import com.hospital.service.HospitalManagementService;

@RestController
@RequestMapping("/evital/doctor")
public class DoctorController {
	private DoctorManagementService doctorManagementService;

	public DoctorController(DoctorManagementService doctorManagementService){
		this.doctorManagementService = doctorManagementService;
	}


	// add doctor
	@PostMapping(path = "/add/Doctor", consumes = "application/json")
	public ResponseEntity<String> registerDoctor(@RequestBody DoctorInfo doctorDetails) {
		return doctorManagementService.registerDoctor(doctorDetails);
	}

	// get doctor details by id
	@GetMapping("/get/doctor/{id}")
	public ResponseEntity<DoctorInfo> getDoctorById(@PathVariable(name = "id") Long doctorId) {
		return doctorManagementService.getDoctorById(doctorId);
	}
	
	
	
}
