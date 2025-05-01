package com.ecotrack.ecotrack.mapper;

import com.ecotrack.ecotrack.dto.CustomerDTO;
import com.ecotrack.ecotrack.entity.Customer;

public class CustomerMapper {
    public static CustomerDTO toDTO(Customer customer) {
        return CustomerDTO.builder()
                .id(customer.getId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .user(UserMapper.toDTO(customer.getUser()))
                .build();
    }

    public static Customer toEntity(CustomerDTO customerDTO) {
        return Customer.builder()
                .id(customerDTO.getId())
                .firstName(customerDTO.getFirstName())
                .lastName(customerDTO.getLastName())
                .user(UserMapper.toEntity(customerDTO.getUser()))
                .build();
    }
}