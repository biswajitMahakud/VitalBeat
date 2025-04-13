package com.hospital.entity;


import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "patientAddress")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;
    @Column(nullable = false, length = 30)
    private String city;
    @Column(nullable = false, length = 20)
    private String state;
    @Column(nullable = false, length = 7)
    private String pincode;
}
