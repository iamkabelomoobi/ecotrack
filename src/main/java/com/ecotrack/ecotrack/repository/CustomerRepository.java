package com.ecotrack.ecotrack.repository;

import com.ecotrack.ecotrack.entity.Customer;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @EntityGraph(attributePaths = { "user" })
    Optional<Customer> findByUserId(Long userId);

    boolean existsByUserId(Long userId);
}