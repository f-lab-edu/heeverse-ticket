package com.heeverse.concert.domain;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author jeongheekim
 * @date 2023/08/18
 */
public class FutureDateValidator implements ConstraintValidator<FutureDate, LocalDateTime> {

    @Override
    public boolean isValid(LocalDateTime value, ConstraintValidatorContext context) {
        if(value == null) {
            return false;
        }
        value.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        return value.isAfter(LocalDateTime.now());

    }
}
