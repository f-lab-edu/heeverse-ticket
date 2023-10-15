package com.heeverse.concert.dto;

import com.heeverse.concert.domain.entity.ConcertHelper;
import com.heeverse.concert.dto.presentation.ConcertRequestDto;
import com.heeverse.ticket.dto.TicketGradeDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author jeongheekim
 * @date 2021/08/08
 */
class ConcertRequestDtoTest {

    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    public static void init(){
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @DisplayName("concertDate, ticketOpenTime,ticketEndTime은 요창하는 시간보다 미래여야한다.")
    @Test
    void requestDtoValidationSuccessTest() {
        ConcertRequestDto concertRequestDto = ConcertHelper.normalDto();
        Set<ConstraintViolation<ConcertRequestDto>> violationSet = validator.validate(concertRequestDto);
        assertThat(violationSet.size()).isZero();
    }
}