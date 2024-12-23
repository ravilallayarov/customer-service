package com.iprody.crm.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test/hello")
@Slf4j
public class HelloController {

    @GetMapping
    public ResponseEntity<Void> getHello() {
        log.info("Hello my friend");
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
