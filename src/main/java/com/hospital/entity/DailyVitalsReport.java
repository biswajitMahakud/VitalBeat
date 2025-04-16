package com.hospital.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "daily_vitals")
public class DailyVitalsReport {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long vitalId;
	@Column(nullable = false)
	private Long patientId;
	@Column(nullable = false)
	private LocalDateTime vitalSubmitDate;
	@Column(nullable = false)
	private String pulseRate;
	@Column(nullable = false)
	private String oxygenSaturation;
	@Column(nullable = false)
	private String bloodPressure;
	@Column(nullable = false)
	private String bloodGlucose;
	@Column(nullable = false)
	private String bodyTemperature;
	@Column(nullable = false)
	private String weight;
	@Column(nullable = false)
	private String bmi;
	
	/*@Column(name = "patientId")
	@ManyToOne
	private Patient patient;*/
}
