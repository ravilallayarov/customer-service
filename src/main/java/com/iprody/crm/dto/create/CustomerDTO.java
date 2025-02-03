package com.iprody.crm.dto.create;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;


import java.util.UUID;

@Data
@Builder
public class CustomerDTO {
    private Long id;
    private UUID guid;
    @NotBlank(message = "customer name cannot be blank")
    @Size(max = 30, message = "customer name cannot be more than 30 characters")
    private String name;
    @NotBlank(message = "customer surname cannot be blank")
    @Size(max = 30, message = "customer surname cannot be more than 30 characters")
    private String surname;
    @Valid
    private CountryDTO countryDTO;
    @Valid
    private ContactDetailsDTO contactDetailsDTO;
    private UUID profileRef;
}
