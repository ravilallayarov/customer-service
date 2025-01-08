package com.iprody.crm.validator;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = TelegramValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidTelegram {
    String message() default "Telegram should start with '@'";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
