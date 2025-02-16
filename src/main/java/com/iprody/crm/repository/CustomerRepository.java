package com.iprody.crm.repository;

import com.iprody.crm.entity.Customer;
import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.transaction.annotation.Transactional;

public interface CustomerRepository extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {
    @Modifying
    @Query(value = "ALTER SEQUENCE customers_id_seq RESTART WITH 1", nativeQuery = true)
    @Transactional
    void resetAutoIncrement();

    @Override
    @Nonnull
    @EntityGraph(attributePaths = {"country", "contactDetails"})
    Page<Customer> findAll(Specification<Customer> spec, @Nonnull Pageable pageable);
}
