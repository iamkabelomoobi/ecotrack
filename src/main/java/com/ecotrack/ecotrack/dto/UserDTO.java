package com.ecotrack.ecotrack.dto;

import com.ecotrack.ecotrack.entity.User.Role;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String email;
    private String phone;
    private Role role;
}