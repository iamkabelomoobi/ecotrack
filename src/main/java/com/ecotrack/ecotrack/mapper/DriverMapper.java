package com.ecotrack.ecotrack.mapper;

import com.ecotrack.ecotrack.dto.DriverDTO;
import com.ecotrack.ecotrack.entity.Driver;

public class DriverMapper {
    public static DriverDTO toDTO(Driver driver) {
        return DriverDTO.builder()
                .id(driver.getId())
                .firstName(driver.getFirstName())
                .lastName(driver.getLastName())
                .licenseNumber(driver.getLicenseNumber())
                .vehicleType(driver.getVehicleType())
                .vehicleRegistration(driver.getVehicleRegistration())
                .isAvailable(driver.isAvailable())
                .user(UserMapper.toDTO(driver.getUser()))
                .build();
    }

    public static Driver toEntity(DriverDTO driverDTO) {
        return Driver.builder()
                .id(driverDTO.getId())
                .firstName(driverDTO.getFirstName())
                .lastName(driverDTO.getLastName())
                .licenseNumber(driverDTO.getLicenseNumber())
                .vehicleType(driverDTO.getVehicleType())
                .vehicleRegistration(driverDTO.getVehicleRegistration())
                .isAvailable(driverDTO.isAvailable())
                .user(UserMapper.toEntity(driverDTO.getUser()))
                .build();
    }
}