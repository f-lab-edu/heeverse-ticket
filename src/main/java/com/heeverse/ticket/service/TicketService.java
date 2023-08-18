package com.heeverse.ticket.service;

import com.heeverse.ticket.domain.TicketSerialNumber;
import com.heeverse.ticket.domain.TicketSerialTokenDto;
import com.heeverse.ticket.domain.entity.GradeTicket;
import com.heeverse.ticket.domain.entity.Ticket;
import com.heeverse.ticket.domain.mapper.TicketMapper;
import com.heeverse.ticket.dto.TicketRequestDto;
import com.heeverse.ticket.exception.DuplicatedTicketException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

/**
 * @author gutenlee
 * @since 2023/08/04
 */
@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketMapper ticketMapper;


    @Transactional
    public void registerTicket(TicketRequestDto ticketRequestDto) {

        if (existTicket(ticketRequestDto)){
            throw new DuplicatedTicketException();
        }

        saveTicket(saveGradeTicket(ticketRequestDto), ticketRequestDto);
    }

    @Transactional(readOnly = true)
    public List<Ticket> getTicket(long concertSeq){
        return ticketMapper.findTickets(concertSeq);
    }


    private boolean existTicket(TicketRequestDto ticketRequestDto) {
        return 0 < ticketMapper.countTicket(ticketRequestDto.concertSeq());
    }


    private List<GradeTicket> saveGradeTicket(TicketRequestDto ticketRequestDto) {

        List<GradeTicket> gradeTickets = ticketRequestDto.ticketGradeDtoList().stream()
                .map(dto -> new GradeTicket(dto, ticketRequestDto.concertSeq()))
                .toList();

        ticketMapper.insertTicketGrade(gradeTickets);

        return gradeTickets;
    }

    private void saveTicket(List<GradeTicket> gradeTickets, TicketRequestDto ticketRequestDto) {

        List<Ticket> tickets = gradeTickets.stream()
                .flatMap(grade ->
                    IntStream.range(0, grade.getTicketCount())
                            .mapToObj(idx -> new Ticket(new TicketSerialNumber(
                                        new TicketSerialTokenDto(ticketRequestDto.concertDate(), ticketRequestDto.concertSeq(), grade, idx)
                                    ), grade)
                            )
                )
                .toList();

        ticketMapper.insertTicket(tickets);
    }

}
