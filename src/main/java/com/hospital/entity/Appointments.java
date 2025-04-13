package com.hospital.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.hospital.enums.AppointmentStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity

public class Appointments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appointmentId;

    @Column(nullable = false)
    private LocalDateTime appointmentTime;
    
    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "patientId", nullable = false)
    private Patient patient;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "doctorId", nullable = false)
    private Doctor doctor;
}
