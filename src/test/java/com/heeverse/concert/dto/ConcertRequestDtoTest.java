package com.heeverse.concert.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.heeverse.ticket.dto.TicketGradeDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
    void requestDto_validation_success_test() {

        LocalDateTime concertDate = LocalDateTime.parse("2023-12-15T10:00:00");
        LocalDateTime ticketOpenTime = LocalDateTime.parse("2023-10-15T10:00:00");
        LocalDateTime ticketEndTime = LocalDateTime.parse("2023-10-17T10:00:00");

        List<TicketGradeDto> ticketGradeDtoList = new ArrayList<>();
        TicketGradeDto ticketGradeDto = new TicketGradeDto("VIP", 100);
        ticketGradeDtoList.add(ticketGradeDto);

        ConcertRequestDto dto = new ConcertRequestDto("BTS 콘서트", concertDate,
            ticketOpenTime, ticketEndTime, 1L, 1L, null);


        Set<ConstraintViolation<ConcertRequestDto>> violationSet = validator.validate(dto);
        assertThat(violationSet.size()).isZero();
    }
}