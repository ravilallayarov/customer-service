package com.iprody.crm.controller;

import com.iprody.crm.dto.getAll.RequestForGetAllCustomers;
import com.iprody.crm.dto.create.CustomerDTO;
import com.iprody.crm.dto.update.CustomerUpdateDTO;
import com.iprody.crm.entity.Customer;
import com.iprody.crm.mapper.CustomerMapper;
import com.iprody.crm.service.impl.CustomerServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerServiceImpl customerService;
    private final CustomerMapper customerMapper;

    @PostMapping("/new")
    public Mono<ResponseEntity<CustomerDTO>> save(@Valid @RequestBody CustomerDTO customerDTO) {
        return customerService.save(customerDTO)
                .map(savedCustomerDTO -> ResponseEntity.status(HttpStatus.CREATED).body(savedCustomerDTO));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<CustomerDTO>> findById(@PathVariable Long id) {
        return customerService.findById(id)
                .map(customerMapper::toDto)
                .map(ResponseEntity::ok);
    }

    @PostMapping("/{id}/update")
    public Mono<ResponseEntity<CustomerDTO>> update(@PathVariable Long id, @Valid @RequestBody CustomerUpdateDTO customerUpdateDTO) {
        return customerService.updateById(id, customerUpdateDTO)
                .map(customerMapper::toDto)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{id}/delete")
    public Mono<ResponseEntity<Void>> delete(@PathVariable Long id) {
        return customerService.deleteById(id)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }

    @GetMapping
    public Mono<ResponseEntity<List<CustomerDTO>>> getAll(@Valid @ModelAttribute RequestForGetAllCustomers requestForGetAllCustomers) {
        return customerService.getAll(requestForGetAllCustomers)
                .map(this::toDTOList)
                .map(ResponseEntity::ok);
    }

    private List<CustomerDTO> toDTOList(List<Customer> customers) {
        List<CustomerDTO> customerDTOList = new ArrayList<>();
        customers.forEach(customer -> customerDTOList.add(customerMapper.toDto(customer)));
        return customerDTOList;
    }
}
