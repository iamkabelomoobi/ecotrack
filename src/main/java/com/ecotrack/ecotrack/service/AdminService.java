package com.ecotrack.ecotrack.service;

import com.ecotrack.ecotrack.dto.AdminDTO;
import com.ecotrack.ecotrack.entity.Admin;
import com.ecotrack.ecotrack.exception.ResourceNotFoundException;
import com.ecotrack.ecotrack.mapper.AdminMapper;
import com.ecotrack.ecotrack.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private static final Logger logger = LoggerFactory.getLogger(AdminService.class);

    private final AdminRepository adminRepository;
    private final AdminMapper adminMapper;

    public Admin save(Admin admin) {
        logger.info("Saving admin: {}", admin);
        return adminRepository.save(admin);
    }

    public Optional<AdminDTO> findByUserId(Long userId) {
        logger.info("Finding admin by userId: {}", userId);
        return adminRepository.findByUserId(userId).map(adminMapper::toDTO);
    }

    public List<AdminDTO> findAll() {
        logger.info("Fetching all admins");
        return adminRepository.findAll()
                .stream()
                .map(adminMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public AdminDTO update(Long userId, AdminDTO adminDTO) {
        if (adminDTO == null) {
            throw new IllegalArgumentException("AdminDTO cannot be null");
        }

        logger.info("Updating admin for userId: {}", userId);
        Admin admin = adminRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found for userId: " + userId));

        Optional.ofNullable(adminDTO.getFirstName()).ifPresent(admin::setFirstName);
        Optional.ofNullable(adminDTO.getLastName()).ifPresent(admin::setLastName);

        Admin updatedAdmin = adminRepository.save(admin);
        logger.info("Admin updated successfully for userId: {}", userId);

        return adminMapper.toDTO(updatedAdmin);
    }

    @Transactional
    public void deleteById(Long id) {
        logger.info("Deleting admin by id: {}", id);
        if (!adminRepository.existsById(id)) {
            throw new ResourceNotFoundException("Admin not found for id: " + id);
        }
        adminRepository.deleteById(id);
        logger.info("Admin deleted successfully for id: {}", id);
    }
}
