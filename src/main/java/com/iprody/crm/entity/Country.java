package com.iprody.crm.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "countries")
@Data
@EqualsAndHashCode(callSuper = false)
public class Country extends BaseEntity {
    @Column(nullable = true)
    private UUID guid;
    @Column(name = "country_code", unique = true, length = 3)
    private String countryCode;
    @Column(nullable = false, length = 30)
    private String name;
    @OneToMany(mappedBy = "country")
    @JsonIgnore
    private List<Customer> customerList;

    @Override
    public String toString() {
        return "Country{"
                + "id=" + getId()
                + ", guid=" + guid
                + ", countryCode='" + countryCode + '\''
                + ", name='" + name + '\''
                + '}';
    }
}
