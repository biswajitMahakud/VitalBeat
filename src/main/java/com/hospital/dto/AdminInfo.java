package com.hospital.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter

public class AdminInfo {
    private String name;
    private String email;
    private String password;
}


