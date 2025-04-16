package com.hospital.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatientVitalsHistoryDto {
	private Long vitalId;
    private Long patientId;
    private String patientName;
    private LocalDateTime vitalSubmitDate;
    private String pulseRate;
    private String oxygenSaturation;
    private String bloodPressure;
    private String bloodGlucose;
    private String bodyTemperature;
    private String weight;
    private String bmi;
}
