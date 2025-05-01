package com.ecotrack.ecotrack.repository;

import com.ecotrack.ecotrack.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.EntityGraph;

import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {
    @EntityGraph(attributePaths = { "user" })
    Optional<Driver> findByUserId(Long userId);

    boolean existsByUserId(Long userId);
}