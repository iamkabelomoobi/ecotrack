package com.ecotrack.ecotrack.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecotrack.ecotrack.entity.Address;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    Optional<Address> findByCustomerId(Long customerId);

    Optional<Address> findByPostalCode(String postalCode);

    Optional<List<Address>> findByCity(String city);
}
