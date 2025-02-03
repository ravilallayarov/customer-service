package com.iprody.crm.util;

import com.iprody.crm.dto.getAll.RequestForGetAllCustomers;
import com.iprody.crm.entity.Customer;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.Map;

public class CustomerSpecification {
    private static final Map<String, String> SORT_BY_FIELD = Map.of(
            "id", "id",
            "name", "name",
            "surname", "surname",
            "countryName", "country.name");

    public static Specification<Customer> buildSpecification(RequestForGetAllCustomers request) {
        return Specification.where(hasName(request.getName()))
                .and(hasSurname(request.getSurname()))
                .and(hasCountryName(request.getCountryName()));
    }

    public static Specification<Customer> hasName(String name) {
        return ((root, query, criteriaBuilder) -> {
           if (name == null) {
               return criteriaBuilder.conjunction();
           }
           return criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        });
    }

    public static Specification<Customer> hasSurname(String surname) {
        return ((root, query, criteriaBuilder) -> {
            if (surname == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("surname")), "%" + surname.toLowerCase() + "%");
        });
    }

    public static Specification<Customer> hasCountryName(String countryName) {
        return ((root, query, criteriaBuilder) -> {
            if (countryName == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("country").get("name")),
                    "%" + countryName.toLowerCase() + "%");
        });
    }

    public static Sort buildSort(RequestForGetAllCustomers request) {
        Sort.Direction direction = Sort.Direction.fromString(request.getDirection());
        String sortBy = SORT_BY_FIELD.getOrDefault(request.getSortBy(), "id");
        return Sort.by(direction, sortBy);
    }
}
