package com.iprody.crm.dto.login;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequestDTO {
    @Size(min = 2, max = 30, message = "username length must be between 2 and 30 characters")
    @NotBlank(message = "username cannot be blank")
    private String username;
    @Size(min = 8, max = 30, message = "password length must be between 8 and 30 characters")
    @NotBlank(message = "password cannot be blank")
    private String password;
}
