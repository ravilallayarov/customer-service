package com.iprody.crm.controller;

import com.iprody.crm.dto.CustomerDTO;
import com.iprody.crm.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping("/new")
    public Mono<ResponseEntity<CustomerDTO>> save(@Valid @RequestBody CustomerDTO customerDTO) {
        return customerService.save(customerDTO)
                .map(savedCustomerDTO -> ResponseEntity.status(HttpStatus.CREATED).body(savedCustomerDTO));
    }
}
