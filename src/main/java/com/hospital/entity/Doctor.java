package com.hospital.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import com.hospital.enums.Specialization;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "doctorInfo")

public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long doctorId;
    @Column(nullable = false, length = 50)
    private String name;
    @Column(nullable = false, length = 50)
    private String email;
    @Column(nullable = false, length = 12)
    private String password;

    @OneToMany(mappedBy = "doctor", fetch = FetchType.LAZY)
    @JsonBackReference
    List<Appointments> appointmentsList = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Specialization specialization;

}


