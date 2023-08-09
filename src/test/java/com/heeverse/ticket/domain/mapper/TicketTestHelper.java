package com.heeverse.ticket.domain.mapper;

import com.heeverse.ticket.domain.entity.Ticket;
import com.heeverse.ticket.domain.entity.TicketGrade;
import com.heeverse.ticket.dto.TicketGradeDto;

import java.util.List;
import java.util.stream.IntStream;

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
                .map(dto -> new TicketGrade(dto, concertId))
                .toList();
    }


    public static List<Ticket> publishTicket(List<TicketGrade> ticketGrades) {

        return ticketGrades.stream()
                .flatMap(grade -> {
                    return IntStream.rangeClosed(0, grade.getTicketCount())
                            .mapToObj(i -> new Ticket(grade));
                })
                .toList();
    }
}
