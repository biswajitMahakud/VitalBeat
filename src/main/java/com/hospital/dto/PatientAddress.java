package com.hospital.dto;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class PatientAddress {
    private String city;
    private String state;
    private String pincode;
}
