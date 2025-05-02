package com.ecotrack.ecotrack.mapper;

import com.ecotrack.ecotrack.dto.CustomerDTO;
import com.ecotrack.ecotrack.entity.Customer;
import com.ecotrack.ecotrack.entity.Address;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    private final AddressMapper addressMapper;

    public CustomerMapper(AddressMapper addressMapper) {
        this.addressMapper = addressMapper;
    }

    public CustomerDTO toDTO(Customer customer) {
        return CustomerDTO.builder()
                .id(customer.getId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .address(customer.getAddress() != null ? addressMapper.toDto(customer.getAddress()) : null)
                .user(customer.getUser() != null ? UserMapper.toDTO(customer.getUser()) : null)
                .build();
    }

    public Customer toEntity(CustomerDTO customerDTO) {
        Customer customer = Customer.builder()
                .id(customerDTO.getId())
                .firstName(customerDTO.getFirstName())
                .lastName(customerDTO.getLastName())
                .user(customerDTO.getUser() != null ? UserMapper.toEntity(customerDTO.getUser()) : null)
                .build();

        if (customerDTO.getAddress() != null) {
            Address address = addressMapper.toEntity(customerDTO.getAddress());
            address.setCustomer(customer);
            customer.setAddress(address);
        }

        return customer;
    }
}