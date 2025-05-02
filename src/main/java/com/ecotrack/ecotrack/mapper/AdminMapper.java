package com.ecotrack.ecotrack.mapper;

import org.springframework.stereotype.Component;

import com.ecotrack.ecotrack.dto.AdminDTO;
import com.ecotrack.ecotrack.entity.Admin;

@Component
public class AdminMapper {
    public AdminDTO toDTO(Admin admin) {
        return AdminDTO.builder()
                .id(admin.getId())
                .firstName(admin.getFirstName())
                .lastName(admin.getLastName())
                .user(admin.getUser() != null ? UserMapper.toDTO(admin.getUser()) : null)
                .build();
    }

    public Admin toEntity(AdminDTO adminDTO) {
        return Admin.builder()
                .id(adminDTO.getId())
                .firstName(adminDTO.getFirstName())
                .lastName(adminDTO.getLastName())
                .user(adminDTO.getUser() != null ? UserMapper.toEntity(adminDTO.getUser()) : null)
                .build();
    }
}