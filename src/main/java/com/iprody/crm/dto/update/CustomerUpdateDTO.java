package com.iprody.crm.dto.update;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerUpdateDTO {
    @Size(max = 30, message = "customer name cannot be more than 30 characters")
    private String name;
    @Size(max = 30, message = "customer surname cannot be more than 30 characters")
    private String surname;
    @Valid
    private CountryUpdateDTO countryUpdateDTO;
    @Valid
    private ContactDetailsUpdateDTO contactDetailsUpdateDTO;
}
