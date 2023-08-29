package com.heeverse.concert.domain.entity;

import com.heeverse.concert.ConcertRequestDtoFactory;
import com.heeverse.concert.exception.ConcertTimeValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author jeongheekim
 * @date 2023/08/28
 */
class ConcertTest {
    @Test
    @DisplayName("concertDate가 ticketOpenTime보다 미래다.")
    void validateConcertDateAndTicketOpenTimeSuccessTest() {
        Concert concert = new Concert(ConcertRequestDtoFactory.normalDto());
        Assertions.assertTrue(concert.getConcertDate().isAfter(concert.getTicketOpenTime()));

    }

    @Test
    @DisplayName("ticketEndTime이 ticketOpenTime보다 미래다.")
    void validateTicketOpenTimeAndTicketEndTimeSuccessTest() {
        Concert concert = new Concert(ConcertRequestDtoFactory.normalDto());
        Assertions.assertTrue(concert.getTicketEndTime().isAfter(concert.getTicketOpenTime()));

    }

    @Test
    @DisplayName("ticketOpenTime이 concertDate보다 미래면 ConcertTimeValidationException을 던진다.")
    void validateConcertTimeAndTicketOpenTimeTest() {
        Assertions.assertThrows(ConcertTimeValidationException.class, () -> new Concert(
                ConcertRequestDtoFactory.ticketOpenTimeAfterConcertDateDto()));

    }

    @Test
    @DisplayName("ticketOpenTime이 ticketEndTime보다 미래이면 ConcertTimeValidationException을 던진다.")
    void validateTicketOpenTimeAndTicketEndTimeTest() {
        Assertions.assertThrows(ConcertTimeValidationException.class,
                () -> new Concert(ConcertRequestDtoFactory.ticketOpenTimeAfterTicketEndTimeDto()));

    }
}