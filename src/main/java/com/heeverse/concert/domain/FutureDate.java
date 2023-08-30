package com.heeverse.concert.domain;

import com.heeverse.common.Constants;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.CompositionType;
import org.hibernate.validator.constraints.ConstraintComposition;
import org.springframework.format.annotation.DateTimeFormat;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
/**
* @author jeongheekim
* @date 2023/08/18
*/


@Target(FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@NotNull
@Future
@DateTimeFormat(pattern = Constants.DATE_FORMAT)
@Constraint(validatedBy = {})
@ConstraintComposition(CompositionType.AND)
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