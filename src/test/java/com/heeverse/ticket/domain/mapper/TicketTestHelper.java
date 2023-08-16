package com.heeverse.ticket.domain.mapper;

import com.heeverse.ticket.dto.TicketGradeDto;
import com.heeverse.ticket.dto.TicketRequestDto;

import java.time.LocalDate;
import java.util.List;

/**
 * @author gutenlee
 * @since 2023/08/07
 */
public class TicketTestHelper {

    public static List<TicketGradeDto> createTicketCategoryDtos() {
        return List.of(
                new TicketGradeDto(1, "VIP", 1500),
                new TicketGradeDto(2, "S석", 2000),
                new TicketGradeDto(3, "R석", 4000)
        );
    }


    public static TicketRequestDto createTicketRequestDto(long concertId, LocalDate concertDate) {
        return new TicketRequestDto(concertId, concertDate, createTicketCategoryDtos());
    }


    public static TicketRequestDto createTicketRequestDto(long concertId, LocalDate concertDate, List<TicketGradeDto> list) {
        return new TicketRequestDto(concertId, concertDate, list);
    }

}
