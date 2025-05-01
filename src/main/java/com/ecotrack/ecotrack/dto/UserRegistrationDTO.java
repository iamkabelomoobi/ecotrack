package com.ecotrack.ecotrack.dto;

import com.ecotrack.ecotrack.entity.User.Role;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "role")
@JsonSubTypes({
    @JsonSubTypes.Type(value = AdminRegistrationDTO.class, name = "ADMIN"),
    @JsonSubTypes.Type(value = CustomerRegistrationDTO.class, name = "CUSTOMER"),
    @JsonSubTypes.Type(value = DriverRegistrationDTO.class, name = "DRIVER")
})
public abstract class UserRegistrationDTO {
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+?[0-9\\s-]{10,}$", message = "Invalid phone number")
    private String phone;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    private Role role;
}