package com.hospital.controllers;

import java.util.List;

import com.hospital.service.AdminManagementService;
import com.hospital.service.DoctorManagementService;
import com.hospital.service.PatientManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hospital.entity.Appointments;
import com.hospital.service.HospitalManagementService;

@RestController
@RequestMapping("/evital/admin")
public class AdminController {
	private AdminManagementService adminManagementService;
	private DoctorManagementService doctorManagementService;
	private PatientManagementService patientManagementService;

	public AdminController(AdminManagementService adminManagementService, DoctorManagementService doctorManagementService, PatientManagementService patientManagementService){
		this.adminManagementService = adminManagementService;
		this.doctorManagementService = doctorManagementService;
		this.patientManagementService = patientManagementService;
	}

	// get appointment by id
	@GetMapping("/getAppointment/{appointmentId}")
	public ResponseEntity<Appointments> getAppointmentsById(@PathVariable(name = "appointmentId") Long id) {
		return adminManagementService.getAppointmentsById(id);
	}

	// get all appointments
	@GetMapping("/get/all/appointments")
	public ResponseEntity<List<Appointments>> getAllAppointments() {
		return adminManagementService.getAllAppointments();
	}
}
