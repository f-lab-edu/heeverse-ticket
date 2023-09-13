package com.heeverse.ticket.domain.mapper;

import com.heeverse.ticket.dto.TicketGradeDto;
import com.heeverse.ticket.dto.TicketRequestDto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author gutenlee
 * @since 2023/08/07
 */
public class TicketTestHelper {

    public static List<TicketGradeDto> createTicketCategoryDtos() {
        return List.of(
                new TicketGradeDto( "VIP", 1500),
                new TicketGradeDto( "S석", 2000),
                new TicketGradeDto( "R석", 4000)
        );
    }


    public static TicketRequestDto createTicketRequestDto(long concertId, LocalDateTime concertDate) {
        return new TicketRequestDto(concertId, concertDate, createTicketCategoryDtos());
    }


    public static TicketRequestDto createTicketRequestDto(long concertId, LocalDateTime concertDate, List<TicketGradeDto> list) {
        return new TicketRequestDto(concertId, concertDate, list);
    }


    public static Long 콘서트_생성하고_시퀀스_반환() {
         return 1L;
    }
    public static List<Long> createTicketSeq() {
        return List.of(1L, 2L, 3L);
    }
}
