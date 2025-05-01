package com.ecotrack.ecotrack.dto;

import com.ecotrack.ecotrack.entity.Driver.VehicleType;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriverDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String licenseNumber;
    private VehicleType vehicleType;
    private String vehicleRegistration;
    private boolean isAvailable;
    private UserDTO user;
}