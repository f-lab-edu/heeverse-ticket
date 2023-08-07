package com.heeverse.ticket.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TicketRequestDtoTest {

    private static Validator validator;

    @BeforeAll
    public static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }


    @Test
    @DisplayName("TicketRequestDto 의 모든 필드는 NotNull 검사 수행된다")
    void ticketRequestDto_not_null() throws Exception {
        TicketRequestDto ticketRequestDto = new TicketRequestDto(null, null);

        Set<ConstraintViolation<TicketRequestDto>> violations = validator.validate(ticketRequestDto);

        Assertions.assertEquals(TicketRequestDto.class.getDeclaredFields().length, violations.size());
    }


    @Test
    @DisplayName("TicketRequestDto 유효성 검사시 필드 TicketGradeDto 검사도 함께 수행한다")
    void ticketRequestDto_ticketGradeDtoList_field_should_not_null() throws Exception {


        List<TicketGradeDto> ticketGradeDtoList = new ArrayList<>();
        ticketGradeDtoList.add(new TicketGradeDto(1, "VIP석", null));
        TicketRequestDto ticketRequestDto = new TicketRequestDto(1L, ticketGradeDtoList);


        Set<ConstraintViolation<TicketRequestDto>> violations = validator.validate(ticketRequestDto);


        ConstraintViolation<TicketRequestDto> violation = violations.iterator().next();
        assertEquals("ticketGradeDtoList[0].seatCount", violation.getPropertyPath().toString());
        assertEquals("널이어서는 안됩니다", violation.getMessage());
    }


}