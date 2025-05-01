package com.ecotrack.ecotrack.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {
    private Long id;

    @Size(max = 100, message = "Street must be less than 100 characters")
    private String street;

    @Size(max = 50, message = "City must be less than 50 characters")
    private String city;

    @Size(max = 50, message = "State/Province must be less than 50 characters")
    private String stateProvince;

    @Pattern(regexp = "^\\d{4}$", message = "Postal code must be exactly 4 digits")
    private String postalCode;

    @Size(max = 50, message = "Country must be less than 50 characters")
    private String country;
}