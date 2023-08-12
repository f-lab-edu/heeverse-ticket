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
    @DisplayName("TicketRequestDto 의 필드 유효성 위반 개수 검사")
    void ticketRequestDto_not_null() throws Exception {

        assertViolationCount(new TicketRequestDto(null, null), 2);
        assertViolationCount(new TicketRequestDto(1L, null), 1);
    }


    @Test
    @DisplayName("TicketRequestDto 유효성 검사시 필드 TicketGradeDto 검사도 함께 수행한다")
    void ticketRequestDto_ticketGradeDtoList_field_should_not_null() throws Exception {


        List<TicketGradeDto> ticketGradeDtoList = new ArrayList<>();
        ticketGradeDtoList.add(new TicketGradeDto( "VIP석", 0));
        TicketRequestDto ticketRequestDto = new TicketRequestDto(1L, ticketGradeDtoList);


        Set<ConstraintViolation<TicketRequestDto>> violations = validator.validate(ticketRequestDto);


        ConstraintViolation<TicketRequestDto> violation = violations.iterator().next();
        assertEquals("ticketGradeDtoList[0].seatCount", violation.getPropertyPath().toString());
        assertEquals("1 이상이어야 합니다", violation.getMessage());
    }


    @Test
    @DisplayName("ticketGradeDto 필드 유효성 위반 개수 검사")
    void ticketGradeDto_validation_test() throws Exception {

        assertViolationCount(new TicketGradeDto( "VIP", 10000), 1);
        assertViolationCount(new TicketGradeDto( "VIP", 0), 2);
        assertViolationCount(new TicketGradeDto( "", 0), 3);
        assertViolationCount(new TicketGradeDto( null, 0), 3);

    }


    static <T> void assertViolationCount(T obj, int expectedViolationSize) {

        Set<ConstraintViolation<T>> violations = validator.validate(obj);
        assertEquals(expectedViolationSize, violations.size());
    }


}