package com.iprody.crm.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Entity
@Table(name = "customers")
@Data
@EqualsAndHashCode(callSuper = false)
public class Customer extends BaseEntity {
    @Column(nullable = true)
    private UUID guid;
    @Column(length = 30, nullable = false)
    private String name;
    @Column(length = 30, nullable = false)
    private String surname;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_code_id")
    private Country country;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contact_details_id")
    private ContactDetails contactDetails;
    @Column(name = "profile_ref", nullable = true)
    private UUID profileRef;

    @Override
    public String toString() {
        return "Customer{"
                + "id=" + getId()
                + ", guid=" + guid
                + ", name='" + name + '\''
                + ", surname='" + surname + '\''
                + ", country=" + country
                + ", contactDetails=" + contactDetails
                + ", profileRef=" + profileRef
                + '}';
    }
}
