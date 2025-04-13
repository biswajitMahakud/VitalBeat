package com.hospital.controllers;

import com.hospital.dto.AppointmentRequest;
import com.hospital.dto.DoctorInfo;
import com.hospital.dto.PatientInfo;
import com.hospital.entity.Appointments;
import com.hospital.service.HospitalManagementService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/evital")
@RestController
public class ManagementController {
    @Autowired
    HospitalManagementService hospitalManagementService;
    //add doctor
    @PostMapping(path = "/addDoctor", consumes = "application/json")
    public ResponseEntity<String> addDoctor(@RequestBody DoctorInfo doctorDetails) {
        return hospitalManagementService.addDoctor(doctorDetails);
    }
    
    //get doctor details by id
    @GetMapping("/get/doctor/{id}")
    public ResponseEntity<DoctorInfo> getDoctorById(@PathVariable(name = "id") Long doctorId) {
    	return hospitalManagementService.getDoctorById(doctorId);
    }

    //add patient
    @PostMapping("/addPatient")
    public ResponseEntity<String> addPatient(@RequestBody PatientInfo patientDetails) {
        return hospitalManagementService.addPatient(patientDetails);
    }

    //get patient details
    @PostMapping("/getPatient/{paitentId}")
    public ResponseEntity<PatientInfo> getPatient(@PathVariable (name = "paitentId") Long id) {
        return hospitalManagementService.getPatient(id);
    }
    
    //take appointments
    
    @PostMapping("/book/doctor/appointment")
    public ResponseEntity<String> takeDoctorAppointment(@RequestBody AppointmentRequest appointmentRequest){
    	return hospitalManagementService.takeDoctorAppointment(appointmentRequest);
    }
    
    //get appointment by id
    @GetMapping("/getAppointment/{appointmentId}")
    public ResponseEntity<Appointments> getAppointmentsById(@PathVariable (name = "appointmentId") Long id) {
        return hospitalManagementService.getAppointmentsById(id);
    }
    
    //get all appointments
    @GetMapping("/get/all/appointments")
    public ResponseEntity<List<Appointments>> getAllAppointments() {
        return hospitalManagementService.getAllAppointments();
    }
    

}
