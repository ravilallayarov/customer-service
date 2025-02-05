package com.iprody.crm.service;

import com.iprody.crm.dto.create.CustomerDTO;
import com.iprody.crm.dto.update.CustomerUpdateDTO;
import com.iprody.crm.entity.Customer;
import reactor.core.publisher.Mono;

public interface CustomerService {
    Mono<CustomerDTO> save(CustomerDTO customerDTO);
    Mono<Customer> findById(Long id);
    Mono<Customer> updateById(Long id, CustomerUpdateDTO customerUpdateDTO);
    Mono<Void> deleteById(Long id);
}
