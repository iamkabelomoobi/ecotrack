package com.ecotrack.ecotrack.mapper;

import com.ecotrack.ecotrack.dto.AdminDTO;
import com.ecotrack.ecotrack.entity.Admin;

public class AdminMapper {
    public static AdminDTO toDTO(Admin admin) {
        return AdminDTO.builder()
                .id(admin.getId())
                .firstName(admin.getFirstName())
                .lastName(admin.getLastName())
                .user(UserMapper.toDTO(admin.getUser()))
                .build();
    }

    public static Admin toEntity(AdminDTO adminDTO) {
        return Admin.builder()
                .id(adminDTO.getId())
                .firstName(adminDTO.getFirstName())
                .lastName(adminDTO.getLastName())
                .user(UserMapper.toEntity(adminDTO.getUser()))
                .build();
    }
}