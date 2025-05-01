package com.ecotrack.ecotrack.dto;

import com.ecotrack.ecotrack.entity.Driver.VehicleType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DriverRegistrationDTO extends UserRegistrationDTO {
    @NotBlank(message = "First name is required")
    @Size(max = 50, message = "First name must be less than 50 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 50, message = "Last name must be less than 50 characters")
    private String lastName;

    @NotBlank(message = "Driver's license number is required")
    @Size(min = 6, max = 20, message = "License number must be between 6-20 characters")
    private String licenseNumber;

    @NotNull(message = "Vehicle type is required")
    private VehicleType vehicleType;

    @NotBlank(message = "Vehicle registration is required")
    @Pattern(regexp = "[A-Z0-9]{6,20}", message = "Registration must be 6-20 alphanumeric characters in uppercase")
    private String vehicleRegistration;
}