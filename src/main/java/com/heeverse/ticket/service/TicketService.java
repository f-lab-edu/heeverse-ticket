package com.heeverse.ticket.service;

import com.heeverse.common.TicketSerialNumber;
import com.heeverse.common.TicketSerialTokenDto;
import com.heeverse.ticket.domain.entity.Ticket;
import com.heeverse.ticket.domain.entity.GradeTicket;
import com.heeverse.ticket.domain.mapper.TicketMapper;
import com.heeverse.ticket.dto.TicketRequestDto;
import com.heeverse.ticket.exception.DuplicatedTicketException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @author gutenlee
 * @since 2023/08/04
 */
@Service
public class TicketService {

    private final TicketMapper ticketMapper;
    private final TicketSerialNumber serial = new TicketSerialNumber();

    public TicketService(TicketMapper ticketMapper) {
        this.ticketMapper = ticketMapper;
    }

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
        return 0 < ticketMapper.countTicket(ticketRequestDto.concertId());
    }


    private List<GradeTicket> saveGradeTicket(TicketRequestDto ticketRequestDto) {

        List<GradeTicket> gradeTickets = ticketRequestDto.ticketGradeDtoList().stream()
                .map(dto -> new GradeTicket(dto, ticketRequestDto.concertId()))
                .toList();

        ticketMapper.insertTicketGrade(gradeTickets);

        return gradeTickets;
    }

    private void saveTicket(List<GradeTicket> gradeTickets, TicketRequestDto ticketRequestDto) {

        List<Ticket> tickets = gradeTickets.stream()
                .flatMap(grade ->
                    IntStream.range(0, grade.getTicketCount())
                            .mapToObj(idx -> new Ticket(generateTicketSerialNumber(ticketRequestDto, grade, idx), grade))
                )
                .toList();

        ticketMapper.insertTicket(tickets);
    }


    private String generateTicketSerialNumber(TicketRequestDto ticketRequestDto, GradeTicket grade, int idx) {

        // TODO - 공연날짜, 공연장 시퀀스, 시리얼 번호
        int idxLength = Integer.toString(grade.getTicketCount()).length();

        return serial.generate(new TicketSerialTokenDto(LocalDate.now(), 1L, grade.getGradeName(), idx, idxLength));

    }



}
