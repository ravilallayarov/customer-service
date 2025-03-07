package com.iprody.crm.controller;

import com.iprody.crm.dto.login.AuthRequestDTO;
import com.iprody.crm.security.SecurityServiceClient;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final SecurityServiceClient securityServiceClient;

    @PostMapping("/login")
    Mono<ResponseEntity<String>> authenticate(@RequestBody @Valid AuthRequestDTO request) {
        return Mono.fromCallable(() -> ResponseEntity.ok(securityServiceClient.authenticate(request)));
    }

    @PostMapping("/register")
    Mono<ResponseEntity<AuthRequestDTO>> register(@RequestBody @Valid AuthRequestDTO request) {
        return Mono.fromCallable(() -> ResponseEntity.status(HttpStatus.CREATED).body(
                securityServiceClient.register(request)));
    }
}
