package com.ecotrack.ecotrack.mapper;

import com.ecotrack.ecotrack.dto.UserDTO;
import com.ecotrack.ecotrack.entity.User;

public class UserMapper {
    public static UserDTO toDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .phone(user.getPhone())
                .password(user.getPassword())
                .role(user.getRole())
                .build();
    }

    public static User toEntity(UserDTO userDTO) {
        return User.builder()
                .id(userDTO.getId())
                .email(userDTO.getEmail())
                .phone(userDTO.getPhone())
                .password(userDTO.getPassword())
                .role(userDTO.getRole())
                .build();
    }
}