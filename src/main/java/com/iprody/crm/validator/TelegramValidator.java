package com.iprody.crm.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TelegramValidator implements ConstraintValidator<ValidTelegram, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return s != null && s.startsWith("@");
    }
}
