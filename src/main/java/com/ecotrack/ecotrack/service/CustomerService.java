package com.ecotrack.ecotrack.service;

import com.ecotrack.ecotrack.dto.CustomerDTO;
import com.ecotrack.ecotrack.dto.AddressDTO;
import com.ecotrack.ecotrack.entity.Customer;
import com.ecotrack.ecotrack.entity.Address;
import com.ecotrack.ecotrack.exception.ResourceNotFoundException;
import com.ecotrack.ecotrack.mapper.CustomerMapper;
import com.ecotrack.ecotrack.mapper.AddressMapper;
import com.ecotrack.ecotrack.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final AddressMapper addressMapper;

    public CustomerService(CustomerRepository customerRepository, CustomerMapper customerMapper,
            AddressMapper addressMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
        this.addressMapper = addressMapper;
    }

    private void updateCustomerFields(Customer customer, CustomerDTO dto) {
        Optional.ofNullable(dto.getFirstName()).ifPresent(customer::setFirstName);
        Optional.ofNullable(dto.getLastName()).ifPresent(customer::setLastName);
    }

    public List<CustomerDTO> findAll() {
        return customerRepository.findAll()
                .stream()
                .map(customerMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<CustomerDTO> findByUserId(Long userId) {
        return customerRepository.findByUserId(userId)
                .map(customerMapper::toDTO);
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

        return customerMapper.toDTO(customerRepository.save(customer));
    }

    @Transactional
    public AddressDTO createOrUpdateAddress(Long userId, AddressDTO addressDTO) {
        Customer customer = customerRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found for user id: " + userId));

        Address address = customer.getAddress();
        if (address == null) {
            address = addressMapper.toEntity(addressDTO);
            address.setCustomer(customer);
        } else {
            addressMapper.updateAddressFromDto(addressDTO, address);
        }

        customer.setAddress(address);
        customerRepository.save(customer);

        return addressMapper.toDto(address);
    }

    @Transactional(readOnly = true)
    public AddressDTO getAddress(Long userId) {
        Customer customer = customerRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found for user id: " + userId));

        Address address = customer.getAddress();
        if (address == null) {
            throw new ResourceNotFoundException("Address not found for customer with user id: " + userId);
        }

        return addressMapper.toDto(address);
    }

    @Transactional
    public void deleteAddress(Long userId) {
        Customer customer = customerRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found for user id: " + userId));

        Address address = customer.getAddress();
        if (address != null) {
            customer.setAddress(null);
            customerRepository.save(customer);
        }
    }
}
