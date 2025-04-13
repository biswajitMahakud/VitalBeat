package com.hospital.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hospital.entity.Appointments;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter

public class DoctorInfo {
    private String name;
    private String email;
    private String password;
    private String specialization;
    @JsonIgnore
    private List<Appointments> appointmentsList;
}


