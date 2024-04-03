package com.driver.Config;

import com.driver.Models.AdminInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// Repository interface for managing AdminInfo entities in the database
public interface AuthRepository extends JpaRepository<AdminInfo, Integer> {
    // Query method to find an AdminInfo entity by email
    Optional<AdminInfo> findByEmail(String email);
}
