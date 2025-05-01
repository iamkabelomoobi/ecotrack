package com.ecotrack.ecotrack.service;

import com.ecotrack.ecotrack.exception.ResourceNotFoundException;
import com.ecotrack.ecotrack.mapper.AddressMapper;
import com.ecotrack.ecotrack.dto.AddressDTO;
import com.ecotrack.ecotrack.entity.*;
import com.ecotrack.ecotrack.repository.AddressRepository;
import com.ecotrack.ecotrack.repository.CustomerRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final CustomerRepository customerRepository;
    private final AddressMapper addressMapper;

    private Address getAddressEntityByCustomerId(Long customerId) {
        return addressRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found for customer id: " + customerId));
    }

    @Transactional
    public AddressDTO createAddress(Long customerId, @Valid AddressDTO addressDTO) {
        Address address = addressMapper.toEntity(addressDTO);

        address.setCustomer(customerRepository.getReferenceById(customerId));

        Address savedAddress = addressRepository.save(address);
        return addressMapper.toDto(savedAddress);
    }

    @Transactional(readOnly = true)
    public AddressDTO getAddressById(Long id) {
        log.debug("Fetching address with id: {}", id);
        return addressRepository.findById(id)
                .map(addressMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public AddressDTO getAddressByCustomerId(Long customerId) {
        log.debug("Fetching address for customer id: {}", customerId);
        return addressMapper.toDto(getAddressEntityByCustomerId(customerId));
    }

    @Transactional(readOnly = true)
    public List<AddressDTO> getAllAddresses() {
        log.debug("Fetching all addresses");
        return addressRepository.findAll()
                .stream()
                .map(addressMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public AddressDTO updateAddress(Long id, @Valid AddressDTO addressDTO) {
        log.info("Updating address with id: {}", id);
        Address existingAddress = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + id));

        addressMapper.updateAddressFromDto(addressDTO, existingAddress);
        Address updatedAddress = addressRepository.save(existingAddress);
        return addressMapper.toDto(updatedAddress);
    }

    @Transactional
    public AddressDTO patchAddress(Long id, AddressDTO addressDTO) {
        Address existingAddress = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + id));

        addressMapper.updateAddressFromDto(addressDTO, existingAddress);
        Address updatedAddress = addressRepository.save(existingAddress);
        return addressMapper.toDto(updatedAddress);
    }

    @Transactional
    public void deleteAddress(Long id) {
        log.info("Deleting address with id: {}", id);
        if (!addressRepository.existsById(id)) {
            throw new ResourceNotFoundException("Address not found with id: " + id);
        }
        addressRepository.deleteById(id);
    }

    @Transactional
    public AddressDTO updateAddressByCustomerId(Long customerId, @Valid AddressDTO addressDTO) {
        log.info("Updating address for customer id: {}", customerId);
        Address address = getAddressEntityByCustomerId(customerId);
        addressMapper.updateAddressFromDto(addressDTO, address);
        Address updated = addressRepository.save(address);
        return addressMapper.toDto(updated);
    }

    @Transactional
    public void deleteAddressByCustomerId(Long customerId) {
        log.info("Deleting address for customer id: {}", customerId);
        Address address = getAddressEntityByCustomerId(customerId);
        addressRepository.delete(address);
    }

    @Transactional(readOnly = true)
    public List<AddressDTO> getAddressesByCity(String city) {
        log.debug("Fetching addresses for city: {}", city);
        return addressRepository.findByCity(city)
                .orElse(List.of())

                .stream()
                .map(addressMapper::toDto)
                .collect(Collectors.toList());
    }

}
