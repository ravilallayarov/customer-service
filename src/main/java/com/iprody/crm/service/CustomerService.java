package com.iprody.crm.service;

import com.iprody.crm.dto.CustomerDTO;
import reactor.core.publisher.Mono;

public interface CustomerService {
    Mono<CustomerDTO> save(CustomerDTO customerDTO);
    Mono<CustomerDTO> findById(Long id);
}
