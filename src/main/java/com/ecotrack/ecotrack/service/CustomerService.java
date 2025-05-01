package com.ecotrack.ecotrack.service;

import com.ecotrack.ecotrack.dto.CustomerDTO;
import com.ecotrack.ecotrack.entity.Customer;
import com.ecotrack.ecotrack.exception.ResourceNotFoundException;
import com.ecotrack.ecotrack.mapper.CustomerMapper;
import com.ecotrack.ecotrack.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    private void updateCustomerFields(Customer customer, CustomerDTO dto) {
        Optional.ofNullable(dto.getFirstName()).ifPresent(customer::setFirstName);
        Optional.ofNullable(dto.getLastName()).ifPresent(customer::setLastName);
    }

    public List<CustomerDTO> findAll() {
        return customerRepository.findAll()
                .stream()
                .map(CustomerMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<CustomerDTO> findByUserId(Long userId) {
        return customerRepository.findByUserId(userId)
                .map(CustomerMapper::toDTO);
    }

    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }

    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    @Transactional
    public CustomerDTO updateCustomerByUserId(Long userId, CustomerDTO customerDTO) {
        Customer customer = customerRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found."));

        updateCustomerFields(customer, customerDTO);

        return CustomerMapper.toDTO(customerRepository.save(customer));
    }
}
