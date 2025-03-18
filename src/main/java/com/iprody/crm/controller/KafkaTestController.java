package com.iprody.crm.controller;

import com.iprody.crm.dto.login.AuthRequestDTO;
import com.iprody.crm.service.KafkaProducerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/kafka")
@RequiredArgsConstructor
public class KafkaTestController {
    private final KafkaProducerService kafkaProducerService;

    @PostMapping("/send")
    public Mono<ResponseEntity<AuthRequestDTO>> sendMessage(@RequestBody @Valid AuthRequestDTO authRequestDTO) {
        kafkaProducerService.sendMessage(authRequestDTO);
        return Mono.just(ResponseEntity.ok(authRequestDTO));
    }
}
