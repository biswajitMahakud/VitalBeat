package com.hospital.dto;

import java.util.List;

import com.hospital.entity.Appointments;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter

public class PatientInfo {
	private Long patientId;
    private String name;
    private String email;
    private String password;
    private PatientAddress address;
    private List<Appointments> appointments;
}


