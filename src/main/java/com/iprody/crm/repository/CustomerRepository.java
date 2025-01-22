package com.iprody.crm.repository;

import com.iprody.crm.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @Modifying
    @Query(value = "ALTER SEQUENCE customers_id_seq RESTART WITH 1", nativeQuery = true)
    @Transactional
    void resetAutoIncrement();
}
