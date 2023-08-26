package com.heeverse.ticket.dto;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.heeverse.ticket.domain.mapper.TicketTestHelper;
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

class TicketRequestDtoTest {

    private static Validator validator;
    private static Long concertSeq;

    @BeforeAll
    public static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        concertSeq = TicketTestHelper.콘서트_생성하고_시퀀스_반환();
    }


    @Test
    @DisplayName("TicketRequestDto 유효성 검사시 필드 TicketGradeDto 검사도 함께 수행한다")
    void ticketRequestDto_ticketGradeDtoList_field_should_not_null() throws Exception {

        List<TicketGradeDto> ticketGradeDtoList = new ArrayList<>();
        ticketGradeDtoList.add(new TicketGradeDto("VIP석", 0));
        TicketRequestDto ticketRequestDto = TicketTestHelper.createTicketRequestDto(concertSeq, LocalDateTime.now(), ticketGradeDtoList);

        Set<ConstraintViolation<TicketRequestDto>> violations = validator.validate(
            ticketRequestDto);

        ConstraintViolation<TicketRequestDto> violation = violations.iterator().next();
        assertEquals("ticketGradeDtoList[0].seatCount", violation.getPropertyPath().toString());
        assertEquals("1 이상이어야 합니다", violation.getMessage());
    }


    @Test
    @DisplayName("@NotNull 위반 개수 검사")
    void ticketRequestDto_not_null() {

        assertAll(
            () -> assertViolationCount(new TicketRequestDto(null, null, null), 2),
            () -> assertViolationCount(new TicketRequestDto(concertSeq, null, null), 1)
        );

    }


    @Test
    @DisplayName("@Min=1 유효성 위반 개수 테스트")
    void min_violation_count() {
         assertAll(
             () -> assertViolationCount(new TicketGradeDto("VIP", 0), 1),
             () -> assertViolationCount(new TicketGradeDto("VIP", 1), 0),
             () -> assertViolationCount(new TicketGradeDto("VIP", 2), 0)
         );
    }


    @Test
    @DisplayName("@NotBlank 유효성 위반 개수 테스트")
    void notBlank_violation_test() {
        assertViolationCount(new TicketGradeDto( "VIP", 1), 0);
        assertViolationCount(new TicketGradeDto( "", 1), 1);
        assertViolationCount(new TicketGradeDto( null, 1), 1);
    }



    static <T> void assertViolationCount(T obj, int expectedViolationSize) {

        Set<ConstraintViolation<T>> violations = validator.validate(obj);
        assertEquals(expectedViolationSize, violations.size());
    }


}