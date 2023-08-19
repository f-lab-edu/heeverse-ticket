package com.heeverse.concert.domain;

import static java.lang.annotation.ElementType.FIELD;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author jeongheekim
 * @date 2023/08/18
 */
@Target(FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FutureDateValidator.class)
public @interface FutureDate {

    String message() default "request 날짜는 현재 시간보다 미래여야합니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target(FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {

        FutureDate[] value();
    }
}
