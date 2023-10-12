package com.heeverse.concert.domain.entity;

import com.heeverse.concert.dto.persistence.RegisteredConcertMapperDto;
import com.heeverse.concert.dto.presentation.ConcertRequestDto;
import com.heeverse.concert.dto.presentation.RegisteredConcertResponseDto;
import com.heeverse.ticket.domain.mapper.TicketTestHelper;

import java.time.LocalDateTime;

/**
 * @author jeongheekim
 * @date 2023/08/28
 */
public class ConcertHelper {

    public static ConcertRequestDto normalDto() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime concertDate = now.plusYears(1);
        LocalDateTime ticketOpenTime = now.plusDays(10);
        LocalDateTime ticketEndTime = ticketOpenTime.plusDays(7);

        return new ConcertRequestDto("BTS 콘서트", concertDate,
            ticketOpenTime, ticketEndTime, 1L, 1L, TicketTestHelper.createTicketCategoryDtos());

    }

    public static ConcertRequestDto ticketOpenTimeAfterConcertDateDto() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime concertDate = now.plusYears(1);
        LocalDateTime ticketOpenTime = concertDate.plusDays(10);
        LocalDateTime ticketEndTime = ticketOpenTime.plusDays(7);

        return new ConcertRequestDto("BTS 콘서트", concertDate,
            ticketOpenTime, ticketEndTime, 1L, 1L, null);

    }

    public static ConcertRequestDto ticketOpenTimeAfterTicketEndTimeDto() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime concertDate = now.plusYears(1);
        LocalDateTime ticketOpenTime = now.plusDays(10);
        LocalDateTime ticketEndTime = ticketOpenTime.minusDays(7);

        return new ConcertRequestDto("BTS 콘서트", concertDate,
            ticketOpenTime, ticketEndTime, 1L, 1L, null);

    }

    public static Concert buildConcert() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime concertDate = now.plusYears(1);
        LocalDateTime ticketOpenTime = now.plusDays(10);
        LocalDateTime ticketEndTime = ticketOpenTime.plusDays(7);
        return new Concert(1L, 1L,"BTS 콘서트", concertDate,
                ticketOpenTime, ticketEndTime);
    }


    public static RegisteredConcertResponseDto mockRegisteredConcertResponseDto() {

        Concert concert = buildConcert();
        return new RegisteredConcertResponseDto(new RegisteredConcertMapperDto(
                concert.getConcertName(),
                concert.getConcertDate(),
                "방탄소년단",
                "BTS",
                "고척돔",
                50000
        ));
    }



}
