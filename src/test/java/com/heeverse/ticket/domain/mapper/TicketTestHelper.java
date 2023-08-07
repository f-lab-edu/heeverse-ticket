package com.heeverse.ticket.domain.mapper;

import com.heeverse.ticket.domain.entity.Ticket;
import com.heeverse.ticket.domain.entity.TicketGrade;
import com.heeverse.ticket.dto.TicketGradeDto;

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


    public static List<TicketGrade> toTicketGrade(List<TicketGradeDto> ticketGradeDtos, long concertId) {
        return ticketGradeDtos.stream()
                .map(dto -> TicketGrade.toEntity(dto, concertId))
                .toList();
    }


    public static List<Ticket> publishTicket(List<TicketGrade> ticketGrades) {

        return ticketGrades.stream()
                .map(Ticket::publish)
                .toList();
    }
}
