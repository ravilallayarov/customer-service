package com.iprody.crm.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Entity
@Table(name = "customers")
@Data
@EqualsAndHashCode(callSuper = false)
@SuperBuilder
@NoArgsConstructor
public class Customer extends BaseEntity {
    @Column(nullable = true)
    private UUID guid;
    @Column(length = 30, nullable = false)
    private String name;
    @Column(length = 30, nullable = false)
    private String surname;
    @ManyToOne
    @JoinColumn(name = "country_code_id")
    private Country country;
    @OneToOne(cascade = {CascadeType.REMOVE, CascadeType.MERGE})
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
