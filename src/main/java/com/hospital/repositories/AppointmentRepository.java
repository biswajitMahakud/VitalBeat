package com.hospital.repositories;

import com.hospital.entity.Appointments;
import com.hospital.entity.Doctor;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository extends JpaRepository<Appointments, Long> {
    List<Appointments> findByDoctorAndAppointmentTimeBetween(Doctor doctor, LocalDateTime start, LocalDateTime end);

}
