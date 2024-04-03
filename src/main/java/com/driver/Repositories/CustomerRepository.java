package com.driver.Repositories;

import com.driver.Models.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByEmail(String emailId);

    Optional<Customer> findByUuid(String uuid);

    @Query(value = "SELECT * FROM customers ORDER BY first_name ASC", nativeQuery = true)
    Page<Customer> findByFirstNameASC(Pageable pageable);

    @Query(value = "SELECT * FROM customers ORDER BY first_name DESC", nativeQuery = true)
    Page<Customer> findByFirstNameDESC(Pageable pageable);
}
