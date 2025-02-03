package com.iprody.crm.dto.getAll;

import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

@Data
@NoArgsConstructor
public class RequestForGetAllCustomers {
    private String name;
    private String surname;
    private String countryName;
    private String sortBy = "id";
    private String direction = "ASC";
    @Range(min = 1, max = 100)
    private Integer limit = 25;
    @Min(value = 0)
    private Integer page = 0;
}
