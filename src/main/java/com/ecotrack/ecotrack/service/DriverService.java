package com.ecotrack.ecotrack.service;

import com.ecotrack.ecotrack.dto.AdminDTO;
import com.ecotrack.ecotrack.dto.DriverDTO;
import com.ecotrack.ecotrack.entity.Admin;
import com.ecotrack.ecotrack.entity.Driver;
import com.ecotrack.ecotrack.exception.ResourceNotFoundException;
import com.ecotrack.ecotrack.mapper.DriverMapper;
import com.ecotrack.ecotrack.repository.DriverRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DriverService {
    private final DriverRepository driverRepository;
    private final DriverMapper driverMapper;

    public DriverService(DriverRepository driverRepository, DriverMapper driverMapper) {
        this.driverRepository = driverRepository;
        this.driverMapper = driverMapper;
    }

    public Driver save(Driver driver) {
        return driverRepository.save(driver);
    }

    public Optional<DriverDTO> findByUserId(Long userId) {
        return driverRepository.findByUserId(userId).map(driverMapper::toDTO);
    }

    public List<DriverDTO> findAll() {
        return driverRepository.findAll()
                .stream()
                .map(driverMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public DriverDTO update(Long userId, DriverDTO driverDTO) {
        if (driverDTO == null) {
            throw new IllegalArgumentException("DriverDTO cannot be null");
        }

        Driver driver = driverRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found for userId: " + userId));

        Optional.ofNullable(driverDTO.getFirstName()).ifPresent(driver::setFirstName);
        Optional.ofNullable(driverDTO.getLastName()).ifPresent(driver::setLastName);

        Driver updatedDriver = driverRepository.save(driver);

        return driverMapper.toDTO(updatedDriver);
    }

    public void deleteById(Long id) {
        driverRepository.deleteById(id);
    }
}
