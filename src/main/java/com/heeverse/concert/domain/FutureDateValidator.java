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
        System.out.println("value = " + value);
        if(value == null) {
            return false;
        }
        System.out.println("value111111 = " + value);
        value.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        LocalDateTime now = LocalDateTime.now();
        System.out.println("now = " + now);
        return value.isAfter(LocalDateTime.now());

    }
}
