package com.iprody.crm.dto.create;

import com.iprody.crm.validator.ValidTelegram;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ContactDetailsDTO {
    private Long id;
    private UUID guid;
    @NotBlank(message = "email cannot be blank")
    @Size(max = 50, message = "email cannot be more than 50 characters")
    @Email(message = "email should be correct")
    private String email;
    @Size(max = 50, message = "telegram cannot be more than 50 characters")
    @ValidTelegram
    private String telegramId;
}
