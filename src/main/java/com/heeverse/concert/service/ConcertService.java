package com.heeverse.concert.service;

import static org.springframework.transaction.annotation.Propagation.MANDATORY;

import com.heeverse.concert.domain.entity.Concert;
import com.heeverse.concert.domain.mapper.ConcertMapper;
import com.heeverse.concert.dto.ConcertRequestDto;
import com.heeverse.ticket.domain.mapper.GradeTicketMapper;
import com.heeverse.ticket.dto.GradeTicketDto;
import com.heeverse.ticket.domain.entity.GradeTicket;
import com.heeverse.ticket.dto.TicketGradeDto;
import com.heeverse.ticket.dto.TicketRequestDto;
import com.heeverse.ticket.service.TicketService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author jeongheekim
 * @date 2023/08/07
 */
@RequiredArgsConstructor
@Service
public class ConcertService {

    private final ConcertMapper concertMapper;
    private final TicketService ticketService;

    @Transactional(propagation = MANDATORY)
    public void registerConcert(List<ConcertRequestDto> listDto) {
        for (ConcertRequestDto dto : listDto) {
            Long concertId = concertMapper.insertConcert(new Concert(dto));

            TicketRequestDto ticketRequestDto = new TicketRequestDto(concertId, dto.getTicketGradeDtoList());
            ticketService.registerTicket(ticketRequestDto);
        }
    }
}
