package com.ecotrack.ecotrack.repository;

import com.ecotrack.ecotrack.entity.Admin;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    @EntityGraph(attributePaths = { "user" })
    Optional<Admin> findByUserId(Long userId);

    boolean existsByUserId(Long userId);
}