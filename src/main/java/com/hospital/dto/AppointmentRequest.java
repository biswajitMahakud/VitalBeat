package com.hospital.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter

public class AppointmentRequest {
    private Long patientId;
    private Long doctorId;
    private LocalDateTime appointmentTime;
}


