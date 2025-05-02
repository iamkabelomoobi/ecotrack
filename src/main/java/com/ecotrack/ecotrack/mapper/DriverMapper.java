package com.ecotrack.ecotrack.mapper;

import org.springframework.stereotype.Component;

import com.ecotrack.ecotrack.dto.DriverDTO;
import com.ecotrack.ecotrack.entity.Driver;

@Component
public class DriverMapper {
    public DriverDTO toDTO(Driver driver) {
        return DriverDTO.builder()
                .id(driver.getId())
                .firstName(driver.getFirstName())
                .lastName(driver.getLastName())
                .licenseNumber(driver.getLicenseNumber())
                .vehicleType(driver.getVehicleType())
                .vehicleRegistration(driver.getVehicleRegistration())
                .isAvailable(driver.isAvailable())
                .user(driver.getUser() != null ? UserMapper.toDTO(driver.getUser()) : null)
                .build();
    }

    public Driver toEntity(DriverDTO driverDTO) {
        return Driver.builder()
                .id(driverDTO.getId())
                .firstName(driverDTO.getFirstName())
                .lastName(driverDTO.getLastName())
                .licenseNumber(driverDTO.getLicenseNumber())
                .vehicleType(driverDTO.getVehicleType())
                .vehicleRegistration(driverDTO.getVehicleRegistration())
                .isAvailable(driverDTO.isAvailable())
                .user(driverDTO.getUser() != null ? UserMapper.toEntity(driverDTO.getUser()) : null)
                .build();
    }
}