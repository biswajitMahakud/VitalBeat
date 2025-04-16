package com.hospital.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "patientInfo")
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long patientId;
    @Column(nullable = false, length = 50)
    private String name;
    @Column(nullable = false, length = 50)
    private String email;
    @Column(nullable = false, length = 30)
    private String password;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", nullable = false)
    private Address patientAddress;

    @OneToMany(mappedBy = "patient", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<Appointments> appointments = new ArrayList<>();
    
    @OneToMany
    private List<DailyVitalsReport> allDailyVitals = new ArrayList<>();
}
