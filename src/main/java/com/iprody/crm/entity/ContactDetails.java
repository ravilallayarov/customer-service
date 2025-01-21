package com.iprody.crm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "contact_details")
@Data
@EqualsAndHashCode(callSuper = false)
public class ContactDetails extends BaseEntity {
    @Column(nullable = true)
    private UUID guid;
    @Column(length = 50, nullable = false, unique = true)
    private String email;
    @Column(name = "telegram_id", length = 50, nullable = true)
    private String telegramId;
    @OneToOne(mappedBy = "contactDetails")
    @JsonIgnore
    private Customer customer;

    @Override
    public String toString() {
        return "ContactDetails{"
                + "id=" + getId()
                + ", guid=" + guid
                + ", email='" + email + '\''
                + ", telegramId='" + telegramId + '\''
                + '}';
    }
}
