package com.iprody.crm.security;

import com.iprody.crm.dto.login.AuthRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "security-service", url = "http://localhost:8081")
public interface SecurityServiceClient {

    @PostMapping("auth/register")
    AuthRequestDTO register(@RequestBody AuthRequestDTO request);

    @PostMapping("auth/login")
    String authenticate(@RequestBody AuthRequestDTO request);

    @PostMapping("auth/validate")
    boolean tokenValidate(@RequestBody String token);
}
