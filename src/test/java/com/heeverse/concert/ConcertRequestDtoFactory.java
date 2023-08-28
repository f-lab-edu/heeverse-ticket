package com.heeverse.concert;

import com.heeverse.concert.dto.presentation.ConcertRequestDto;
import java.time.LocalDateTime;

/**
 * @author jeongheekim
 * @date 2023/08/28
 */
public class ConcertRequestDtoFactory {

    public static ConcertRequestDto normalDto() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime concertDate = now.plusYears(1);
        LocalDateTime ticketOpenTime = now.plusDays(10);
        LocalDateTime ticketEndTime = ticketOpenTime.plusDays(7);

        return new ConcertRequestDto("BTS 콘서트", concertDate,
            ticketOpenTime, ticketEndTime, 1L, 1L, null);

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


}
