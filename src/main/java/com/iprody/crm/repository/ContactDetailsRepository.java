package com.iprody.crm.repository;

import com.iprody.crm.entity.ContactDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface ContactDetailsRepository extends JpaRepository<ContactDetails, Long> {
    boolean existsByEmail(String email);
    boolean existsByTelegramId(String telegramId);
    @Modifying
    @Query(value = "ALTER SEQUENCE contact_details_id_seq RESTART WITH 1", nativeQuery = true)
    @Transactional
    void resetAutoIncrement();
}
