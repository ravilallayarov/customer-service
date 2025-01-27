package com.iprody.crm.dto.update;

import com.iprody.crm.validator.ValidTelegram;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContactDetailsUpdateDTO {
    @Size(max = 50, message = "email cannot be more than 50 characters")
    @Email(message = "email should be correct")
    private String email;
    @Size(max = 50, message = "telegram cannot be more than 50 characters")
    @ValidTelegram
    private String telegramId;
}
