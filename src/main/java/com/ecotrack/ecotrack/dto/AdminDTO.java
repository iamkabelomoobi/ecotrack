package com.ecotrack.ecotrack.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private UserDTO user;
}