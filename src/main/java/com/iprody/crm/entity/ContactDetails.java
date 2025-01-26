package com.iprody.crm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Entity
@Table(name = "contact_details")
@Data
@EqualsAndHashCode(callSuper = false)
@SuperBuilder
@NoArgsConstructor
public class ContactDetails extends BaseEntity {
    @Column(nullable = true)
    private UUID guid;
    @Column(length = 50, nullable = false, unique = true)
    private String email;
    @Column(name = "telegram_id", length = 50, nullable = true, unique = true)
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
